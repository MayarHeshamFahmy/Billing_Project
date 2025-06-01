package com.localbrand.service.impl;

import com.localbrand.dto.RatedCDR;
import com.localbrand.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingEngineImplTest {

    @InjectMocks
    private RatingEngineImpl ratingEngine;

    @Mock private CDR cdr;
    @Mock private Customer customer;
    @Mock private ServiceSubscription subscription;
    @Mock private ServicePackage servicePackage;
    @Mock private Service voiceService;

    @BeforeEach
    void setUp() {
        // No common stubs needed
    }

    private void setupBasicCDR() {
        when(cdr.getDialA()).thenReturn("1234567890");
        when(cdr.getDialB()).thenReturn("0987654321");
        when(cdr.getStartTime()).thenReturn(LocalDateTime.now());
    }

    @Test
    void testRateCDR_usageWithinFreeUnits() {
        setupBasicCDR();
        
        // Setup service and package
        when(voiceService.getType()).thenReturn(ServiceType.VOICE);

        when(subscription.getActive()).thenReturn(true);
        when(subscription.getServicePackage()).thenReturn(servicePackage);
        when(subscription.getRemainingFreeUnits()).thenReturn(100);

        when(servicePackage.getName()).thenReturn("Basic Package");
        when(servicePackage.getServices()).thenReturn(Arrays.asList(voiceService));

        // Setup customer and CDR
        when(customer.getSubscriptions()).thenReturn(Arrays.asList(subscription));
        when(cdr.getServiceType()).thenReturn(ServiceType.VOICE);
        when(cdr.getUsage()).thenReturn(50L);
        when(cdr.getExternalCharges()).thenReturn(0.0);

        RatedCDR actualRatedCDR = ratingEngine.rateCDR(cdr, customer);

        assertNotNull(actualRatedCDR);
        assertEquals(new BigDecimal("0.00").setScale(2, RoundingMode.HALF_UP), actualRatedCDR.getRatedAmount());
        assertEquals(50, actualRatedCDR.getFreeUnitsUsed());
        assertEquals(0, actualRatedCDR.getPaidUnits());
        assertTrue(actualRatedCDR.getRatingNotes().contains("Rated using service package: Basic Package"));
    }

    @Test
    void testRateCDR_usageExceedingFreeUnits() {
        setupBasicCDR();
        
        // Setup service and package
        when(voiceService.getType()).thenReturn(ServiceType.VOICE);
        when(voiceService.getUnitPrice()).thenReturn(new BigDecimal("0.10"));
        when(voiceService.getRateMultiplierForTime(any(LocalTime.class))).thenReturn(BigDecimal.ONE);
        when(voiceService.getRateMultiplierForDestination(anyString())).thenReturn(BigDecimal.ONE);

        when(subscription.getActive()).thenReturn(true);
        when(subscription.getServicePackage()).thenReturn(servicePackage);
        when(subscription.getRemainingFreeUnits()).thenReturn(100);

        when(servicePackage.getServices()).thenReturn(Arrays.asList(voiceService));

        // Setup customer and CDR
        when(customer.getSubscriptions()).thenReturn(Arrays.asList(subscription));
        when(cdr.getServiceType()).thenReturn(ServiceType.VOICE);
        when(cdr.getUsage()).thenReturn(120L);
        when(cdr.getExternalCharges()).thenReturn(0.0);

        RatedCDR actualRatedCDR = ratingEngine.rateCDR(cdr, customer);

        assertNotNull(actualRatedCDR);
        assertEquals(new BigDecimal("2.00").setScale(2, RoundingMode.HALF_UP), actualRatedCDR.getRatedAmount());
        assertEquals(100, actualRatedCDR.getFreeUnitsUsed());
        assertEquals(20, actualRatedCDR.getPaidUnits());
    }

    @Test
    void testRateCDR_withExternalCharges() {
        setupBasicCDR();
        
        // Setup service and package
        when(voiceService.getType()).thenReturn(ServiceType.VOICE);

        when(subscription.getActive()).thenReturn(true);
        when(subscription.getServicePackage()).thenReturn(servicePackage);
        when(subscription.getRemainingFreeUnits()).thenReturn(100);

        when(servicePackage.getServices()).thenReturn(Arrays.asList(voiceService));

        // Setup customer and CDR
        when(customer.getSubscriptions()).thenReturn(Arrays.asList(subscription));
        when(cdr.getServiceType()).thenReturn(ServiceType.VOICE);
        when(cdr.getUsage()).thenReturn(30L);
        when(cdr.getExternalCharges()).thenReturn(0.50);

        RatedCDR actualRatedCDR = ratingEngine.rateCDR(cdr, customer);

        assertNotNull(actualRatedCDR);
        assertEquals(new BigDecimal("0.50").setScale(2, RoundingMode.HALF_UP), actualRatedCDR.getRatedAmount());
        assertEquals(30, actualRatedCDR.getFreeUnitsUsed());
        assertEquals(0, actualRatedCDR.getPaidUnits());
    }

    @Test
    void testUpdateFreeUnits() {
        when(customer.getSubscriptions()).thenReturn(Arrays.asList(subscription));
        when(subscription.getActive()).thenReturn(true);
        when(subscription.getRemainingFreeUnits()).thenReturn(100);

        RatedCDR ratedCDR = new RatedCDR();
        ratedCDR.setFreeUnitsUsed(40);

        ratingEngine.updateFreeUnits(customer, ratedCDR);
        verify(subscription).setRemainingFreeUnits(60);

        when(subscription.getRemainingFreeUnits()).thenReturn(30);
        ratedCDR.setFreeUnitsUsed(40);
        ratingEngine.updateFreeUnits(customer, ratedCDR);
        verify(subscription).setRemainingFreeUnits(0);
    }

    @Test
    void testRateCDR_noActiveSubscription() {
        when(customer.getSubscriptions()).thenReturn(Arrays.asList());
        when(cdr.getExternalCharges()).thenReturn(1.20);
        when(cdr.getServiceType()).thenReturn(ServiceType.SMS);
        when(cdr.getUsage()).thenReturn(1L);

        RatedCDR actualRatedCDR = ratingEngine.rateCDR(cdr, customer);

        assertNotNull(actualRatedCDR);
        assertEquals(new BigDecimal("1.20").setScale(2, RoundingMode.HALF_UP), actualRatedCDR.getRatedAmount());
        assertTrue(actualRatedCDR.getRatingNotes().contains("No active subscription found"));
    }

    @Test
    void testRateCDR_noMatchingServiceInPackage() {
        // Setup subscription and package
        when(subscription.getActive()).thenReturn(true);
        when(subscription.getServicePackage()).thenReturn(servicePackage);
        when(servicePackage.getServices()).thenReturn(Arrays.asList());

        // Setup customer and CDR
        when(customer.getSubscriptions()).thenReturn(Arrays.asList(subscription));
        when(cdr.getServiceType()).thenReturn(ServiceType.VOICE);
        when(cdr.getExternalCharges()).thenReturn(0.75);

        RatedCDR actualRatedCDR = ratingEngine.rateCDR(cdr, customer);

        assertNotNull(actualRatedCDR);
        assertEquals(new BigDecimal("0.75").setScale(2, RoundingMode.HALF_UP), actualRatedCDR.getRatedAmount());
        assertTrue(actualRatedCDR.getRatingNotes().contains("No matching service found in the package"));
    }
}
