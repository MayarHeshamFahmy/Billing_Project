package com.localbrand.repository;

import com.localbrand.model.ServicePackage;
import java.util.List;

public interface ServicePackageRepository {
    List<ServicePackage> findAll();
} 