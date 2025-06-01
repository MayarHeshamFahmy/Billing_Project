package com.localbrand.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "rate_plan_id")
    private RatePlan ratePlan;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<ServiceSubscription> subscriptions;

    @OneToMany(mappedBy = "customer")
    private List<CDR> cdrs;

    @OneToMany(mappedBy = "customer")
    private List<Invoice> invoices;

    @OneToOne(mappedBy = "customer")
    private Profile profile;

    public String getName() {
        return name;
    }

    public List<ServiceSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RatePlan getRatePlan() {
        return ratePlan;
    }

    public void setRatePlan(RatePlan ratePlan) {
        this.ratePlan = ratePlan;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setSubscriptions(List<ServiceSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<CDR> getCdrs() {
        return cdrs;
    }

    public void setCdrs(List<CDR> cdrs) {
        this.cdrs = cdrs;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
} 