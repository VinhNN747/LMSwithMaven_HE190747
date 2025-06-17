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
            handleError(request, response, "User not found");
            return;
        }

        showEditForm(request, response, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        User existingUser = userDao.findById(userId);

        if (existingUser == null) {
            handleError(request, response, "User not found");
            return;
        }

        // Validate input
        if (!validateInput(request, response, existingUser)) {
            return;
        }

        // Process the update
        EntityManager em = userDao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            updateUser(em, request, existingUser);
            tx.commit();
            response.sendRedirect("list");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            handleError(request, response, "Failed to update user: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        // Get all divisions for dropdown
        List<Division> divisions = divisionDao.list();
        request.setAttribute("divisions", divisions);

        // Get managers and heads from the same division
        List<User> managers = getManagersInDivision(user);
        request.setAttribute("managers", managers);

        request.setAttribute("user", user);
        request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
    }

    private List<User> getManagersInDivision(User user) {
        return userDao.list().stream()
                .filter(u -> (ROLE_LEAD.equals(u.getRole()) || ROLE_HEAD.equals(u.getRole())))
                .filter(u -> !u.getUserId().equals(user.getUserId())) // Exclude current user
                .filter(u -> u.getDivisionId().equals(user.getDivisionId())) // Only same division
                .collect(Collectors.toList());
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws IOException {
        request.setAttribute("error", errorMessage);
        response.sendRedirect("list");
    }

    private boolean validateInput(HttpServletRequest request, HttpServletResponse response, User existingUser) 
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");

        if (!validateFullName(request, response, existingUser, fullName)) return false;
        if (!validateUsername(request, response, existingUser, username)) return false;
        if (!validateEmail(request, response, existingUser, email)) return false;
        if (!validateGender(request, response, existingUser, gender)) return false;
        if (!validateUniqueUsernameEmail(request, response, existingUser, username, email)) return false;

        return true;
    }

    private boolean validateFullName(HttpServletRequest request, HttpServletResponse response, User existingUser, String fullName) 
            throws ServletException, IOException {
        if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100) {
            handleValidationError(request, response, existingUser, "Full name is required and must not exceed 100 characters");
            return false;
        }
        return true;
    }

    private boolean validateUsername(HttpServletRequest request, HttpServletResponse response, User existingUser, String username) 
            throws ServletException, IOException {
        if (username == null || username.trim().isEmpty() || username.length() > 50) {
            handleValidationError(request, response, existingUser, "Username is required and must not exceed 50 characters");
            return false;
        }
        return true;
    }

    private boolean validateEmail(HttpServletRequest request, HttpServletResponse response, User existingUser, String email) 
            throws ServletException, IOException {
        if (email == null || email.trim().isEmpty() || email.length() > 100
                || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            handleValidationError(request, response, existingUser, "Valid email is required and must not exceed 100 characters");
            return false;
        }
        return true;
    }

    private boolean validateGender(HttpServletRequest request, HttpServletResponse response, User existingUser, String gender) 
            throws ServletException, IOException {
        if (gender != null && !gender.isEmpty() && !gender.matches("^[MF]$")) {
            handleValidationError(request, response, existingUser, "Gender must be 'M' or 'F'");
            return false;
        }
        return true;
    }

    private boolean validateUniqueUsernameEmail(HttpServletRequest request, HttpServletResponse response, User existingUser, 
            String username, String email) throws ServletException, IOException {
        if (userDao.existsByUsernameOrEmail(username, email) 
                && !username.equals(existingUser.getUsername()) 
                && !email.equals(existingUser.getEmail())) {
            handleValidationError(request, response, existingUser, 
                    "Username '" + username + "' or email '" + email + "' already exists");
            return false;
        }
        return true;
    }

    private void handleValidationError(HttpServletRequest request, HttpServletResponse response, User user, String errorMessage) 
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
    }

    private void updateUser(EntityManager em, HttpServletRequest request, User existingUser) {
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String isActiveStr = request.getParameter("isActive");
        String managerIdStr = request.getParameter("managerId");
        boolean isActive = Boolean.parseBoolean(isActiveStr);

        // Update user
        existingUser.setFullName(fullName);
        existingUser.setUsername(username);
        existingUser.setEmail(email);
        existingUser.setGender(gender != null && !gender.isEmpty() ? gender : null);
        existingUser.setIsActive(isActive);
        
        // Only update managerId if it's not null
        if (managerIdStr != null && !managerIdStr.isEmpty()) {
            existingUser.setManagerId(managerIdStr);
        }
        
        em.merge(existingUser);
    }
}
