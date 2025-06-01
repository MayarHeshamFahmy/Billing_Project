package com.localbrand.service.impl;

import com.localbrand.dto.RatedCDR;
import com.localbrand.model.CDR;
import com.localbrand.model.Customer;
import com.localbrand.service.RatingEngine;

public class RatingEngineImpl implements RatingEngine {
    @Override
    public RatedCDR rateCDR(CDR cdr, Customer customer) {
        // TODO: Implement rating logic
        return new RatedCDR();
    }

    @Override
    public void updateFreeUnits(Customer customer, RatedCDR ratedCDR) {
        // TODO: Implement free units update logic
    }
} 