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
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM service_packages");
            while (rs.next()) {
                packages.add(mapResultSetToServicePackage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    private ServicePackage mapResultSetToServicePackage(ResultSet rs) throws SQLException {
        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setId(rs.getLong("id"));
        servicePackage.setName(rs.getString("name"));
        servicePackage.setDescription(rs.getString("description"));
        servicePackage.setPrice(rs.getBigDecimal("price"));
        return servicePackage;
    }
} 