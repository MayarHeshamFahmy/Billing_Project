package com.localbrand.service.impl;

import com.localbrand.model.ServicePackage;
import com.localbrand.repository.ServicePackageRepository;
import com.localbrand.repository.impl.ServicePackageRepositoryImpl;
import com.localbrand.service.ServicePackageService;
import java.util.List;

public class ServicePackageServiceImpl implements ServicePackageService {
    private ServicePackageRepository servicePackageRepository;

    public ServicePackageServiceImpl() {
        this.servicePackageRepository = new ServicePackageRepositoryImpl();
    }

    @Override
    public List<ServicePackage> getAllServicePackages() {
        return servicePackageRepository.findAll();
    }

    @Override
    public ServicePackage getServicePackageById(Long id) {
        return servicePackageRepository.findById(id);
    }
} 