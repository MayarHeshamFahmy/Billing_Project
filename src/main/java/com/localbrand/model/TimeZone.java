package com.localbrand.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "time_zones")
public class TimeZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private BigDecimal rateMultiplier; // Multiplier for the base rate

    @Column
    private String description;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getRateMultiplier() {
        return rateMultiplier;
    }

    public void setRateMultiplier(BigDecimal rateMultiplier) {
        this.rateMultiplier = rateMultiplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInTimeZone(LocalTime time) {
        if (startTime.isBefore(endTime)) {
            return !time.isBefore(startTime) && !time.isAfter(endTime);
        } else {
            // Handles overnight time zones (e.g., 22:00-06:00)
            return !time.isBefore(startTime) || !time.isAfter(endTime);
        }
    }
} 