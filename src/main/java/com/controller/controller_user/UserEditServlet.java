/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.controller_user;

import com.dao.RoleDao;
import com.entity.User;
import com.entity.Role;
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

            // Get potential managers based on hierarchy rules
            List<User> potentialManagers = getPotentialManagers(existingUser);

            request.setAttribute("user", existingUser);
            request.setAttribute("potentialManagers", potentialManagers);
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
            String managerIdStr = request.getParameter("managerId");

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

            // Parse and set manager with validation
            if (managerIdStr != null && !managerIdStr.trim().isEmpty()) {
                try {
                    Integer managerId = Integer.valueOf(managerIdStr);
                    
                    // Validate manager selection
                    String managerValidation = validateManagerSelection(existingUser, managerId);
                    if (managerValidation != null) {
                        request.setAttribute("error", managerValidation);
                        request.setAttribute("user", existingUser);
                        request.setAttribute("potentialManagers", getPotentialManagers(existingUser));
                        request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                        return;
                    }
                    
                    existingUser.setManagerId(managerId);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid manager ID format");
                    request.setAttribute("user", existingUser);
                    request.setAttribute("potentialManagers", getPotentialManagers(existingUser));
                    request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                    return;
                }
            } else {
                existingUser.setManagerId(null); // No manager assigned
            }

            User originalUser = udb.getById(userId);
            String userEditValidation = userEditValidation(existingUser, originalUser);
            if (userEditValidation != null) {
                request.setAttribute("error", userEditValidation);
                request.setAttribute("user", originalUser);
                request.setAttribute("potentialManagers", getPotentialManagers(originalUser));
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                return;
            }

            try {
                udb.edit(existingUser);
                response.sendRedirect(request.getContextPath() + "/user/list");
            } catch (Exception e) {
                request.setAttribute("error", "Failed to update user: " + e.getMessage());
                request.setAttribute("user", originalUser);
                request.setAttribute("potentialManagers", getPotentialManagers(originalUser));
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format");
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
        }
    }

    /**
     * Get potential managers based on hierarchy rules:
     * 1. Must be in the same division
     * 2. Must have higher role level than the current user
     * 3. Cannot be the same user
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
        Integer managerRoleLevel = getRoleLevel(potentialManager);
        Integer userRoleLevel = getRoleLevel(currentUser);
        
        // If either user has no role level, they can't be compared
        if (managerRoleLevel == null || userRoleLevel == null) {
            return false;
        }
        
        return managerRoleLevel > userRoleLevel;
    }

    /**
     * Get the role level for a user (single role)
     */
    private Integer getRoleLevel(User user) {
        // Use DAO method instead of direct relationship to avoid lazy loading issues
        List<Role> userRoles = udb.getUserRoles(user.getUserId());
        
        // Since each user has only 1 role, get the first (and only) role
        if (userRoles.isEmpty()) {
            return null;
        }
        
        return userRoles.get(0).getRoleLevel();
    }

    /**
     * Validate manager selection
     */
    private String validateManagerSelection(User currentUser, Integer managerId) {
        // Check if user is trying to be their own manager
        if (managerId.equals(currentUser.getUserId())) {
            return "User cannot be their own manager";
        }
        
        // Check if manager exists
        User manager = udb.getById(managerId);
        if (manager == null) {
            return "Selected manager does not exist";
        }
        
        // Check if manager is in the same division
        if (currentUser.getDivisionId() == null || manager.getDivisionId() == null 
                || !currentUser.getDivisionId().equals(manager.getDivisionId())) {
            return "Manager must be in the same division";
        }
        
        // Check if manager has higher role level
        if (!hasHigherRoleLevel(manager, currentUser)) {
            return "Manager must have a higher role level";
        }
        
        return null;
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
