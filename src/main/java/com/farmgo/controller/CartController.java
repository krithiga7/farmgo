package com.farmgo.controller;

import com.farmgo.dao.CartDAO;
import com.farmgo.dao.ProductDAO;
import com.farmgo.entity.Cart;
import com.farmgo.entity.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/cart/*")
public class CartController extends BaseController {
    private CartDAO cartDAO = new CartDAO();
    private ProductDAO productDAO = new ProductDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if ("/add".equals(pathInfo)) {
                addProductToCart(request, response);
            } else if ("/update".equals(pathInfo)) {
                updateCartItem(request, response);
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
            if ("/items".equals(pathInfo)) {
                getCartItems(request, response);
            } else if ("/count".equals(pathInfo)) {
                getCartCount(request, response);
            } else {
                sendErrorResponse(response, "Invalid endpoint", 404);
            }
        } catch (SQLException e) {
            sendErrorResponse(response, "Database error: " + e.getMessage(), 500);
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo != null && pathInfo.startsWith("/remove/")) {
                int id = Integer.parseInt(pathInfo.substring("/remove/".length()));
                removeCartItem(id, response);
            } else if ("/clear".equals(pathInfo)) {
                clearCart(response);
            } else {
                sendErrorResponse(response, "Invalid endpoint", 404);
            }
        } catch (NumberFormatException e) {
            sendErrorResponse(response, "Invalid item ID", 400);
        } catch (SQLException e) {
            sendErrorResponse(response, "Database error: " + e.getMessage(), 500);
        }
    }
    
    private void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int pid = Integer.parseInt(request.getParameter("pid"));
        String pname = request.getParameter("pname");
        BigDecimal pprice = new BigDecimal(request.getParameter("pprice"));
        String pimage = request.getParameter("pimage");
        String pcode = request.getParameter("pcode");
        int pqty = Integer.parseInt(request.getParameter("pqty"));
        BigDecimal totalPrice = pprice.multiply(new BigDecimal(pqty));
        
        // Check if product already exists in cart
        if (cartDAO.isProductInCart(pcode)) {
            Map<String, String> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "Item already added to your cart!");
            sendResponse(response, result);
            return;
        }
        
        Cart cartItem = new Cart();
        cartItem.setProductName(pname);
        cartItem.setProductPrice(pprice);
        cartItem.setProductImage(pimage);
        cartItem.setQty(pqty);
        cartItem.setTotalPrice(totalPrice);
        cartItem.setProductCode(pcode);
        
        boolean success = cartDAO.addProductToCart(cartItem);
        
        Map<String, String> result = new HashMap<>();
        if (success) {
            result.put("status", "success");
            result.put("message", "Item added to your cart!");
        } else {
            result.put("status", "error");
            result.put("message", "Failed to add item to cart!");
        }
        sendResponse(response, result);
    }
    
    private void getCartItems(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        List<Cart> cartItems = cartDAO.getAllCartItems();
        
        BigDecimal grandTotal = BigDecimal.ZERO;
        for (Cart item : cartItems) {
            grandTotal = grandTotal.add(item.getTotalPrice());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("items", cartItems);
        result.put("grandTotal", grandTotal);
        result.put("count", cartItems.size());
        
        sendResponse(response, result);
    }
    
    private void getCartCount(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int count = cartDAO.getCartItemCount();
        
        Map<String, Integer> result = new HashMap<>();
        result.put("count", count);
        
        sendResponse(response, result);
    }
    
    private void removeCartItem(int id, HttpServletResponse response) throws IOException, SQLException {
        boolean success = cartDAO.removeCartItem(id);
        
        Map<String, String> result = new HashMap<>();
        if (success) {
            result.put("status", "success");
            result.put("message", "Item removed from the cart!");
        } else {
            result.put("status", "error");
            result.put("message", "Failed to remove item from cart!");
        }
        sendResponse(response, result);
    }
    
    private void clearCart(HttpServletResponse response) throws IOException, SQLException {
        boolean success = cartDAO.clearCart();
        
        Map<String, String> result = new HashMap<>();
        if (success) {
            result.put("status", "success");
            result.put("message", "All items removed from the cart!");
        } else {
            result.put("status", "error");
            result.put("message", "Failed to clear cart!");
        }
        sendResponse(response, result);
    }
    
    private void updateCartItem(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int qty = Integer.parseInt(request.getParameter("qty"));
        int pid = Integer.parseInt(request.getParameter("pid"));
        BigDecimal pprice = new BigDecimal(request.getParameter("pprice"));
        BigDecimal totalPrice = pprice.multiply(new BigDecimal(qty));
        
        boolean success = cartDAO.updateCartItemQuantity(pid, qty, totalPrice);
        
        Map<String, String> result = new HashMap<>();
        if (success) {
            result.put("status", "success");
            result.put("message", "Cart updated successfully!");
        } else {
            result.put("status", "error");
            result.put("message", "Failed to update cart!");
        }
        sendResponse(response, result);
    }
}