package com.farmgo.websocket;

import com.farmgo.config.WebSocketConfig;
import com.farmgo.service.NotificationService;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/notifications", configurator = WebSocketConfig.class)
public class NotificationWebSocket {
    
    private NotificationService notificationService = NotificationService.getInstance();
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened: " + session.getId());
        notificationService.addSession(session);
        
        // Send welcome message
        try {
            session.getBasicRemote().sendText("{\"type\":\"welcome\",\"message\":\"Connected to FarmGo real-time notifications\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message);
        // Echo the message back to the client
        try {
            session.getBasicRemote().sendText("{\"type\":\"echo\",\"message\":\"" + message + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("WebSocket connection closed: " + session.getId() + " Reason: " + reason);
        notificationService.removeSession(session);
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error for session " + session.getId() + ": " + throwable.getMessage());
        throwable.printStackTrace();
    }
}