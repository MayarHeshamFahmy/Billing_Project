package com.localbrand.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType type;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "unit_type", nullable = false)
    private String unitType;

    @ManyToOne
    @JoinColumn(name = "service_package_id")
    private ServicePackage servicePackage;

    @ManyToMany
    @JoinTable(
        name = "service_time_zones",
        joinColumns = @JoinColumn(name = "service_id"),
        inverseJoinColumns = @JoinColumn(name = "time_zone_id")
    )
    private List<TimeZone> timeZones = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "service_destination_zones",
        joinColumns = @JoinColumn(name = "service_id"),
        inverseJoinColumns = @JoinColumn(name = "destination_zone_id")
    )
    private List<DestinationZone> destinationZones = new ArrayList<>();

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ServiceType getType() {
        return type;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getUnitType() {
        return unitType;
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public List<TimeZone> getTimeZones() {
        return timeZones;
    }

    public List<DestinationZone> getDestinationZones() {
        return destinationZones;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }

    public void setTimeZones(List<TimeZone> timeZones) {
        this.timeZones = timeZones;
    }

    public void setDestinationZones(List<DestinationZone> destinationZones) {
        this.destinationZones = destinationZones;
    }

    // Business methods
    public BigDecimal getRateMultiplierForTime(LocalTime time) {
        return timeZones.stream()
                .filter(tz -> tz.isInTimeZone(time))
                .findFirst()
                .map(TimeZone::getRateMultiplier)
                .orElse(BigDecimal.ONE);
    }

    public BigDecimal getRateMultiplierForDestination(String dialNumber) {
        return destinationZones.stream()
                .filter(dz -> dialNumber.matches(dz.getPattern()))
                .findFirst()
                .map(DestinationZone::getRateMultiplier)
                .orElse(BigDecimal.ONE);
    }
} 