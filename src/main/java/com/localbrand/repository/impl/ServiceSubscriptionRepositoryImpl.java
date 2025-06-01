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
                "INSERT INTO service_subscriptions (customer_phone, service_package_id, start_date, end_date) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            pstmt.setString(1, subscription.getCustomerPhone());
            pstmt.setLong(2, subscription.getServicePackageId());
            pstmt.setTimestamp(3, Timestamp.valueOf(subscription.getStartDate()));
            if (subscription.getEndDate() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(subscription.getEndDate()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }
            
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                subscription.setId(rs.getLong(1));
            }
            return subscription;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
} 