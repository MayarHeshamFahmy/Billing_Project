package com.localbrand.repository.impl;

import com.localbrand.model.ServiceSubscription;
import com.localbrand.repository.ServiceSubscriptionRepository;
import com.localbrand.util.DatabaseUtil;

import java.sql.*;

public class ServiceSubscriptionRepositoryImpl implements ServiceSubscriptionRepository {
    private Connection connection;

    public ServiceSubscriptionRepositoryImpl() {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public ServiceSubscription save(ServiceSubscription subscription) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO service_subscriptions (customer_id, service_package_id, start_date, end_date, active, remaining_free_units) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            pstmt.setLong(1, subscription.getCustomerId());
            pstmt.setLong(2, subscription.getServicePackageId());
            pstmt.setTimestamp(3, Timestamp.valueOf(subscription.getStartDate()));
            if (subscription.getEndDate() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(subscription.getEndDate()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }
            pstmt.setBoolean(5, subscription.isActive());
            pstmt.setInt(6, subscription.getRemainingFreeUnits());
            
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                subscription.setId(rs.getLong(1));
            }
            return subscription;
        } catch (SQLException e) {
            System.err.println("Error saving service subscription: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
} 