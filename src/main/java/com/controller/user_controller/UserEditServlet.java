//package com.controller.user_controller;
//
//import com.entity.Division;
//import com.entity.User;
//import jakarta.persistence.EntityManager;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@WebServlet("/user/edit")
//public class UserEditServlet extends BaseUserServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String id = request.getParameter("id");
//        User user = userDao.findById(id);
//
//        if (user == null) {
//            handleError(request, response, "User not found");
//            return;
//        }
//
//        showEditForm(request, response, user);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String userId = request.getParameter("userId");
//        User existingUser = userDao.findById(userId);
//
//        if (existingUser == null) {
//            handleError(request, response, "User not found");
//            return;
//        }
//
//        // Validate input
//        if (!validateInput(request, response, existingUser)) {
//            return;
//        }
//
//        // Process the update
//        EntityManager em = userDao.getEntityManager();
//        try {
//            executeInTransaction(em, () -> {
//                updateUser(em, request, existingUser);
//            });
//            response.sendRedirect("list");
//        } catch (Exception e) {
//            handleError(request, response, "Failed to update user: " + e.getMessage());
//        } finally {
//            em.close();
//        }
//    }
//
//    private void showEditForm(HttpServletRequest request, HttpServletResponse response, User user)
//            throws ServletException, IOException {
//        // Get all divisions for dropdown
//        List<Division> divisions = divisionDao.list();
//        request.setAttribute("divisions", divisions);
//
//        // Get managers and heads from the same division
//        List<User> managers = getManagersInDivision(user);
//        request.setAttribute("managers", managers);
//
//        request.setAttribute("user", user);
//        request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
//    }
//
//    private List<User> getManagersInDivision(User user) {
//        return userDao.list().stream()
//                .filter(u -> (ROLE_LEAD.equals(u.getRole()) || ROLE_HEAD.equals(u.getRole())))
//                .filter(u -> !u.getUserId().equals(user.getUserId())) // Exclude current user
//                .filter(u -> u.getDivisionId().equals(user.getDivisionId())) // Only same division
//                .collect(Collectors.toList());
//    }
//
//    private boolean validateInput(HttpServletRequest request, HttpServletResponse response, User existingUser)
//            throws ServletException, IOException {
//        String fullName = request.getParameter("fullName");
//        String username = request.getParameter("username");
//        String email = request.getParameter("email");
//        String gender = request.getParameter("gender");
//
//        if (!validateFullName(fullName)) {
//            handleValidationError(request, response, existingUser, "Full name is required and must not exceed 100 characters");
//            return false;
//        }
//        if (!validateUsername(username)) {
//            handleValidationError(request, response, existingUser, "Username is required and must not exceed 50 characters");
//            return false;
//        }
//        if (!validateEmail(email)) {
//            handleValidationError(request, response, existingUser, "Valid email is required and must not exceed 100 characters");
//            return false;
//        }
//        if (!validateGender(gender)) {
//            handleValidationError(request, response, existingUser, "Gender must be 'M' or 'F'");
//            return false;
//        }
//        if ((username != null && !username.equals(existingUser.getUsername()))) {
//            if (!validateUniqueUsername(username, existingUser)) {
//                handleValidationError(request, response, existingUser, "Username '" + username + "' already exists");
//                return false;
//            }
//        }
//        if ((email != null && !email.equals(existingUser.getEmail()))) {
//            if (!validateUniqueEmail(email, existingUser)) {
//                handleValidationError(request, response, existingUser, "Email '" + email + "' already exists");
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    private void handleValidationError(HttpServletRequest request, HttpServletResponse response, User user, String errorMessage)
//            throws ServletException, IOException {
//        request.setAttribute("error", errorMessage);
//        request.setAttribute("user", user);
//        request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
//    }
//
//    private void updateUser(EntityManager em, HttpServletRequest request, User existingUser) {
//        String fullName = request.getParameter("fullName");
//        String username = request.getParameter("username");
//        String email = request.getParameter("email");
//        String gender = request.getParameter("gender");
//        String isActiveStr = request.getParameter("isActive");
//        String managerIdStr = request.getParameter("managerId");
//        boolean isActive = Boolean.parseBoolean(isActiveStr);
//
//        // Update user
//        existingUser.setFullName(fullName);
//        existingUser.setUsername(username);
//        existingUser.setEmail(email);
//        existingUser.setGender(gender != null && !gender.isEmpty() ? gender : null);
//        existingUser.setIsActive(isActive);
//
//        // Only update managerId if it's not null
//        if (managerIdStr != null && !managerIdStr.isEmpty()) {
//            existingUser.setManagerId(managerIdStr);
//        }
//
//        em.merge(existingUser);
//    }
//}
