package com.localbrand.dto;

import com.localbrand.model.ServiceType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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
} 