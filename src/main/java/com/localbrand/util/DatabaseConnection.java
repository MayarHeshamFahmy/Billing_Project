package com.localbrand.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String BILLING_SYSTEM_URL = "jdbc:mysql://localhost:3306/billing_system";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static Connection getRatedCdrsConnection() throws SQLException {
        return DriverManager.getConnection(BILLING_SYSTEM_URL, USER, PASSWORD);
    }
} 