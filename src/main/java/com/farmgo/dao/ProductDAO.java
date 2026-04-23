package com.farmgo.dao;

import com.farmgo.entity.Product;
import com.farmgo.service.NotificationService;
import com.farmgo.util.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    
    private NotificationService notificationService = NotificationService.getInstance();
    
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setProductImage(resultSet.getString("product_image"));
                product.setProductName(resultSet.getString("product_name"));
                product.setProductPrice(resultSet.getBigDecimal("product_price"));
                product.setProductQty(resultSet.getInt("product_qty"));
                product.setProductCode(resultSet.getString("product_code"));
                products.add(product);
            }
        }
        return products;
    }
    
    public Product getProductById(int id) throws SQLException {
        String query = "SELECT * FROM product WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setProductImage(resultSet.getString("product_image"));
                    product.setProductName(resultSet.getString("product_name"));
                    product.setProductPrice(resultSet.getBigDecimal("product_price"));
                    product.setProductQty(resultSet.getInt("product_qty"));
                    product.setProductCode(resultSet.getString("product_code"));
                    return product;
                }
            }
        }
        return null;
    }
    
    public boolean updateProductQuantity(int productId, int newQuantity) throws SQLException {
        String query = "UPDATE product SET product_qty = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, newQuantity);
            statement.setInt(2, productId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                // Send real-time notification about inventory update
                Product product = getProductById(productId);
                if (product != null) {
                    NotificationService.InventoryUpdateNotification notification = 
                        new NotificationService.InventoryUpdateNotification(
                            productId,
                            product.getProductName(),
                            newQuantity,
                            "Product quantity updated to " + newQuantity
                        );
                    
                    notificationService.sendNotificationToAll(notification, "inventory_update");
                }
                return true;
            }
            return false;
        }
    }
}