package com.farmgo.websocket;

import com.farmgo.config.WebSocketConfig;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/chat/{username}", configurator = WebSocketConfig.class)
public class ChatWebSocket {
    
    // Store all active sessions
    private static final CopyOnWriteArraySet<ChatWebSocket> chatEndpoints = new CopyOnWriteArraySet<>();
    
    // Store username for each session
    private static final ConcurrentHashMap<String, Session> users = new ConcurrentHashMap<>();
    
    private Session session;
    private String username;
    
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        this.session = session;
        this.username = username;
        
        // Add the session to the list of active sessions
        chatEndpoints.add(this);
        users.put(username, session);
        
        // Send welcome message to the user
        sendMessage(session, "Welcome to FarmGo Customer Support Chat, " + username + "!");
        
        // Notify all users about the new user
        broadcast("User " + username + " has joined the chat.");
    }
    
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Broadcast the message to all connected users
        if (this.username != null) {
            broadcast("[" + this.username + "]: " + message);
        }
    }
    
    @OnClose
    public void onClose(Session session) throws IOException {
        // Remove the session from the list of active sessions
        chatEndpoints.remove(this);
        users.remove(this.username);
        
        // Notify all users about the user leaving
        broadcast("User " + this.username + " has left the chat.");
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Chat WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace();
    }
    
    // Send message to a specific session
    private void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
    
    // Broadcast message to all connected users
    private void broadcast(String message) {
        chatEndpoints.forEach(endpoint -> {
            try {
                endpoint.sendMessage(endpoint.session, message);
            } catch (IOException e) {
                System.err.println("Error broadcasting message: " + e.getMessage());
            }
        });
    }
}