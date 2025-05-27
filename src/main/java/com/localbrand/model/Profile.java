package com.localbrand.model;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "profile")
    private List<ServicePackage> servicePackages;

    @OneToMany(mappedBy = "profile")
    private List<Customer> customers;
} 