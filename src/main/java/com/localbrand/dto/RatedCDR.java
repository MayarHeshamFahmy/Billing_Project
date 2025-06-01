package com.localbrand.dto;

import com.localbrand.model.ServiceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RatedCDR {
    private String dialA;
    private String dialB;
    private ServiceType serviceType;
    private Long usage;
    private LocalDateTime startTime;
    private Double externalCharges;
    private BigDecimal ratedAmount;
    private Integer freeUnitsUsed;
    private Integer paidUnits;
    private BigDecimal unitPrice;
    private String ratingNotes;

    // Added getters and setters based on test and RatingEngineImpl usage

    public BigDecimal getRatedAmount() {
        return ratedAmount;
    }

    public void setRatedAmount(BigDecimal ratedAmount) {
        this.ratedAmount = ratedAmount;
    }

    public Integer getFreeUnitsUsed() {
        return freeUnitsUsed;
    }

    public void setFreeUnitsUsed(Integer freeUnitsUsed) {
        this.freeUnitsUsed = freeUnitsUsed;
    }

    public Integer getPaidUnits() {
        return paidUnits;
    }

    public void setPaidUnits(Integer paidUnits) {
        this.paidUnits = paidUnits;
    }

    public String getRatingNotes() {
        return ratingNotes;
    }

    public void setRatingNotes(String ratingNotes) {
        this.ratingNotes = ratingNotes;
    }

    public String getDialA() {
        return dialA;
    }

    public void setDialA(String dialA) {
        this.dialA = dialA;
    }

    public String getDialB() {
        return dialB;
    }

    public void setDialB(String dialB) {
        this.dialB = dialB;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Long getUsage() {
        return usage;
    }

    public void setUsage(Long usage) {
        this.usage = usage;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Double getExternalCharges() {
        return externalCharges;
    }

    public void setExternalCharges(Double externalCharges) {
        this.externalCharges = externalCharges;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void addRatingNote(String note) {
        if (ratingNotes == null) {
            ratingNotes = note;
        } else {
            ratingNotes += "\n" + note;
        }
    }
} 