package com.localbrand.repository.impl;

import com.localbrand.model.Customer;
import com.localbrand.model.RatePlan;
import com.localbrand.repository.CustomerRepository;
import com.localbrand.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {
    private Connection connection;

    public CustomerRepositoryImpl() {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT c.*, rp.name as rate_plan_name, rp.description as rate_plan_description, rp.base_price as rate_plan_base_price FROM customers c LEFT JOIN rate_plans rp ON c.rate_plan_id = rp.id");
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public Customer findByPhoneNumber(String phoneNumber) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT c.*, rp.name as rate_plan_name, rp.description as rate_plan_description, rp.base_price as rate_plan_base_price FROM customers c LEFT JOIN rate_plans rp ON c.rate_plan_id = rp.id WHERE c.phone_number = ?");
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            } else {
                return null; // Customer not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Customer save(Customer customer) {
        String sql;
        boolean isNew = customer.getId() == null;

        if (isNew) {
            sql = "INSERT INTO customers (name, phone_number, email, address, rate_plan_id) VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE customers SET name = ?, phone_number = ?, email = ?, address = ?, rate_plan_id = ? WHERE id = ?";
        }

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getPhoneNumber());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getAddress());
            if (customer.getRatePlan() != null) {
                pstmt.setLong(5, customer.getRatePlan().getId());
            } else {
                pstmt.setNull(5, java.sql.Types.BIGINT);
            }

            if (!isNew) {
                pstmt.setLong(6, customer.getId());
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0 && isNew) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setId(generatedKeys.getLong(1));
                    }
                }
            }
            return customer;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setName(rs.getString("name"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));

        // Map Rate Plan data
        Long ratePlanId = rs.getLong("rate_plan_id");
        if (!rs.wasNull()) { // Check if rate_plan_id is not NULL
            RatePlan ratePlan = new RatePlan();
            ratePlan.setId(ratePlanId);
            ratePlan.setName(rs.getString("rate_plan_name"));
            ratePlan.setDescription(rs.getString("rate_plan_description"));
            ratePlan.setBasePrice(rs.getBigDecimal("rate_plan_base_price"));
            customer.setRatePlan(ratePlan);
        }

        return customer;
    }
} 