package com.farmgo.dao;

import com.farmgo.entity.Wishlist;
import com.farmgo.util.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {
    
    public List<Wishlist> getWishlistItems(int userId) throws SQLException {
        List<Wishlist> wishlistItems = new ArrayList<>();
        String query = "SELECT * FROM wishlist WHERE user_id = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Wishlist item = new Wishlist();
                    item.setId(resultSet.getInt("id"));
                    item.setUserId(resultSet.getInt("user_id"));
                    item.setProductId(resultSet.getInt("product_id"));
                    item.setProductName(resultSet.getString("product_name"));
                    item.setProductPrice(resultSet.getBigDecimal("product_price"));
                    item.setProductImage(resultSet.getString("product_image"));
                    item.setProductCode(resultSet.getString("product_code"));
                    wishlistItems.add(item);
                }
            }
        }
        return wishlistItems;
    }
    
    public boolean addToWishlist(Wishlist wishlistItem) throws SQLException {
        String query = "INSERT INTO wishlist (user_id, product_id, product_name, product_price, product_image, product_code) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, wishlistItem.getUserId());
            statement.setInt(2, wishlistItem.getProductId());
            statement.setString(3, wishlistItem.getProductName());
            statement.setBigDecimal(4, wishlistItem.getProductPrice());
            statement.setString(5, wishlistItem.getProductImage());
            statement.setString(6, wishlistItem.getProductCode());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean removeFromWishlist(int userId, int productId) throws SQLException {
        String query = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean isProductInWishlist(int userId, int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM wishlist WHERE user_id = ? AND product_id = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public int getWishlistItemCount(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM wishlist WHERE user_id = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
}