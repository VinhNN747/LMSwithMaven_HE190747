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
            
            // Handle role changes and division changes
            if (!role.equals(existingUser.getRole()) || !divisionId.equals(existingUser.getDivisionId())) {
                if (role.equals(ROLE_DIRECTOR)) {
                    // Promoting to Director or moving to new division as director
                    try {
                        // Step 1: Handle current director in new division
                        if (division.getDivisionDirector() != null) {
                            User currentDirector = userDao.findById(division.getDivisionDirector());
                            if (currentDirector != null && !currentDirector.getUserId().equals(userId)) {
                                // Demote current director to manager
                                currentDirector.setRole(ROLE_MANAGER);
                                em.merge(currentDirector);
                            }
                        }

                        // Step 2: Update division's director
                        division.setDivisionDirector(userId);
                        em.merge(division);

                        // Step 3: Update all users in new division to be managed by new director
                        em.createQuery("UPDATE User u SET u.managerId = :newDirectorId " +
                                "WHERE u.divisionId = :divisionId " +
                                "AND u.userId != :newDirectorId")
                                .setParameter("newDirectorId", userId)
                                .setParameter("divisionId", division.getDivisionId())
                                .executeUpdate();

                        // Step 4: Set new director's manager to null
                        managerId = null;

                    } catch (Exception e) {
                        System.err.println("Error in director promotion: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to promote user to director: " + e.getMessage());
                    }
                } else if (role.equals(ROLE_EMPLOYEE)) {
                    // If becoming an employee, use the provided manager ID or default to division director
                    managerId = request.getParameter("managerId");
                    if (managerId == null || managerId.trim().isEmpty()) {
                        managerId = division.getDivisionDirector();
                    }
                } else {
                    // For any other role or division change, set manager to new division's director
                    managerId = division.getDivisionDirector();
                }
            } else {
                // No role or division change, just update manager based on role
                if (role.equals(ROLE_DIRECTOR)) {
                    managerId = null; // Directors have no manager
                } else if (role.equals(ROLE_EMPLOYEE)) {
                    // For employees, use the provided manager ID or default to division director
                    managerId = request.getParameter("managerId");
                    if (managerId == null || managerId.trim().isEmpty()) {
                        managerId = division.getDivisionDirector();
                    }
                } else {
                    managerId = division.getDivisionDirector(); // Managers are managed by director
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