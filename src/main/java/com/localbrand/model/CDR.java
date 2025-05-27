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
} 