package com.controller.controller_authorization;

import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Example servlet demonstrating proper usage of AuthorizationServlet.
 * This servlet will only be accessible to users with the appropriate permissions.
 * 
 * @author vinhnnpc
 */
@WebServlet("/example/authorized")
public class ExampleAuthorizedServlet extends AuthorizationServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        // This method is only called if the user is authorized
        request.setAttribute("user", user);
        request.setAttribute("message", "Welcome to the authorized area!");
        request.getRequestDispatcher("/view/authorized.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        // This method is only called if the user is authorized
        String action = request.getParameter("action");
        
        if ("update".equals(action)) {
            // Handle update action
            response.getWriter().write("Update successful for user: " + user.getUserId());
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
} 