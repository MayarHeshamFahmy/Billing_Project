package com.localbrand.service.impl;

import com.localbrand.dto.RatedCDR;
import com.localbrand.model.CDR;
import com.localbrand.model.Customer;
import com.localbrand.model.Service;
import com.localbrand.model.ServicePackage;
import com.localbrand.model.ServiceSubscription;
import com.localbrand.model.ServiceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ManualRatingTest {

    public static void main(String[] args) {
        // Create sample Service, ServicePackage, ServiceSubscription, Customer, and CDR
        Service voiceService = new Service();
        voiceService.setType(ServiceType.VOICE);
        voiceService.setUnitPrice(new BigDecimal("0.10"));
        voiceService.setName("Voice Call");

        List<Service> servicesList = Arrays.asList(voiceService);

        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setName("Basic Package");
        servicePackage.setServices(servicesList);

        ServiceSubscription subscription = new ServiceSubscription();
        subscription.setActive(true);
        subscription.setRemainingFreeUnits(100);
        subscription.setServicePackage(servicePackage);

        Customer customer = new Customer();
        customer.setSubscriptions(Arrays.asList(subscription));

        CDR cdr = new CDR();
        cdr.setDialA("1234567890");
        cdr.setDialB("0987654321");
        cdr.setServiceType(ServiceType.VOICE);
        cdr.setUsage(50L); // Usage less than free units
        cdr.setStartTime(LocalDateTime.now());
        cdr.setExternalCharges(0.0);

        // Instantiate and use the RatingEngineImpl
        RatingEngineImpl ratingEngine = new RatingEngineImpl();
        RatedCDR ratedCDR = ratingEngine.rateCDR(cdr, customer);

        // Print the output
        System.out.println("--- Rated CDR Output ---");
        System.out.println("Dial A: " + ratedCDR.getDialA());
        System.out.println("Dial B: " + ratedCDR.getDialB());
        System.out.println("Service Type: " + ratedCDR.getServiceType());
        System.out.println("Usage: " + ratedCDR.getUsage());
        System.out.println("Start Time: " + ratedCDR.getStartTime());
        System.out.println("External Charges: " + ratedCDR.getExternalCharges());
        System.out.println("Rated Amount: " + ratedCDR.getRatedAmount());
        System.out.println("Free Units Used: " + ratedCDR.getFreeUnitsUsed());
        System.out.println("Paid Units: " + ratedCDR.getPaidUnits());
        System.out.println("Unit Price: " + ratedCDR.getUnitPrice());
        System.out.println("Rating Notes: " + ratedCDR.getRatingNotes());
        System.out.println("-----------------------");
    }
} 