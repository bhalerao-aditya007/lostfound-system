package com.lostfound.db;

import com.lostfound.util.SimpleLogger;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
    private static Properties properties = null;

    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (FileInputStream fis = new FileInputStream("config/app.properties")) {
                properties.load(fis);
                SimpleLogger.log("Configuration loaded successfully");
            } catch (IOException e) {
                SimpleLogger.error("Failed to load config/app.properties: " + e.getMessage());
                throw new RuntimeException("Configuration file not found", e);
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        loadProperties();

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        if (url == null || user == null || password == null) {
            throw new SQLException("Database configuration incomplete");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            SimpleLogger.error("MySQL JDBC Driver not found!");
            throw new SQLException("JDBC Driver not found", e);
        }

        try {
            
            Connection conn = DriverManager.getConnection(url, user, password);
            SimpleLogger.log("Database connection established");
            return conn;
        } catch (SQLException e) {
            SimpleLogger.error("Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }

    public static String getProperty(String key) {
        loadProperties();
        return properties.getProperty(key);
    }
}