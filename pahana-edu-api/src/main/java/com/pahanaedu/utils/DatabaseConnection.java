package com.pahanaedu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    private Connection connection;

    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database.properties");
            if (inputStream == null) {
                throw new RuntimeException("database.properties file not found in the classpath");
            }
            props.load(inputStream);

            String driver = props.getProperty("db.driver");
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            Class.forName(driver);

            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established successfully!");

        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println("Failed to connect to the database. Check your configuration and database server.");
            e.printStackTrace();
            throw new RuntimeException("Error initializing database connection", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}