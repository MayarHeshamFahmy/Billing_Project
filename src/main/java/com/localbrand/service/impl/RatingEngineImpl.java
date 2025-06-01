package com.localbrand.service.impl;

import com.localbrand.dto.RatedCDR;
import com.localbrand.model.*;
import com.localbrand.service.RatingEngine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class RatingEngineImpl implements RatingEngine {

    @Override
    public RatedCDR rateCDR(CDR cdr, Customer customer) {
        RatedCDR ratedCDR = new RatedCDR();
        ratedCDR.setDialA(cdr.getDialA());
        ratedCDR.setDialB(cdr.getDialB());
        ratedCDR.setStartTime(cdr.getStartTime());
        ratedCDR.setServiceType(cdr.getServiceType());
        ratedCDR.setUsage(cdr.getUsage());

        List<ServiceSubscription> subscriptions = customer.getSubscriptions();
        ServiceSubscription matchedSubscription = null;
        Service matchedService = null;

        boolean hasActiveSubscription = false;

        for (ServiceSubscription subscription : subscriptions) {
            if (!subscription.getActive()) continue;
            hasActiveSubscription = true;
            ServicePackage servicePackage = subscription.getServicePackage();

            for (Service service : servicePackage.getServices()) {
                if (service.getType() == cdr.getServiceType()) {
                    matchedSubscription = subscription;
                    matchedService = service;
                    ratedCDR.addRatingNote("Rated using service package: " + servicePackage.getName());
                    break;
                }
            }
            if (matchedService != null) break;
        }

        long usage = cdr.getUsage();
        BigDecimal ratedAmount = BigDecimal.ZERO;
        int freeUnitsUsed = 0;
        int paidUnits = 0;

        if (matchedSubscription != null && matchedService != null) {
            int remainingFreeUnits = matchedSubscription.getRemainingFreeUnits();

            if (usage <= remainingFreeUnits) {
                freeUnitsUsed = (int) usage;
            } else {
                freeUnitsUsed = remainingFreeUnits;
                paidUnits = (int) (usage - remainingFreeUnits);

                BigDecimal unitPrice = matchedService.getUnitPrice();
                BigDecimal timeMultiplier = matchedService.getRateMultiplierForTime(cdr.getStartTime().toLocalTime());
                BigDecimal destMultiplier = matchedService.getRateMultiplierForDestination(cdr.getDialB());

                BigDecimal rate = unitPrice.multiply(timeMultiplier).multiply(destMultiplier);
                ratedAmount = rate.multiply(BigDecimal.valueOf(paidUnits));
            }
        } else {
            if (!hasActiveSubscription) {
                ratedCDR.addRatingNote("No active subscription found");
            } else {
                ratedCDR.addRatingNote("No matching service found in the package");
            }
        }

        BigDecimal externalCharges = BigDecimal.valueOf(cdr.getExternalCharges());
        ratedAmount = ratedAmount.add(externalCharges);
        ratedAmount = ratedAmount.setScale(2, RoundingMode.HALF_UP);

        ratedCDR.setRatedAmount(ratedAmount);
        ratedCDR.setFreeUnitsUsed(freeUnitsUsed);
        ratedCDR.setPaidUnits(paidUnits);

        return ratedCDR;
    }

    @Override
    public void updateFreeUnits(Customer customer, RatedCDR ratedCDR) {
        for (ServiceSubscription subscription : customer.getSubscriptions()) {
            if (subscription.getActive()) {
                int remaining = subscription.getRemainingFreeUnits();
                int used = ratedCDR.getFreeUnitsUsed();
                int updated = Math.max(remaining - used, 0);
                subscription.setRemainingFreeUnits(updated);
            }
        }
    }
}
