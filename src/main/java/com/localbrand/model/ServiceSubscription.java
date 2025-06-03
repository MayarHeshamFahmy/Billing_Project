package com.localbrand.model;

import java.time.LocalDateTime;

public class ServiceSubscription {
    private Long id;
    private Long customerId;
    private Long servicePackageId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private Integer remainingFreeUnits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getRemainingFreeUnits() {
        return remainingFreeUnits;
    }

    public void setRemainingFreeUnits(Integer remainingFreeUnits) {
        this.remainingFreeUnits = remainingFreeUnits;
    }
} 