package com.farmgo.dao;

import com.farmgo.entity.User;
import com.farmgo.util.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    public User authenticateUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection connection = DatabaseConfig.getLoginConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, email);
            statement.setString(2, password);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAddress(resultSet.getString("address"));
                    user.setCity(resultSet.getString("city"));
                    user.setState(resultSet.getString("state"));
                    user.setZipCode(resultSet.getString("zip_code"));
                    user.setCountry(resultSet.getString("country"));
                    user.setUserType(resultSet.getString("user_type"));
                    user.setLanguage(resultSet.getString("language"));
                    user.setProfileImage(resultSet.getString("profile_image"));
                    return user;
                }
            }
        }
        return null;
    }
    
    public boolean registerUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, email, password, first_name, last_name, phone, address, city, state, zip_code, country, user_type, language, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConfig.getLoginConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getPhone());
            statement.setString(7, user.getAddress());
            statement.setString(8, user.getCity());
            statement.setString(9, user.getState());
            statement.setString(10, user.getZipCode());
            statement.setString(11, user.getCountry());
            statement.setString(12, user.getUserType());
            statement.setString(13, user.getLanguage());
            statement.setString(14, user.getProfileImage());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean isEmailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection connection = DatabaseConfig.getLoginConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getLoginConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAddress(resultSet.getString("address"));
                    user.setCity(resultSet.getString("city"));
                    user.setState(resultSet.getString("state"));
                    user.setZipCode(resultSet.getString("zip_code"));
                    user.setCountry(resultSet.getString("country"));
                    user.setUserType(resultSet.getString("user_type"));
                    user.setLanguage(resultSet.getString("language"));
                    user.setProfileImage(resultSet.getString("profile_image"));
                    return user;
                }
            }
        }
        return null;
    }
    
    public boolean updateUserProfile(User user) throws SQLException {
        String query = "UPDATE users SET first_name = ?, last_name = ?, phone = ?, address = ?, city = ?, state = ?, zip_code = ?, country = ?, language = ?, profile_image = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getLoginConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getAddress());
            statement.setString(5, user.getCity());
            statement.setString(6, user.getState());
            statement.setString(7, user.getZipCode());
            statement.setString(8, user.getCountry());
            statement.setString(9, user.getLanguage());
            statement.setString(10, user.getProfileImage());
            statement.setInt(11, user.getId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean updateUserLanguage(int userId, String language) throws SQLException {
        String query = "UPDATE users SET language = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getLoginConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, language);
            statement.setInt(2, userId);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}