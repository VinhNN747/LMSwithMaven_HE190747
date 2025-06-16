package com.controller.user_controller;

import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/delete")
public class UserDeleteServlet extends BaseUserServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        User user = userDao.findById(id);
        if (user == null) {
            request.setAttribute("error", "User not found");
        } else {
            try {
                userDao.delete(user);
            } catch (Exception e) {
                request.setAttribute("error",
                        "Cannot delete user: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            }
        }
        response.sendRedirect("list");
    }
} 