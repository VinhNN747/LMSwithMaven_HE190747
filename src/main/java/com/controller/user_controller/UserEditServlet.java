package com.controller.user_controller;

import com.entity.Division;
import com.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/user/edit")
public class UserEditServlet extends BaseUserServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        User user = userDao.findById(id);
        if (user == null) {
            request.setAttribute("error", "User not found");
            response.sendRedirect("list");
        } else {
            // Get all divisions for dropdown
            List<Division> divisions = divisionDao.list();
            request.setAttribute("divisions", divisions);

            // Get managers and directors from the same division
            List<User> managers = userDao.list().stream()
                    .filter(u -> (ROLE_MANAGER.equals(u.getRole()) || ROLE_DIRECTOR.equals(u.getRole())))
                    .filter(u -> !u.getUserId().equals(id)) // Exclude current user
                    .filter(u -> u.getDivisionId().equals(user.getDivisionId())) // Only same division
                    .collect(Collectors.toList());
            request.setAttribute("managers", managers);

            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String divisionIdStr = request.getParameter("divisionId");
        String role = request.getParameter("role");
        String isActiveStr = request.getParameter("isActive");
        String managerId = null;
        User existingUser = userDao.findById(userId);

        // Basic validation
        if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100) {
            request.setAttribute("error", "Full name is required and must not exceed 100 characters");
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }
        if (username == null || username.trim().isEmpty() || username.length() > 50) {
            request.setAttribute("error", "Username is required and must not exceed 50 characters");
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }
        if (email == null || email.trim().isEmpty() || email.length() > 100
                || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            request.setAttribute("error", "Valid email is required and must not exceed 100 characters");
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }
        if (gender != null && !gender.isEmpty() && !gender.matches("^[MF]$")) {
            request.setAttribute("error", "Gender must be 'M' or 'F'");
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }

        Integer divisionId = null;
        if (divisionIdStr != null && !divisionIdStr.trim().isEmpty()) {
            try {
                divisionId = Integer.parseInt(divisionIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid Division ID format");
                request.setAttribute("user", existingUser);
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                return;
            }
        }

        if (role == null || role.trim().isEmpty()) {
            request.setAttribute("error", "Role is required");
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }

        // Check if role change is valid (only one level)
        if (!role.equals(existingUser.getRole()) && !isValidRoleTransition(existingUser.getRole(), role)) {
            request.setAttribute("error",
                    "Invalid role change. You can only promote or demote by one level at a time.");
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }

        // Check for duplicate username/email, excluding current user
        if (userDao.existsByUsernameOrEmail(username, email) && 
            !username.equals(existingUser.getUsername()) && 
            !email.equals(existingUser.getEmail())) {
            request.setAttribute("error", "Username '" + username + "' or email '" + email + "' already exists");
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }

        boolean isActive = Boolean.parseBoolean(isActiveStr);
        EntityManager em = userDao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            // Get current division
            Division division = divisionDao.get(divisionId);
            
            // Handle role changes
            if (!role.equals(existingUser.getRole())) {
                if (role.equals(ROLE_DIRECTOR)) {
                    // Promoting to Director
                    try {
                        // Step 1: Handle current director
                        if (division.getDivisionDirector() != null) {
                            User currentDirector = userDao.findById(division.getDivisionDirector());
                            if (currentDirector != null && !currentDirector.getUserId().equals(userId)) {
                                // Demote current director to manager
                                currentDirector.setRole(ROLE_MANAGER);
                                currentDirector.setManagerId(userId);
                                em.merge(currentDirector);
                            }
                        }

                        // Step 2: Update division's director
                        division.setDivisionDirector(userId);
                        em.merge(division);

                        // Step 3: Update manager references
                        em.createQuery("UPDATE User u SET u.managerId = :newDirectorId " +
                                "WHERE u.divisionId = :divisionId " +
                                "AND u.userId != :newDirectorId " +
                                "AND u.role != :directorRole")
                                .setParameter("newDirectorId", userId)
                                .setParameter("divisionId", division.getDivisionId())
                                .setParameter("directorRole", ROLE_DIRECTOR)
                                .executeUpdate();

                        // Step 4: Set new director's manager to null
                        managerId = null;

                    } catch (Exception e) {
                        System.err.println("Error in director promotion: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to promote user to director: " + e.getMessage());
                    }
                    
                } else if (role.equals(ROLE_MANAGER)) {
                    // Promoting to Manager or Demoting to Manager
                    if (existingUser.getRole().equals(ROLE_DIRECTOR)) {
                        // Demoting from Director to Manager
                        // Set all users previously managed by this director to have no manager
                        em.createQuery("UPDATE User u SET u.managerId = NULL " +
                                "WHERE u.managerId = :oldDirectorId")
                                .setParameter("oldDirectorId", userId)
                                .executeUpdate();
                        
                        // Set the demoted director to be managed by the division's director
                        String newDirectorId = division.getDivisionDirector();
                        if (newDirectorId != null && !newDirectorId.equals(userId)) {
                            managerId = newDirectorId;
                        } else {
                            managerId = null; // If no new director or self-reference, set to null
                        }
                    } else {
                        managerId = division.getDivisionDirector(); // Regular manager is managed by director
                    }
                    
                } else if (role.equals(ROLE_EMPLOYEE)) {
                    // Demoting to Employee
                    if (existingUser.getRole().equals(ROLE_DIRECTOR)) {
                        // Demoting from Director to Employee
                        // Get the new director (if any)
                        String newDirectorId = division.getDivisionDirector();
                        
                        // Update all users previously managed by this user to be managed by the new director
                        em.createQuery("UPDATE User u SET u.managerId = :newDirectorId " +
                                "WHERE u.managerId = :oldDirectorId")
                                .setParameter("newDirectorId", newDirectorId)
                                .setParameter("oldDirectorId", userId)
                                .executeUpdate();
                    } else if (existingUser.getRole().equals(ROLE_MANAGER)) {
                        // Demoting from Manager to Employee
                        // Update all users previously managed by this user to be managed by the director
                        em.createQuery("UPDATE User u SET u.managerId = :directorId " +
                                "WHERE u.managerId = :oldManagerId")
                                .setParameter("directorId", division.getDivisionDirector())
                                .setParameter("oldManagerId", userId)
                                .executeUpdate();
                    }
                    managerId = division.getDivisionDirector(); // Employee is managed by director
                }
            } else {
                // No role change, just update manager based on current role
                if (role.equals(ROLE_EMPLOYEE)) {
                    managerId = request.getParameter("managerId");
                    if (managerId == null || managerId.trim().isEmpty()) {
                        managerId = division.getDivisionDirector();
                    }
                } else if (role.equals(ROLE_MANAGER)) {
                    managerId = division.getDivisionDirector();
                } else if (role.equals(ROLE_DIRECTOR)) {
                    managerId = null;
                }
            }

            // Update user
            existingUser.setFullName(fullName);
            existingUser.setUsername(username);
            existingUser.setEmail(email);
            existingUser.setGender(gender != null && !gender.isEmpty() ? gender : null);
            existingUser.setDivisionId(divisionId);
            existingUser.setRole(role);
            existingUser.setIsActive(isActive);
            existingUser.setManagerId(managerId);
            
            em.merge(existingUser);
            tx.commit();
            
            response.sendRedirect("list");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            request.setAttribute("error", "Failed to update user: " + e.getMessage());
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
        } finally {
            em.close();
        }
    }
} 