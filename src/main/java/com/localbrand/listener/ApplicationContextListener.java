package com.localbrand.listener;

import com.localbrand.util.MySQLDriverCleanup;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Application startup logic
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Clean up MySQL driver and threads when application is stopped
        MySQLDriverCleanup.cleanup();
    }
} 