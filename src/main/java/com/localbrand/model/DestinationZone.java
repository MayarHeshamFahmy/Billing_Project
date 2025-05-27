package com.localbrand.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "destination_zones")
public class DestinationZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String pattern; // Regex pattern to match dial numbers

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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
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
} 