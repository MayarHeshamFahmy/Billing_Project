package com.localbrand.repository;

import com.localbrand.model.RatePlan;
import java.util.List;

public interface RatePlanRepository {
    List<RatePlan> findAll();
    RatePlan save(RatePlan ratePlan);
    RatePlan findById(Long id);
} 