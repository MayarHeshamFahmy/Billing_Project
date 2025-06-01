package com.localbrand.service.impl;

import com.localbrand.model.RatePlan;
import com.localbrand.repository.RatePlanRepository; // Updated import
import com.localbrand.repository.impl.RatePlanRepositoryImpl; // Updated import
import com.localbrand.service.RatePlanService;
import java.util.List;

public class RatePlanServiceImpl implements RatePlanService {
    private RatePlanRepository ratePlanRepository; // Renamed repository field

    public RatePlanServiceImpl() {
        this.ratePlanRepository = new RatePlanRepositoryImpl(); // Instantiating the new repository
    }

    @Override
    public List<RatePlan> getAllRatePlans() {
        return ratePlanRepository.findAll();
    }

    @Override
    public RatePlan createRatePlan(RatePlan ratePlan) {
        return ratePlanRepository.save(ratePlan);
    }
    
    @Override
    public RatePlan getRatePlanById(Long id) {
        return ratePlanRepository.findById(id);
    }
} 