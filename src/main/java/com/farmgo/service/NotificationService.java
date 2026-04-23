package com.farmgo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class NotificationService {
    private static NotificationService instance = null;
    private final Set<Session> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private NotificationService() {}
    
    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }
    
    public void addSession(Session session) {
        sessions.add(session);
    }
    
    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    public void sendNotificationToAll(Object notification) {
        sendNotificationToAll(notification, null);
    }
    
    public void sendNotificationToAll(Object notification, String type) {
        try {
            String message;
            if (type != null) {
                message = objectMapper.writeValueAsString(
                    new NotificationMessage(type, notification)
                );
            } else {
                message = objectMapper.writeValueAsString(notification);
            }
            
            for (Session session : sessions) {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendNotificationToSession(Session session, Object notification) {
        sendNotificationToSession(session, notification, null);
    }
    
    public void sendNotificationToSession(Session session, Object notification, String type) {
        try {
            String message;
            if (type != null) {
                message = objectMapper.writeValueAsString(
                    new NotificationMessage(type, notification)
                );
            } else {
                message = objectMapper.writeValueAsString(notification);
            }
            
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Inner class for notification messages
    public static class NotificationMessage {
        private String type;
        private Object data;
        private long timestamp;
        
        public NotificationMessage() {
            this.timestamp = System.currentTimeMillis();
        }
        
        public NotificationMessage(String type, Object data) {
            this();
            this.type = type;
            this.data = data;
        }
        
        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
    
    // Specific notification types
    public static class InventoryUpdateNotification {
        private int productId;
        private String productName;
        private int newQuantity;
        private String message;
        
        public InventoryUpdateNotification() {}
        
        public InventoryUpdateNotification(int productId, String productName, int newQuantity, String message) {
            this.productId = productId;
            this.productName = productName;
            this.newQuantity = newQuantity;
            this.message = message;
        }
        
        // Getters and setters
        public int getProductId() { return productId; }
        public void setProductId(int productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public int getNewQuantity() { return newQuantity; }
        public void setNewQuantity(int newQuantity) { this.newQuantity = newQuantity; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class OrderStatusNotification {
        private int orderId;
        private String orderStatus;
        private String message;
        
        public OrderStatusNotification() {}
        
        public OrderStatusNotification(int orderId, String orderStatus, String message) {
            this.orderId = orderId;
            this.orderStatus = orderStatus;
            this.message = message;
        }
        
        // Getters and setters
        public int getOrderId() { return orderId; }
        public void setOrderId(int orderId) { this.orderId = orderId; }
        public String getOrderStatus() { return orderStatus; }
        public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}