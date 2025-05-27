package com.localbrand.service;

import com.localbrand.dto.RatedCDR;
import com.localbrand.model.CDR;
import com.localbrand.model.Customer;

public interface RatingEngine {
    RatedCDR rateCDR(CDR cdr, Customer customer);
    void updateFreeUnits(Customer customer, RatedCDR ratedCDR);
}