package com.farmgo.controller;

import com.farmgo.dao.UserDAO;
import com.farmgo.entity.User;
import com.farmgo.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/profile/*")
public class ProfileController extends BaseController {
    private UserDAO userDAO = new UserDAO();
    private NotificationService notificationService = NotificationService.getInstance();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            sendErrorResponse(response, "User not logged in", 401);
            return;
        }
        
        try {
            if ("/details".equals(pathInfo)) {
                getUserProfile(request, response, userId);
            } else if ("/orders".equals(pathInfo)) {
                getUserOrders(request, response, userId);
            } else if ("/transactions".equals(pathInfo)) {
                getUserTransactions(request, response, userId);
            } else if ("/settings".equals(pathInfo)) {
                getUserSettings(request, response, userId);
            } else {
                sendErrorResponse(response, "Invalid endpoint", 404);
            }
        } catch (SQLException e) {
            sendErrorResponse(response, "Database error: " + e.getMessage(), 500);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            sendErrorResponse(response, "User not logged in", 401);
            return;
        }
        
        try {
            if ("/update".equals(pathInfo)) {
                updateUserProfile(request, response, userId);
            } else if ("/language".equals(pathInfo)) {
                updateUserLanguage(request, response, userId);
            } else {
                sendErrorResponse(response, "Invalid endpoint", 404);
            }
        } catch (SQLException e) {
            sendErrorResponse(response, "Database error: " + e.getMessage(), 500);
        }
    }
    
    private void getUserProfile(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("user", user);
            sendResponse(response, result);
        } else {
            sendErrorResponse(response, "User not found", 404);
        }
    }
    
    private void getUserOrders(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        // In a real implementation, you would fetch orders for this user
        // For now, we'll return sample data
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("orders", getSampleOrders());
        sendResponse(response, result);
    }
    
    private void getUserTransactions(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        // In a real implementation, you would fetch transactions for this user
        // For now, we'll return sample data
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("transactions", getSampleTransactions());
        sendResponse(response, result);
    }
    
    private void getUserSettings(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("settings", Map.of(
                "language", user.getLanguage(),
                "notifications", true,
                "emailUpdates", true
            ));
            sendResponse(response, result);
        } else {
            sendErrorResponse(response, "User not found", 404);
        }
    }
    
    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zipCode = request.getParameter("zipCode");
        String country = request.getParameter("country");
        String profileImage = request.getParameter("profileImage");
        
        User user = userDAO.getUserById(userId);
        if (user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setAddress(address);
            user.setCity(city);
            user.setState(state);
            user.setZipCode(zipCode);
            user.setCountry(country);
            user.setProfileImage(profileImage);
            
            boolean success = userDAO.updateUserProfile(user);
            if (success) {
                Map<String, Object> result = new HashMap<>();
                result.put("status", "success");
                result.put("message", "Profile updated successfully");
                result.put("user", user);
                sendResponse(response, result);
            } else {
                sendErrorResponse(response, "Failed to update profile", 500);
            }
        } else {
            sendErrorResponse(response, "User not found", 404);
        }
    }
    
    private void updateUserLanguage(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        String language = request.getParameter("language");
        
        boolean success = userDAO.updateUserLanguage(userId, language);
        if (success) {
            // Update session with new language
            HttpSession session = request.getSession();
            session.setAttribute("language", language);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Language updated successfully");
            result.put("language", language);
            sendResponse(response, result);
        } else {
            sendErrorResponse(response, "Failed to update language", 500);
        }
    }
    
    // Sample data methods
    private Object getSampleOrders() {
        return new Object[] {
            Map.of(
                "id", 1001,
                "date", "2023-05-15",
                "items", "Apple (2), Banana (1)",
                "total", 300.00,
                "status", "Delivered"
            ),
            Map.of(
                "id", 1002,
                "date", "2023-05-10",
                "items", "Cabbage (3), Egg (10)",
                "total", 1300.00,
                "status", "Processing"
            )
        };
    }
    
    private Object getSampleTransactions() {
        return new Object[] {
            Map.of(
                "id", 5001,
                "date", "2023-05-15",
                "amount", 300.00,
                "type", "Debit Card",
                "status", "Completed"
            ),
            Map.of(
                "id", 5002,
                "date", "2023-05-10",
                "amount", 1300.00,
                "type", "Cash on Delivery",
                "status", "Completed"
            )
        };
    }
}