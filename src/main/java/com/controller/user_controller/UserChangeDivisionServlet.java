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
// * Handles user division changes within the organization: - For regular users:
// * Updates division and manager - For leads: Handles their subordinates'
// * management - For heads: Handles division leadership transfer
// *
// * The servlet ensures proper management of organizational structure and
// * maintains the integrity of management relationships.
// */
//@WebServlet("/user/changeDivision")
//public class UserChangeDivisionServlet extends BaseUserServlet {
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
//        showChangeDivisionForm(request, response, user);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String userId = request.getParameter("userId");
//        String newDivisionIdStr = request.getParameter("divisionId");
//
//        User user = userDao.findById(userId);
//        if (user == null) {
//            handleError(request, response, "User not found");
//            return;
//        }
//
//        Integer newDivisionId = parseDivisionId(newDivisionIdStr);
//        if (newDivisionId == null) {
//            handleError(request, response, "Invalid Division ID format");
//            return;
//        }
//
//        Division newDivision = divisionDao.get(newDivisionId);
//        if (newDivision == null) {
//            handleError(request, response, "Division not found");
//            return;
//        }
//
//        EntityManager em = userDao.getEntityManager();
//        try {
//            executeInTransaction(em, () -> {
//                changeUserDivision(em, user, newDivision);
//            });
//            response.sendRedirect("list");
//        } catch (Exception e) {
//            handleError(request, response, "Failed to change division: " + e.getMessage());
//        } finally {
//            em.close();
//        }
//    }
//
//    /**
//     * Displays the division change form with: - Current user information - List
//     * of available divisions
//     */
//    private void showChangeDivisionForm(HttpServletRequest request, HttpServletResponse response, User user)
//            throws ServletException, IOException {
//        request.setAttribute("user", user);
//        request.setAttribute("divisions", divisionDao.list());
//        request.getRequestDispatcher("/view/user/changeDivision.jsp").forward(request, response);
//    }
//
//    /**
//     * Parses and validates the division ID from the request parameter
//     *
//     * @return Integer division ID or null if invalid
//     */
//    private Integer parseDivisionId(String divisionIdStr) {
//        if (divisionIdStr == null || divisionIdStr.trim().isEmpty()) {
//            return null;
//        }
//        try {
//            return Integer.parseInt(divisionIdStr);
//        } catch (NumberFormatException e) {
//            return null;
//        }
//    }
//
//    /**
//     * Handles the division change process:
//     * - For heads: Demotes existing head, updates division, and handles management
//     * - For regular users: Updates division and manager
//     */
//    private void changeUserDivision(EntityManager em, User user, Division newDivision) {
//        // If user is a head, handle division head change
//        if (ROLE_HEAD.equals(user.getRole())) {
//            // First handle the old division
//            Division oldDivision = divisionDao.get(user.getDivisionId());
//            if (oldDivision != null) {
//                // Remove user as head of old division
//                oldDivision.setDivisionHead(null);
//                em.merge(oldDivision);
//
//                // Update users in old division to have no manager
//                em.createQuery("UPDATE User u SET u.managerId = NULL "
//                        + "WHERE u.divisionId = :divisionId "
//                        + "AND u.managerId = :headId")
//                        .setParameter("divisionId", oldDivision.getDivisionId())
//                        .setParameter("headId", user.getUserId())
//                        .executeUpdate();
//            }
//
//            // Then handle the new division
//            // First demote the existing head of the new division if any
//            String existingHeadId = newDivision.getDivisionHead();
//            if (existingHeadId != null) {
//                User existingHead = em.find(User.class, existingHeadId);
//                if (existingHead != null) {
//                    // Demote existing head to lead
//                    existingHead.setRole(ROLE_LEAD);
//                    em.merge(existingHead);
//                }
//            }
//
//            // Update the new division's head
//            newDivision.setDivisionHead(user.getUserId());
//            em.merge(newDivision);
//
//            // Update the moving head's division
//            user.setDivisionId(newDivision.getDivisionId());
//            em.merge(user);
//
//            // Update management relationships in the new division
//            updateDivisionUsers(em, user, newDivision);
//
//            user.setRole(ROLE_HEAD);
//            em.merge(user);
//        } else {
//            // For regular users, just update division and manager
//            handleRegularUserDivisionChange(em, user, newDivision);
//        }
//    }
//
//    /**
//     * Handles division change for regular users and leads: - For leads:
//     * Reassigns their subordinates to division head - Updates user's division
//     * and manager - Maintains management hierarchy
//     */
//    private void handleRegularUserDivisionChange(EntityManager em, User user, Division newDivision) {
//        // If the user is a lead, handle their subordinates first
//        if (ROLE_LEAD.equals(user.getRole())) {
//            // Update any users who were managed by this lead to be managed by the division
//            // head
//            em.createQuery("UPDATE User u SET u.managerId = :headId "
//                    + "WHERE u.divisionId = :divisionId "
//                    + "AND u.managerId = :userId")
//                    .setParameter("headId", user.getManagerId())
//                    .setParameter("divisionId", user.getDivisionId())
//                    .setParameter("userId", user.getUserId())
//                    .executeUpdate();
//            em.flush();
//        }
//
//        // Update the user's division and manager
//        user.setDivisionId(newDivision.getDivisionId());
//        user.setManagerId(newDivision.getDivisionHead());
//        em.merge(user);
//    }
//}
