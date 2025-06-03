package com.localbrand.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CustomerInvoice {
    private String customerMsisdn;
    private String serviceType;
    private int totalVolume;
    private BigDecimal totalCharges;
    private Date invoiceDate;
} 