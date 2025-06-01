package com.localbrand.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void main(String[] args) {
        // Update these based on your DB
        String jdbcURL = "jdbc:mysql://localhost:3306/billing_system"; // Replace 'local' with your DB name
        String username = "root"; // Your DB username
        String password = "root"; // Your DB password

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Attempt connection
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            if (connection != null) {
                System.out.println("✅ Connected to the database successfully!");
                connection.close();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed.");
            e.printStackTrace();
        }
    }
}
