/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.controller_user;

import com.dao.RoleDao;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "UserEditServlet", urlPatterns = {"/user/edit"})
public class UserEditServlet extends UserBaseServlet {

    private RoleDao roleDao = new RoleDao();

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("id");

        Integer userId = Integer.valueOf(userIdStr);
        User existingUser = udb.get(userId);

        // Get potential managers based on hierarchy rules
        List<User> potentialManagers = getPotentialManagers(existingUser);

        request.setAttribute("user", existingUser);
        request.setAttribute("potentialManagers", potentialManagers);
        request.getRequestDispatcher("../view/user/edit.jsp").forward(request, response);

    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("userId");

        Integer userId = Integer.valueOf(userIdStr);
        User targetUser = udb.get(userId);

        String fullName = request.getParameter("fullName");
        String userName = request.getParameter("username");
        String email = request.getParameter("email");
        String genderStr = request.getParameter("gender");
        String managerIdStr = request.getParameter("managerId");

        targetUser.setFullName(fullName);
        targetUser.setUsername(userName);
        targetUser.setEmail(email);

        // Parse gender
        if (genderStr != null && !genderStr.trim().isEmpty()) {
            targetUser.setGender(Boolean.valueOf(genderStr));
        } else {
            targetUser.setGender(null);
        }

        // Parse and set manager with validation
        if (managerIdStr != null && !managerIdStr.trim().isEmpty()) {
            Integer managerId = Integer.valueOf(managerIdStr);
            targetUser.setManagerId(managerId);
        } else {
            targetUser.setManagerId(null); // No manager assigned
        }

        User originalUser = udb.get(userId);
        String userEditValidation = userEditValidation(targetUser, originalUser);
        if (userEditValidation != null) {
            request.setAttribute("error", userEditValidation);
            request.setAttribute("user", originalUser);
            request.setAttribute("potentialManagers", getPotentialManagers(originalUser));
            request.getRequestDispatcher("../view/user/edit.jsp").forward(request, response);
            return;
        }

        udb.edit(targetUser);
        response.sendRedirect("list");

    }

    /**
     * Get potential managers based on hierarchy rules: 1. Must be in the same
     * division 2. Must have higher role level than the current user 3. Cannot
     * be the same user
     */
    private List<User> getPotentialManagers(User currentUser) {
        List<User> allUsers = udb.list();

        return allUsers.stream()
                .filter(u -> !u.getUserId().equals(currentUser.getUserId())) // Not the same user
                .filter(u -> u.getDivisionId() != null && currentUser.getDivisionId() != null
                && u.getDivisionId().equals(currentUser.getDivisionId())) // Same division
                .filter(u -> hasHigherRoleLevel(u, currentUser)) // Higher role level
                .collect(Collectors.toList());
    }

    /**
     * Check if a user has higher role level than another user
     */
    private boolean hasHigherRoleLevel(User potentialManager, User currentUser) {
        // Get role levels for both users (each user has only 1 role)
        Integer managerRoleLevel = potentialManager.getRole().getRoleLevel();
        Integer userRoleLevel = currentUser.getRole().getRoleLevel();

        // If either user has no role level, they can't be compared
        if (managerRoleLevel == null || userRoleLevel == null) {
            return false;
        }

        return managerRoleLevel > userRoleLevel;
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
