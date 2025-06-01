package com.localbrand.service;

import com.localbrand.model.RatePlan;
import java.util.List;

public interface RatePlanService {
    List<RatePlan> getAllRatePlans();
    RatePlan createRatePlan(RatePlan ratePlan);
    RatePlan getRatePlanById(Long id);
} 