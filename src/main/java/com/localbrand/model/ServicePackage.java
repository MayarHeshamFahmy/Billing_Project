package com.localbrand.model;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "service_packages")
public class ServicePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring;

    @Column(name = "free_units")
    private Integer freeUnits;

    @OneToMany(mappedBy = "servicePackage")
    private List<Service> services;

    @OneToMany(mappedBy = "servicePackage")
    private List<ServiceSubscription> subscriptions;
} 