package com.localbrand.service;

import com.localbrand.dto.InvoiceViewDTO;
import com.localbrand.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

public class InvoiceViewService {
    public InvoiceViewDTO getInvoiceForCustomer(String msisdn, Date invoiceDate) {
        InvoiceViewDTO dto = new InvoiceViewDTO();
        dto.setCustomerMsisdn(msisdn);
        dto.setInvoiceDate(invoiceDate);

        // 1. Get breakdown
        String breakdownQuery = "SELECT service_type, total_volume, total_charges FROM customer_invoice WHERE customer_msisdn = ? AND invoice_date = ?";
        List<InvoiceViewDTO.ServiceBreakdown> breakdownList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getRatedCdrsConnection();
             PreparedStatement stmt = conn.prepareStatement(breakdownQuery)) {
            stmt.setString(1, msisdn);
            stmt.setDate(2, new java.sql.Date(invoiceDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                InvoiceViewDTO.ServiceBreakdown breakdown = new InvoiceViewDTO.ServiceBreakdown();
                breakdown.setServiceType(rs.getString("service_type"));
                breakdown.setTotalVolume(rs.getInt("total_volume"));
                breakdown.setTotalCharges(rs.getBigDecimal("total_charges"));
                breakdownList.add(breakdown);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dto.setBreakdown(breakdownList);

        // 2. Get total charges
        String totalQuery = "SELECT total_charges FROM invoice WHERE customer_msisdn = ? AND invoice_date = ?";
        try (Connection conn = DatabaseConnection.getRatedCdrsConnection();
             PreparedStatement stmt = conn.prepareStatement(totalQuery)) {
            stmt.setString(1, msisdn);
            stmt.setDate(2, new java.sql.Date(invoiceDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dto.setTotalCharges(rs.getBigDecimal("total_charges"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dto;
    }
} 