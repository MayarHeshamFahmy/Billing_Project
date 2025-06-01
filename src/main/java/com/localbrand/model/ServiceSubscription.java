package com.localbrand.model;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceSubscription {
    private Long id;
    private String customerPhone;
    private Long servicePackageId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private int remainingFreeUnits;
    private ServicePackage servicePackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Long getServicePackageId() {
        return servicePackageId;
    }

    public void setServicePackageId(Long servicePackageId) {
        this.servicePackageId = servicePackageId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRemainingFreeUnits() {
        return remainingFreeUnits;
    }

    public void setRemainingFreeUnits(int remainingFreeUnits) {
        this.remainingFreeUnits = remainingFreeUnits;
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }
} 