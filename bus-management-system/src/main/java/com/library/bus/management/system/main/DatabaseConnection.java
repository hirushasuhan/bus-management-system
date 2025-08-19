/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.library.bus.management.system.main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author HP
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bus_management_system_new";
    private static final String USERNAME = "root"; // Change as per your MySQL setup
    private static final String PASSWORD = "root"; // Change as per your MySQL setup
    
    private static Connection connection = null;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully!");
            System.out.println("System Date: 2025-08-18 06:26:08 UTC");
            System.out.println("Current User: Hirusha suhan");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: "
                    + "" + e.getMessage());
        }
    }
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database 'bus_management_system_new' connected successfully!");
                
                Statement stmt = connection.createStatement();
                stmt.execute("SELECT 1");
                System.out.println("Database connection verified!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.err.println("Please check:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'bus_management_system_new' exists");
            System.err.println("3. Username and password are correct");
            System.err.println("4. Run the database_schema.sql file first");
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed gracefully.");
                System.out.println("Session ended by: hirushasuhan");
                System.out.println("Closed at: 2025-08-18 06:26:08 UTC");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                Statement stmt = conn.createStatement();
                stmt.execute("SELECT 1");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
        }
        return false;
    }
    
    public static void printConnectionInfo() {
        System.out.println("\nDATABASE CONNECTION INFO:");
        System.out.println("URL: " + URL);
        System.out.println("Username: " + USERNAME);
        System.out.println("Status: " + (testConnection() ? "Connected" : "Disconnected"));
        System.out.println("Last Check: 2025-08-18 06:26:08 UTC");
        System.out.println("By: Hirusha suhan\n");
    }
}
