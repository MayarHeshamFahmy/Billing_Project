package com.localbrand.service.impl;

import com.localbrand.dto.RatedCDR;
import com.localbrand.model.CDR;
import com.localbrand.model.Customer;
import com.localbrand.model.Service;
import com.localbrand.model.ServicePackage;
import com.localbrand.model.ServiceSubscription;
import com.localbrand.service.RatingEngine;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.Optional;

@Stateless
public class RatingEngineImpl implements RatingEngine {

    @Override
    @Transactional
    public RatedCDR rateCDR(CDR cdr, Customer customer) {
        RatedCDR ratedCDR = new RatedCDR();
        ratedCDR.setDialA(cdr.getDialA());
        ratedCDR.setDialB(cdr.getDialB());
        ratedCDR.setServiceType(cdr.getServiceType());
        ratedCDR.setUsage(cdr.getUsage());
        ratedCDR.setStartTime(cdr.getStartTime());
        ratedCDR.setExternalCharges(cdr.getExternalCharges());

        // Get active subscription for the customer
        Optional<ServiceSubscription> activeSubscription = customer.getSubscriptions().stream()
                .filter(sub -> sub.getActive())
                .findFirst();

        if (activeSubscription.isPresent()) {
            ServiceSubscription subscription = activeSubscription.get();
            ServicePackage servicePackage = subscription.getServicePackage();

            // Find the matching service in the package
            Optional<Service> matchingService = servicePackage.getServices().stream()
                    .filter(service -> service.getType() == cdr.getServiceType())
                    .findFirst();

            if (matchingService.isPresent()) {
                Service service = matchingService.get();
                ratedCDR.setUnitPrice(service.getUnitPrice());

                // Calculate free units usage
                int remainingFreeUnits = subscription.getRemainingFreeUnits();
                int usage = cdr.getUsage().intValue();
                int freeUnitsUsed = Math.min(remainingFreeUnits, usage);
                int paidUnits = usage - freeUnitsUsed;

                ratedCDR.setFreeUnitsUsed(freeUnitsUsed);
                ratedCDR.setPaidUnits(paidUnits);

                // Get time zone multiplier
                LocalTime callTime = cdr.getStartTime().toLocalTime();
                BigDecimal timeZoneMultiplier = service.getRateMultiplierForTime(callTime);

                // Get destination zone multiplier
                BigDecimal destinationMultiplier = service.getRateMultiplierForDestination(cdr.getDialB());

                // Calculate rated amount with multipliers
                BigDecimal baseAmount = service.getUnitPrice()
                        .multiply(BigDecimal.valueOf(paidUnits))
                        .multiply(timeZoneMultiplier)
                        .multiply(destinationMultiplier);

                // Add external charges
                BigDecimal ratedAmount = baseAmount
                        .add(BigDecimal.valueOf(cdr.getExternalCharges()))
                        .setScale(2, RoundingMode.HALF_UP);

                ratedCDR.setRatedAmount(ratedAmount);
                ratedCDR.setRatingNotes(String.format(
                    "Rated using service package: %s (Time Zone: %s, Destination: %s)",
                    servicePackage.getName(),
                    timeZoneMultiplier.equals(BigDecimal.ONE) ? "Standard" : "Special",
                    destinationMultiplier.equals(BigDecimal.ONE) ? "Standard" : "Special"
                ));
            } else {
                // No matching service found in the package
                ratedCDR.setRatedAmount(BigDecimal.valueOf(cdr.getExternalCharges()));
                ratedCDR.setRatingNotes("No matching service found in the package");
            }
        } else {
            // No active subscription found
            ratedCDR.setRatedAmount(BigDecimal.valueOf(cdr.getExternalCharges()));
            ratedCDR.setRatingNotes("No active subscription found");
        }

        return ratedCDR;
    }

    @Override
    @Transactional
    public void updateFreeUnits(Customer customer, RatedCDR ratedCDR) {
        customer.getSubscriptions().stream()
                .filter(sub -> sub.getActive())
                .findFirst()
                .ifPresent(subscription -> {
                    int currentFreeUnits = subscription.getRemainingFreeUnits();
                    int newFreeUnits = currentFreeUnits - ratedCDR.getFreeUnitsUsed();
                    subscription.setRemainingFreeUnits(Math.max(0, newFreeUnits));
                });
    }
} 