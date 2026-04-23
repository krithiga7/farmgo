package com.farmgo.controller;

import com.farmgo.dao.UserDAO;
import com.farmgo.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/login")
public class LoginController extends BaseController {
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                sendErrorResponse(response, "Email and password are required", 400);
                return;
            }
            
            User user = userDAO.authenticateUser(email, password);
            
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("username", user.getUsername());
                session.setAttribute("userId", user.getId());
                
                // Regenerate session ID for security
                request.changeSessionId();
                
                response.sendRedirect(request.getContextPath() + "/html/index.html");
            } else {
                response.sendRedirect(request.getContextPath() + "/LoginIndex.html?error=invalid_credentials");
            }
        } catch (SQLException e) {
            sendErrorResponse(response, "Database error: " + e.getMessage(), 500);
        }
    }
}