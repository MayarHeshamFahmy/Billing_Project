package com.localbrand.repository.impl;

import com.localbrand.model.RatePlan;
import com.localbrand.repository.RatePlanRepository;
import com.localbrand.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class RatePlanRepositoryImpl implements RatePlanRepository {
    private Connection connection;

    public RatePlanRepositoryImpl() {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public List<RatePlan> findAll() {
        List<RatePlan> ratePlans = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM rate_plans");
            while (rs.next()) {
                ratePlans.add(mapResultSetToRatePlan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratePlans;
    }

    @Override
    public RatePlan findById(Long id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM rate_plans WHERE id = ?");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToRatePlan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RatePlan save(RatePlan ratePlan) {
        String sql;
        boolean isNew = ratePlan.getId() == null;

        if (isNew) {
            sql = "INSERT INTO rate_plans (name, description, base_price) VALUES (?, ?, ?)";
        } else {
            sql = "UPDATE rate_plans SET name = ?, description = ?, base_price = ? WHERE id = ?";
        }

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, ratePlan.getName());
            pstmt.setString(2, ratePlan.getDescription());
            pstmt.setBigDecimal(3, ratePlan.getBasePrice());
            
            if (!isNew) {
                pstmt.setLong(4, ratePlan.getId());
            }
            
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                ratePlan.setId(rs.getLong(1));
            }
            return ratePlan;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private RatePlan mapResultSetToRatePlan(ResultSet rs) throws SQLException {
        RatePlan ratePlan = new RatePlan();
        ratePlan.setId(rs.getLong("id"));
        ratePlan.setName(rs.getString("name"));
        ratePlan.setDescription(rs.getString("description"));
        ratePlan.setBasePrice(rs.getBigDecimal("base_price"));
        return ratePlan;
    }
} 