package com.farmgo.controller;

import com.farmgo.dao.CartDAO;
import com.farmgo.dao.OrderDAO;
import com.farmgo.entity.Order;
import com.farmgo.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/order/*")
public class OrderController extends BaseController {
    private OrderDAO orderDAO = new OrderDAO();
    private CartDAO cartDAO = new CartDAO();
    private NotificationService notificationService = NotificationService.getInstance();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if ("/place".equals(pathInfo)) {
                placeOrder(request, response);
            } else if ("/update-status".equals(pathInfo)) {
                updateOrderStatus(request, response);
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
        
        try {
            if ("/summary".equals(pathInfo)) {
                getOrderSummary(request, response);
            } else {
                sendErrorResponse(response, "Invalid endpoint", 404);
            }
        } catch (SQLException e) {
            sendErrorResponse(response, "Database error: " + e.getMessage(), 500);
        }
    }
    
    private void placeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String pmode = request.getParameter("pmode");
        String products = request.getParameter("products");
        BigDecimal grandTotal = new BigDecimal(request.getParameter("grand_total"));
        
        Order order = new Order();
        order.setName(name);
        order.setEmail(email);
        order.setPhone(phone);
        order.setAddress(address);
        order.setPmode(pmode);
        order.setProducts(products);
        order.setAmountPaid(grandTotal);
        
        boolean success = orderDAO.createOrder(order);
        
        if (success) {
            // Clear the cart after successful order
            cartDAO.clearCart();
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Your Order Placed Successfully!");
            result.put("orderDetails", order);
            sendResponse(response, result);
        } else {
            sendErrorResponse(response, "Failed to place order", 500);
        }
    }
    
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String status = request.getParameter("status");
        
        boolean success = orderDAO.updateOrderStatus(orderId, status);
        
        if (success) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Order status updated successfully!");
            result.put("orderId", orderId);
            result.put("status", status);
            sendResponse(response, result);
        } else {
            sendErrorResponse(response, "Failed to update order status", 500);
        }
    }
    
    private void getOrderSummary(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String items = orderDAO.getCartItemsForOrder();
        BigDecimal totalAmount = orderDAO.getCartTotalAmount();
        
        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("totalAmount", totalAmount);
        
        sendResponse(response, result);
    }
}