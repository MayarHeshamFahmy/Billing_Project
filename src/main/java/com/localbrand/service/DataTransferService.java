package com.localbrand.service;

import com.localbrand.model.CustomerInvoice;
import com.localbrand.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataTransferService {
    private static final Logger LOGGER = Logger.getLogger(DataTransferService.class.getName());

    public static List<CustomerInvoice> getCustomerInvoices(String customerMsisdn) {
        List<CustomerInvoice> invoices = new ArrayList<>();
        String query = "SELECT " +
                      "    ci.dial_a, " +
                      "    ci.service_type, " +
                      "    ci.total_volume, " +
                      "    ci.total_charges, " +
                      "    ci.invoice_date " +
                      "FROM customer_invoice ci " +
                      "WHERE ci.dial_a = ? " +
                      "ORDER BY ci.invoice_date DESC";

        try (Connection conn = DatabaseConnection.getRatedCdrsConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, customerMsisdn);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CustomerInvoice invoice = new CustomerInvoice();
                invoice.setCustomerMsisdn(rs.getString("dial_a"));
                invoice.setServiceType(rs.getString("service_type"));
                invoice.setTotalVolume(rs.getInt("total_volume"));
                invoice.setTotalCharges(rs.getBigDecimal("total_charges"));
                invoice.setInvoiceDate(rs.getDate("invoice_date"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving customer invoices: " + e.getMessage(), e);
        }
        return invoices;
    }

    public static void transferDataToCustomerInvoice() {
        String selectQuery = "SELECT " +
                           "    dial_a, " +
                           "    service_type, " +
                           "    SUM(volume) as total_volume, " +
                           "    SUM(total) as total_charges, " +
                           "    CURRENT_DATE as invoice_date " +
                           "FROM rated_cdrs " +
                           "GROUP BY dial_a, service_type";

        String insertQuery = "INSERT INTO customer_invoice " +
                           "(dial_a, service_type, total_volume, total_charges, invoice_date) " +
                           "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null; // Use a single connection variable
        try {
            conn = DatabaseConnection.getRatedCdrsConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                 PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) { // Use conn for insertStmt

                ResultSet rs = selectStmt.executeQuery();
                int count = 0;

                while (rs.next()) {
                    insertStmt.setString(1, rs.getString("dial_a"));
                    insertStmt.setString(2, rs.getString("service_type"));
                    insertStmt.setInt(3, rs.getInt("total_volume"));
                    insertStmt.setBigDecimal(4, rs.getBigDecimal("total_charges"));
                    insertStmt.setDate(5, rs.getDate("invoice_date"));

                    insertStmt.addBatch(); // Use batching for efficiency
                    count++;
                }
                
                insertStmt.executeBatch(); // Execute the batch
                conn.commit();
                LOGGER.info("Customer invoice data transfer completed! " + count + " records inserted.");
            } catch (SQLException e) {
                if (conn != null) {
                    conn.rollback();
                }
                LOGGER.log(Level.SEVERE, "Error during customer_invoice transfer: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error: " + e.getMessage(), e);
        } finally {
            closeConnection(conn); // Close the single connection
        }
    }

    public static void transferDataToInvoice() {
        String selectQuery = "SELECT " +
                           "    dial_a, " +
                           "    SUM(total) as total_charges, " +
                           "    CURRENT_DATE as invoice_date " +
                           "FROM rated_cdrs " +
                           "GROUP BY dial_a";

        String insertQuery = "INSERT INTO invoice " +
                           "(id, dial_a, total_charges, invoice_date) " +
                           "VALUES (NULL, ?, ?, ?)";

        Connection conn = null; // Use a single connection variable
        try {
            conn = DatabaseConnection.getRatedCdrsConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                 PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) { // Use conn for insertStmt

                ResultSet rs = selectStmt.executeQuery();
                int count = 0;

                while (rs.next()) {
                    insertStmt.setString(1, rs.getString("dial_a"));
                    insertStmt.setBigDecimal(2, rs.getBigDecimal("total_charges"));
                    insertStmt.setDate(3, rs.getDate("invoice_date"));

                    insertStmt.addBatch(); // Use batching for efficiency
                    count++;
                }
                
                insertStmt.executeBatch(); // Execute the batch
                conn.commit();
                LOGGER.info("Invoice data transfer completed! " + count + " records inserted.");
            } catch (SQLException e) {
                if (conn != null) {
                    conn.rollback();
                }
                LOGGER.log(Level.SEVERE, "Error during invoice transfer: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error: " + e.getMessage(), e);
        } finally {
            closeConnection(conn); // Close the single connection
        }
    }

    // Modified closeConnections to close a single connection
    private static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                 if (!conn.getAutoCommit()) {
                    conn.setAutoCommit(true);
                }
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection: " + e.getMessage(), e);
            }
        }
    }
} 