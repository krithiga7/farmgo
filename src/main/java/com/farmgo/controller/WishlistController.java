package com.farmgo.controller;

import com.farmgo.dao.WishlistDAO;
import com.farmgo.entity.Wishlist;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/wishlist/*")
public class WishlistController extends BaseController {
    private WishlistDAO wishlistDAO = new WishlistDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        // Check if user is logged in
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            sendErrorResponse(response, "User not logged in", 401);
            return;
        }
        
        try {
            if ("/add".equals(pathInfo)) {
                addToWishlist(request, response, userId);
            } else if ("/remove".equals(pathInfo)) {
                removeFromWishlist(request, response, userId);
            } else {
                sendErrorResponse(response, "Invalid endpoint", 404);
            }
        } catch (SQLException e) {
            sendErrorResponse(response, "Database error: " + e.getMessage(), 500);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        // Check if user is logged in
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            sendErrorResponse(response, "User not logged in", 401);
            return;
        }
        
        try {
            if ("/items".equals(pathInfo)) {
                getWishlistItems(request, response, userId);
            } else if ("/count".equals(pathInfo)) {
                getWishlistCount(request, response, userId);
            } else {
                sendErrorResponse(response, "Invalid endpoint", 404);
            }
        } catch (SQLException e) {
            sendErrorResponse(response, "Database error: " + e.getMessage(), 500);
        }
    }
    
    private void addToWishlist(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        BigDecimal productPrice = new BigDecimal(request.getParameter("productPrice"));
        String productImage = request.getParameter("productImage");
        String productCode = request.getParameter("productCode");
        
        // Check if product already exists in wishlist
        if (wishlistDAO.isProductInWishlist(userId, productId)) {
            Map<String, String> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "Item already in your wishlist!");
            sendResponse(response, result);
            return;
        }
        
        Wishlist wishlistItem = new Wishlist();
        wishlistItem.setUserId(userId);
        wishlistItem.setProductId(productId);
        wishlistItem.setProductName(productName);
        wishlistItem.setProductPrice(productPrice);
        wishlistItem.setProductImage(productImage);
        wishlistItem.setProductCode(productCode);
        
        boolean success = wishlistDAO.addToWishlist(wishlistItem);
        
        Map<String, String> result = new HashMap<>();
        if (success) {
            result.put("status", "success");
            result.put("message", "Item added to your wishlist!");
        } else {
            result.put("status", "error");
            result.put("message", "Failed to add item to wishlist!");
        }
        sendResponse(response, result);
    }
    
    private void removeFromWishlist(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        
        boolean success = wishlistDAO.removeFromWishlist(userId, productId);
        
        Map<String, String> result = new HashMap<>();
        if (success) {
            result.put("status", "success");
            result.put("message", "Item removed from your wishlist!");
        } else {
            result.put("status", "error");
            result.put("message", "Failed to remove item from wishlist!");
        }
        sendResponse(response, result);
    }
    
    private void getWishlistItems(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        List<Wishlist> wishlistItems = wishlistDAO.getWishlistItems(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("items", wishlistItems);
        result.put("count", wishlistItems.size());
        
        sendResponse(response, result);
    }
    
    private void getWishlistCount(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException, SQLException {
        int count = wishlistDAO.getWishlistItemCount(userId);
        
        Map<String, Integer> result = new HashMap<>();
        result.put("count", count);
        
        sendResponse(response, result);
    }
}