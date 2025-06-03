package com.localbrand.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class InvoiceViewDTO {
    private String customerMsisdn;
    private Date invoiceDate;
    private List<ServiceBreakdown> breakdown;
    private BigDecimal totalCharges;

    public String getCustomerMsisdn() { return customerMsisdn; }
    public void setCustomerMsisdn(String customerMsisdn) { this.customerMsisdn = customerMsisdn; }

    public Date getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(Date invoiceDate) { this.invoiceDate = invoiceDate; }

    public List<ServiceBreakdown> getBreakdown() { return breakdown; }
    public void setBreakdown(List<ServiceBreakdown> breakdown) { this.breakdown = breakdown; }

    public BigDecimal getTotalCharges() { return totalCharges; }
    public void setTotalCharges(BigDecimal totalCharges) { this.totalCharges = totalCharges; }

    public static class ServiceBreakdown {
        private String serviceType;
        private int totalVolume;
        private BigDecimal totalCharges;

        public String getServiceType() { return serviceType; }
        public void setServiceType(String serviceType) { this.serviceType = serviceType; }

        public int getTotalVolume() { return totalVolume; }
        public void setTotalVolume(int totalVolume) { this.totalVolume = totalVolume; }

        public BigDecimal getTotalCharges() { return totalCharges; }
        public void setTotalCharges(BigDecimal totalCharges) { this.totalCharges = totalCharges; }
    }
} 