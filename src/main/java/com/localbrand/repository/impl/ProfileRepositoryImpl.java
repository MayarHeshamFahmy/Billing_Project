package com.localbrand.repository.impl;

import com.localbrand.model.Profile;
import com.localbrand.repository.ProfileRepository;
import com.localbrand.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileRepositoryImpl implements ProfileRepository {
    private Connection connection;

    public ProfileRepositoryImpl() {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public List<Profile> findAll() {
        List<Profile> profiles = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM profiles");
            while (rs.next()) {
                profiles.add(mapResultSetToProfile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profiles;
    }

    @Override
    public Profile save(Profile profile) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO profiles (name, description, base_price) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            pstmt.setString(1, profile.getName());
            pstmt.setString(2, profile.getDescription());
            pstmt.setBigDecimal(3, profile.getBasePrice());
            
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                profile.setId(rs.getLong(1));
            }
            return profile;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Profile mapResultSetToProfile(ResultSet rs) throws SQLException {
        Profile profile = new Profile();
        profile.setId(rs.getLong("id"));
        profile.setName(rs.getString("name"));
        profile.setDescription(rs.getString("description"));
        profile.setBasePrice(rs.getBigDecimal("base_price"));
        return profile;
    }
} 