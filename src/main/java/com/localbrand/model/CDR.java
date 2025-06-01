package com.localbrand.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cdrs")
public class CDR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dial_a", nullable = false)
    private String dialA; // Source number

    @Column(name = "dial_b")
    private String dialB; // Destination number or URL for data

    @Column(name = "service_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(nullable = false)
    private Long usage; // Duration in seconds, number of messages, or data volume in bytes

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "external_charges", nullable = false)
    private Double externalCharges; // In piasters

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
} 