package com.localbrand.repository.impl;

import com.localbrand.model.ServicePackage;
import com.localbrand.repository.ServicePackageRepository;
import com.localbrand.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePackageRepositoryImpl implements ServicePackageRepository {
    private Connection connection;

    public ServicePackageRepositoryImpl() {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public List<ServicePackage> findAll() {
        List<ServicePackage> packages = new ArrayList<>();
        try {
            System.out.println("Attempting to fetch service packages from database...");
            
            // First, check if the table exists
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "service_packages", null);
            if (!tables.next()) {
                System.err.println("Table 'service_packages' does not exist!");
                return packages;
            }
            
            // Get table structure
            ResultSet columns = metaData.getColumns(null, null, "service_packages", null);
            System.out.println("Table structure:");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                System.out.println("Column: " + columnName + ", Type: " + columnType);
            }
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM service_packages");
            while (rs.next()) {
                ServicePackage pkg = mapResultSetToServicePackage(rs);
                packages.add(pkg);
                System.out.println("Found service package: " + pkg.getName() + " with ID: " + pkg.getId());
            }
            System.out.println("Total service packages found: " + packages.size());
        } catch (SQLException e) {
            System.err.println("Error fetching service packages: " + e.getMessage());
            e.printStackTrace();
        }
        return packages;
    }

    @Override
    public ServicePackage findById(Long id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM service_packages WHERE id = ?");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToServicePackage(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching service package by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private ServicePackage mapResultSetToServicePackage(ResultSet rs) throws SQLException {
        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setId(rs.getLong("id"));
        servicePackage.setName(rs.getString("name"));
        servicePackage.setDescription(rs.getString("description"));
        servicePackage.setPrice(rs.getBigDecimal("price"));
        servicePackage.setRecurring(rs.getBoolean("is_recurring"));
        servicePackage.setFreeUnits(rs.getInt("free_units"));
        return servicePackage;
    }
} 