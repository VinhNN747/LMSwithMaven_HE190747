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
//
///**
// * Handles user promotion within the organizational hierarchy:
// * - Employee -> Lead: Basic promotion to management role
// * - Lead -> Head: Promotion to division leadership
// * 
// * The servlet ensures proper role transitions and handles all necessary
// * updates to division structure and management relationships.
// */
//@WebServlet("/user/promote")
//public class UserPromoteServlet extends BaseUserServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        Integer id = Integer.parseInt(request.getParameter("id"));
//        User user = userDao.findById(id);
//
//        if (user == null) {
//            handleError(request, response, "User not found");
//            return;
//        }
//
//        String newRole = determineNewRole(user.getRole());
//        if (newRole == null) {
//            handleError(request, response, "User cannot be promoted further");
//            return;
//        }
//
//        EntityManager em = userDao.getEntityManager();
//        try {
//            executeInTransaction(em, () -> {
//                promoteUser(em, user, newRole);
//            });
//            response.sendRedirect("list");
//        } catch (Exception e) {
//            handleError(request, response, "Failed to promote user: " + e.getMessage());
//        } finally {
//            em.close();
//        }
//    }
//
//    /**
//     * Determines the next role in the hierarchy based on current role:
//     * Employee -> Lead
//     * Lead -> Head
//     * Head -> null (cannot be promoted further)
//     */
//    private String determineNewRole(String currentRole) {
//        switch (currentRole) {
//            case ROLE_EMPLOYEE:
//                return ROLE_LEAD;
//            case ROLE_LEAD:
//                return ROLE_HEAD;
//            default:
//                return null;
//        }
//    }
//
//    /**
//     * Handles the promotion process:
//     * 1. For Head promotion:
//     * - Updates division structure
//     * - Handles existing head
//     * - Updates management relationships
//     * 2. For Lead promotion:
//     * - Sets manager to division head
//     * 3. Updates user's role
//     */
//    private void promoteUser(EntityManager em, User user, String newRole) {
//        if (newRole.equals(ROLE_HEAD)) {
//            Division division = divisionDao.get(user.getDivisionId());
//            handleDivisionHeadChange(em, user, division);
//        } else if (newRole.equals(ROLE_LEAD)) {
//            handleManagerPromotion(em, user);
//        }
//
//        // Update user's role
//        user.setRole(newRole);
//        em.merge(user);
//    }
//
//    /**
//     * Handles promotion to Lead role:
//     * - Sets the user's manager to their division head
//     * - Maintains the management hierarchy
//     */
//    private void handleManagerPromotion(EntityManager em, User user) {
//        // For manager promotion, set manager to division head
//        Division division = divisionDao.get(user.getDivisionId());
//        String headId = division.getDivisionHead();
//        user.setManagerId(headId);
//    }
//}
