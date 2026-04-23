package com.farmgo.dao;

import com.farmgo.entity.Cart;
import com.farmgo.service.NotificationService;
import com.farmgo.util.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    
    private NotificationService notificationService = NotificationService.getInstance();
    private ProductDAO productDAO = new ProductDAO();
    
    public List<Cart> getAllCartItems() throws SQLException {
        List<Cart> cartItems = new ArrayList<>();
        String query = "SELECT * FROM cart";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Cart cartItem = new Cart();
                cartItem.setId(resultSet.getInt("id"));
                cartItem.setProductName(resultSet.getString("product_name"));
                cartItem.setProductPrice(resultSet.getBigDecimal("product_price"));
                cartItem.setProductImage(resultSet.getString("product_image"));
                cartItem.setQty(resultSet.getInt("qty"));
                cartItem.setTotalPrice(resultSet.getBigDecimal("total_price"));
                cartItem.setProductCode(resultSet.getString("product_code"));
                cartItems.add(cartItem);
            }
        }
        return cartItems;
    }
    
    public boolean isProductInCart(String productCode) throws SQLException {
        String query = "SELECT COUNT(*) FROM cart WHERE product_code = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, productCode);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public boolean addProductToCart(Cart cartItem) throws SQLException {
        String query = "INSERT INTO cart (product_name, product_price, product_image, qty, total_price, product_code) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, cartItem.getProductName());
            statement.setBigDecimal(2, cartItem.getProductPrice());
            statement.setString(3, cartItem.getProductImage());
            statement.setInt(4, cartItem.getQty());
            statement.setBigDecimal(5, cartItem.getTotalPrice());
            statement.setString(6, cartItem.getProductCode());
            
            int rowsAffected = statement.executeUpdate();
            
            // Update product inventory in real-time
            if (rowsAffected > 0) {
                // In a real implementation, you would decrease the product quantity
                // For now, we'll just send a notification
                NotificationService.InventoryUpdateNotification notification = 
                    new NotificationService.InventoryUpdateNotification(
                        0, // We don't have product ID in cart item
                        cartItem.getProductName(),
                        0, // We don't have the actual quantity here
                        "Item added to cart: " + cartItem.getProductName()
                    );
                
                notificationService.sendNotificationToAll(notification, "inventory_update");
            }
            
            return rowsAffected > 0;
        }
    }
    
    public int getCartItemCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM cart";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }
    
    public boolean removeCartItem(int id) throws SQLException {
        String query = "DELETE FROM cart WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean clearCart() throws SQLException {
        String query = "DELETE FROM cart";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected >= 0;
        }
    }
    
    public boolean updateCartItemQuantity(int id, int qty, java.math.BigDecimal totalPrice) throws SQLException {
        String query = "UPDATE cart SET qty = ?, total_price = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, qty);
            statement.setBigDecimal(2, totalPrice);
            statement.setInt(3, id);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}