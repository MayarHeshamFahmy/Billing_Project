package com.localbrand.util;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLDriverCleanup {
    private static final Logger LOGGER = Logger.getLogger(MySQLDriverCleanup.class.getName());

    public static void cleanup() {
        // Deregister JDBC drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOGGER.log(Level.INFO, "Deregistered JDBC driver: {0}", driver);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error deregistering JDBC driver: {0}", e.getMessage());
            }
        }

        // Shutdown MySQL's AbandonedConnectionCleanupThread
        try {
            Class<?> cleanupThread = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
            cleanupThread.getMethod("checkedShutdown").invoke(null);
            LOGGER.info("MySQL AbandonedConnectionCleanupThread shutdown completed");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error shutting down MySQL cleanup thread: {0}", e.getMessage());
        }
    }
} 