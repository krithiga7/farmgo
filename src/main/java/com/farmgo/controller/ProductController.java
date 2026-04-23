package com.farmgo.controller;

import com.farmgo.dao.ProductDAO;
import com.farmgo.entity.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/products")
public class ProductController extends BaseController {
    private ProductDAO productDAO = new ProductDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Product> products = productDAO.getAllProducts();
            
            Map<String, Object> result = new HashMap<>();
            result.put("products", products);
            result.put("count", products.size());
            
            sendResponse(response, result);
        } catch (SQLException e) {
            sendErrorResponse(response, "Database error: " + e.getMessage(), 500);
        }
    }
}