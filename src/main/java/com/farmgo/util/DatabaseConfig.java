package com.farmgo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    private static final String H2_URL = "jdbc:h2:mem:farmgo;DB_CLOSE_DELAY=-1";
    private static final String H2_USERNAME = "sa";
    private static final String H2_PASSWORD = "";
    
    // Database names (not used with H2 in-memory)
    public static final String LOGIN_DB = "login_register";
    public static final String CART_DB = "cart_system";
    
    static {
        try {
            // Initialize H2 database with tables and sample data
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void initializeDatabase() throws SQLException {
        try (Connection connection = getH2Connection();
             Statement statement = connection.createStatement()) {
            
            // Create users table
            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(44) NOT NULL, " +
                    "email VARCHAR(44) NOT NULL, " +
                    "password VARCHAR(44) NOT NULL, " +
                    "first_name VARCHAR(50), " +
                    "last_name VARCHAR(50), " +
                    "phone VARCHAR(20), " +
                    "address TEXT, " +
                    "city VARCHAR(50), " +
                    "state VARCHAR(50), " +
                    "zip_code VARCHAR(10), " +
                    "country VARCHAR(50), " +
                    "user_type VARCHAR(20) DEFAULT 'customer', " +
                    "language VARCHAR(10) DEFAULT 'en', " +
                    "profile_image VARCHAR(255))");
            
            // Create product table
            statement.execute("CREATE TABLE IF NOT EXISTS product (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "product_image VARCHAR(255) NOT NULL, " +
                    "product_name VARCHAR(255), " +
                    "product_price DECIMAL(10,2), " +
                    "product_qty INT, " +
                    "product_code VARCHAR(255))");
            
            // Create cart table
            statement.execute("CREATE TABLE IF NOT EXISTS cart (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "product_name VARCHAR(255), " +
                    "product_price DECIMAL(10,2), " +
                    "product_image VARCHAR(255), " +
                    "qty INT, " +
                    "total_price DECIMAL(10,2), " +
                    "product_code VARCHAR(255))");
            
            // Create orders table
            statement.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "email VARCHAR(255), " +
                    "phone VARCHAR(255), " +
                    "address VARCHAR(255), " +
                    "pmode VARCHAR(255), " +
                    "products TEXT, " +
                    "amount_paid DECIMAL(10,2))");
            
            // Create wishlist table
            statement.execute("CREATE TABLE IF NOT EXISTS wishlist (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT, " +
                    "product_id INT, " +
                    "product_name VARCHAR(255), " +
                    "product_price DECIMAL(10,2), " +
                    "product_image VARCHAR(255), " +
                    "product_code VARCHAR(255))");
            
            // Insert sample data
            // Sample user with profile details
            statement.execute("INSERT INTO users (username, email, password, first_name, last_name, phone, address, city, state, zip_code, country, user_type, language) VALUES " +
                    "('admin', 'admin@gmail.com', 'admin', 'Admin', 'User', '+91-9876543210', '123 Main Street', 'Vellore', 'Tamil Nadu', '632014', 'India', 'admin', 'en')");
            statement.execute("INSERT INTO users (username, email, password, first_name, last_name, phone, address, city, state, zip_code, country, user_type, language) VALUES " +
                    "('farmer1', 'farmer1@gmail.com', 'farmer123', 'Rajesh', 'Kumar', '+91-9876543211', '456 Farm Road', 'Vellore', 'Tamil Nadu', '632014', 'India', 'farmer', 'ta')");
            statement.execute("INSERT INTO users (username, email, password, first_name, last_name, phone, address, city, state, zip_code, country, user_type, language) VALUES " +
                    "('customer1', 'customer1@gmail.com', 'customer123', 'Priya', 'Sharma', '+91-9876543212', '789 Market Street', 'Vellore', 'Tamil Nadu', '632014', 'India', 'customer', 'hi')");
            
            // Sample products
            statement.execute("INSERT INTO product (product_image, product_name, product_price, product_qty, product_code) VALUES " +
                    "('image/apple.jpg', 'Apple', 100.00, 5, 'APL001')");
            statement.execute("INSERT INTO product (product_image, product_name, product_price, product_qty, product_code) VALUES " +
                    "('image/banana.jpg', 'Banana', 100.00, 8, 'BNN001')");
            statement.execute("INSERT INTO product (product_image, product_name, product_price, product_qty, product_code) VALUES " +
                    "('image/cabbage.jpeg', 'Cabbage', 100.00, 3, 'CBG001')");
            statement.execute("INSERT INTO product (product_image, product_name, product_price, product_qty, product_code) VALUES " +
                    "('image/egg.jpeg', 'Egg', 100.00, 10, 'EGG001')");
            statement.execute("INSERT INTO product (product_image, product_name, product_price, product_qty, product_code) VALUES " +
                    "('image/papaya.jpg', 'Papaya', 100.00, 6, 'PPY001')");
            statement.execute("INSERT INTO product (product_image, product_name, product_price, product_qty, product_code) VALUES " +
                    "('image/pumpkin.jpg', 'Pumpkin', 100.00, 4, 'PMK001')");
            statement.execute("INSERT INTO product (product_image, product_name, product_price, product_qty, product_code) VALUES " +
                    "('image/tomato.jpeg', 'Tomato', 100.00, 7, 'TMT001')");
            statement.execute("INSERT INTO product (product_image, product_name, product_price, product_qty, product_code) VALUES " +
                    "('image/potato.jpeg', 'Potato', 100.00, 9, 'PTT001')");
        }
    }
    
    public static Connection getH2Connection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(H2_URL, H2_USERNAME, H2_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 JDBC Driver not found", e);
        }
    }
    
    // For backward compatibility, we'll use the same method names
    public static Connection getConnection(String database) throws SQLException {
        return getH2Connection();
    }
    
    public static Connection getLoginConnection() throws SQLException {
        return getH2Connection();
    }
    
    public static Connection getCartConnection() throws SQLException {
        return getH2Connection();
    }
}