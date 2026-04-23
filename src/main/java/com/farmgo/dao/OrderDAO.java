package com.farmgo.dao;

import com.farmgo.entity.Order;
import com.farmgo.service.NotificationService;
import com.farmgo.util.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    
    private NotificationService notificationService = NotificationService.getInstance();
    
    public boolean createOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (name, email, phone, address, pmode, products, amount_paid) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, order.getName());
            statement.setString(2, order.getEmail());
            statement.setString(3, order.getPhone());
            statement.setString(4, order.getAddress());
            statement.setString(5, order.getPmode());
            statement.setString(6, order.getProducts());
            statement.setBigDecimal(7, order.getAmountPaid());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated order ID
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);
                        
                        // Send real-time notification about new order
                        NotificationService.OrderStatusNotification notification = 
                            new NotificationService.OrderStatusNotification(
                                orderId,
                                "confirmed",
                                "New order #" + orderId + " has been placed"
                            );
                        
                        notificationService.sendNotificationToAll(notification, "order_status");
                    }
                }
                return true;
            }
            return false;
        }
    }
    
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY id DESC";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setName(resultSet.getString("name"));
                order.setEmail(resultSet.getString("email"));
                order.setPhone(resultSet.getString("phone"));
                order.setAddress(resultSet.getString("address"));
                order.setPmode(resultSet.getString("pmode"));
                order.setProducts(resultSet.getString("products"));
                order.setAmountPaid(resultSet.getBigDecimal("amount_paid"));
                orders.add(order);
            }
        }
        return orders;
    }
    
    public String getCartItemsForOrder() throws SQLException {
        StringBuilder items = new StringBuilder();
        String query = "SELECT CONCAT(product_name, '(', qty, ')') AS ItemQty FROM cart";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                if (items.length() > 0) {
                    items.append(", ");
                }
                items.append(resultSet.getString("ItemQty"));
            }
        }
        return items.toString();
    }
    
    public java.math.BigDecimal getCartTotalAmount() throws SQLException {
        String query = "SELECT SUM(total_price) AS total FROM cart";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getBigDecimal("total") != null ? resultSet.getBigDecimal("total") : java.math.BigDecimal.ZERO;
            }
        }
        return java.math.BigDecimal.ZERO;
    }
    
    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String query = "UPDATE orders SET status = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConfig.getCartConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, status);
            statement.setInt(2, orderId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                // Send real-time notification about order status update
                NotificationService.OrderStatusNotification notification = 
                    new NotificationService.OrderStatusNotification(
                        orderId,
                        status,
                        "Order #" + orderId + " status updated to " + status
                    );
                
                notificationService.sendNotificationToAll(notification, "order_status");
                return true;
            }
            return false;
        }
    }
}