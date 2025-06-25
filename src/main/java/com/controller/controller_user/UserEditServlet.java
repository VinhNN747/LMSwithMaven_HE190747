/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.controller_user;

import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "UserEditServlet", urlPatterns = {"/user/edit"})
public class UserEditServlet extends UserBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("id");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/list");
            return;
        }

        try {
            Integer userId = Integer.valueOf(userIdStr);
            User existingUser = udb.getById(userId);
            if (existingUser == null) {
                response.sendRedirect(request.getContextPath() + "/user/list");
                return;
            }

            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/list");
        }
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            request.setAttribute("error", "User ID is required");
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }

        try {
            Integer userId = Integer.valueOf(userIdStr);
            User existingUser = udb.getById(userId);
            if (existingUser == null) {
                request.setAttribute("error", "User not found");
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                return;
            }

            String fullName = request.getParameter("fullName");
            String userName = request.getParameter("username");
            String email = request.getParameter("email");
            String genderStr = request.getParameter("gender");

            // Update existing user object instead of creating new one
            existingUser.setFullName(fullName);
            existingUser.setUsername(userName);
            existingUser.setEmail(email);

            // Parse gender
            if (genderStr != null && !genderStr.trim().isEmpty()) {
                existingUser.setGender(Boolean.valueOf(genderStr));
            } else {
                existingUser.setGender(null);
            }

            User originalUser = udb.getById(userId);
            String userEditValidation = userEditValidation(existingUser, originalUser);
            if (userEditValidation != null) {
                request.setAttribute("error", userEditValidation);
                request.setAttribute("user", originalUser);
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                return;
            }

            try {
                udb.edit(existingUser);
                response.sendRedirect(request.getContextPath() + "/user/list");
            } catch (Exception e) {
                request.setAttribute("error", "Failed to update user: " + e.getMessage());
                request.setAttribute("user", originalUser);
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format");
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
        }
    }

    private String userEditValidation(User newUser, User originalUser) {
        if (newUser == null) {
            return "User cannot be null.";
        }
        if (!isUsernameValid(newUser.getUsername())) {
            return "Username is required.";
        }
        if (!isEmailPresent(newUser.getEmail())) {
            return "Email is required.";
        }
        if (!isEmailFormatValid(newUser.getEmail())) {
            return "Invalid email format.";
        }
        if (!isFullNameValid(newUser.getFullName())) {
            return "Full name is required.";
        }

        // Check username uniqueness (excluding current user)
        if (udb.existsByUsername(newUser.getUsername()) && !originalUser.getUsername().equals(newUser.getUsername())) {
            return "Username has already been taken";
        }

        // Check email uniqueness (excluding current user)
        if (udb.existsByEmail(newUser.getEmail()) && !originalUser.getEmail().equals(newUser.getEmail())) {
            return "Email has already been taken";
        }

        return null;
    }
}
