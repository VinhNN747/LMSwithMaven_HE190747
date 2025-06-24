//package com.controller.user_controller;
//
//import com.dao.DivisionDao;
//import com.dao.UserDao;
//import com.entity.Division;
//import com.entity.User;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Base servlet class that provides common functionality for all user-related
// * operations. This includes: - Role management and validation - User ID
// * generation - Input validation - Transaction handling - Division management -
// * Error handling
// */
//public abstract class BaseUserServlet extends HttpServlet {
//
//    protected UserDao userDao;
//    protected DivisionDao divisionDao;
//
//    @Override
//    public void init() throws ServletException {
//        userDao = new UserDao();
//        divisionDao = new DivisionDao();
//    }
//
//    /**
//     * Generates a unique user ID based on the user's full name. Format: [First
//     * Letter of Each Name][3-digit sequence number] Example: "John Doe" ->
//     * "JD001"
//     */
//    protected String setNewId(String fullName) {
//        // Build acronym from FullName (e.g., John Doe -> JD)
//        StringBuilder acronym = new StringBuilder();
//        String[] nameParts = fullName.trim().split("\\s+");
//        for (String part : nameParts) {
//            if (!part.isEmpty()) {
//                acronym.append(part.charAt(0));
//            }
//        }
//
//        // Get max index for users with the same acronym
//        int index = userDao.getMaxIndexForAcronym(acronym.toString().toUpperCase()) + 1;
//
//        // Format ID like JD001
//        return acronym.toString().toUpperCase() + String.format("%03d", index);
//    }
//
//    /**
//     * Standardized error handling that redirects to the user list with an error
//     * message
//     */
//    protected void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
//            throws IOException {
//        request.setAttribute("error", errorMessage);
//        response.sendRedirect("list");
//    }
//
//    // Common validation methods for user input
//    /**
//     * Validates full name: - Must not be null or empty - Must not exceed 100
//     * characters
//     */
//    protected boolean validateFullName(String fullName) {
//        return fullName != null && !fullName.trim().isEmpty() && fullName.length() <= 100;
//    }
//
//    /**
//     * Validates username: - Must not be null or empty - Must not exceed 50
//     * characters
//     */
//    protected boolean validateUsername(String username) {
//        return username != null && !username.trim().isEmpty() && username.length() <= 50;
//    }
//
//    /**
//     * Validates email: - Must not be null or empty - Must not exceed 100
//     * characters - Must match standard email format
//     */
//    protected boolean validateEmail(String email) {
//        return email != null && !email.trim().isEmpty() && email.length() <= 100
//                && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
//    }
//
//    /**
//     * Validates gender: - Must be null, empty, 'M', or 'F'
//     */
//    protected boolean validateGender(String gender) {
//        return gender == null || gender.isEmpty() || gender.matches("^[MF]$");
//    }
//
//    /**
//     * Validates username and email uniqueness: - For new users: must not exist
//     * in database - For existing users: must not conflict with other users
//     */
//
//    protected boolean validateUniqueUsername(String username, User existingUser) {
//        if (existingUser != null) {
//            return !userDao.existsByUsername(username) || username.equals(existingUser.getUsername());
//        }
//        return !userDao.existsByUsername(username);
//    }
//
//    protected boolean validateUniqueEmail(String email, User existingUser) {
//        if (existingUser != null) {
//            return !userDao.existsByEmail(email) || email.equals(existingUser.getEmail());
//        }
//        return !userDao.existsByEmail(email);
//    }
//
//    /**
//     * Wrapper for transaction handling that ensures proper commit/rollback
//     *
//     * @param em     EntityManager instance
//     * @param action The business logic to execute within the transaction
//     */
//    protected void executeInTransaction(EntityManager em, Runnable action) throws Exception {
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//            action.run();
//            tx.commit();
//        } catch (Exception e) {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
//            throw e;
//        }
//    }
//
//    // Division management methods
//    /**
//     * Handles the process of changing a division head: 1. Handles the old head
//     * (if exists) 2. Updates division's head 3. Reassigns users to new head 4.
//     * Updates new head's manager
//     */
//    protected void handleDivisionHeadChange(EntityManager em, User newHead, Division division) {
//        String oldHeadId = division.getDivisionHead();
//
//        if (oldHeadId != null) {
//            handleExistingHead(em, oldHeadId, newHead.getUserId());
//        }
//
//        // Update division's head
//        division.setDivisionHead(newHead.getUserId());
//        em.merge(division);
//
//        // Reassign all users in division to be managed by new head
//        updateDivisionUsers(em, newHead, division);
//
//        // Set new head's manager to null
//        newHead.setManagerId(null);
//    }
//
//    /**
//     * Handles the existing head when a new head is being appointed: - Demotes
//     * old head to lead role - Sets old head's manager to new head
//     */
//    protected void handleExistingHead(EntityManager em, String oldHeadId, String newHeadId) {
//        User oldHead = userDao.findById(oldHeadId);
//        if (oldHead != null && !oldHead.getUserId().equals(newHeadId)) {
//            // Demote old head to manager
//            oldHead.setRole(ROLE_LEAD);
//            oldHead.setManagerId(newHeadId);
//            em.merge(oldHead);
//        }
//    }
//
//    /**
//     * Updates users in a division when the head changes: - Users with no
//     * manager or managed by old head are assigned to new head - Excludes the
//     * new head from being managed - Excludes other heads from being managed
//     */
//    protected void updateDivisionUsers(EntityManager em, User newHead, Division division) {
//        String oldHeadId = division.getDivisionHead();
//        em.createQuery("UPDATE User u SET u.managerId = :newHeadId "
//                + "WHERE u.divisionId = :divisionId "
//                + "AND u.userId != :newHeadId "
//                + "AND u.role != :headRole "
//                + "AND (u.managerId IS NULL OR u.managerId = :oldHeadId OR u.role = :leadRole)")
//                .setParameter("newHeadId", newHead.getUserId())
//                .setParameter("divisionId", division.getDivisionId())
//                .setParameter("headRole", ROLE_HEAD)
//                .setParameter("leadRole", ROLE_LEAD)
//                .setParameter("oldHeadId", oldHeadId)
//                .executeUpdate();
//    }
//}
