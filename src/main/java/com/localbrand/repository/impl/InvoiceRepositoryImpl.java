package com.localbrand.repository.impl;

import com.localbrand.model.Invoice;
import com.localbrand.repository.InvoiceRepository;
import com.localbrand.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRepositoryImpl implements InvoiceRepository {
    private Connection connection;

    public InvoiceRepositoryImpl() {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public List<Invoice> findByCustomerPhone(String phoneNumber) {
        List<Invoice> invoices = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM invoices WHERE customer_phone = ?");
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    @Override
    public Invoice findById(Long id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM invoices WHERE id = ?");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToInvoice(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Invoice save(Invoice invoice) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE invoices SET status = ? WHERE id = ?"
            );
            pstmt.setString(1, invoice.getStatus());
            pstmt.setLong(2, invoice.getId());
            
            pstmt.executeUpdate();
            return invoice;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("id"));
        invoice.setInvoiceNumber(rs.getString("invoice_number"));
        invoice.setCustomerPhone(rs.getString("customer_phone"));
        invoice.setIssueDate(rs.getTimestamp("issue_date").toLocalDateTime());
        invoice.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
        invoice.setTotal(rs.getBigDecimal("total"));
        invoice.setStatus(rs.getString("status"));
        invoice.setPdfPath(rs.getString("pdf_path"));
        return invoice;
    }
} 