//package com.controller.user_controller;
//
//import com.entity.Division;
//import com.entity.User;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet("/user/demote")
//public class UserDemoteServlet extends BaseUserServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String id = request.getParameter("id");
//        User user = userDao.findById(id);
//
//        if (user == null) {
//            handleError(request, response, "User not found");
//            return;
//        }
//
//        String newRole = determineNewRole(user.getRole());
//        if (newRole == null) {
//            handleError(request, response, "User cannot be demoted further");
//            return;
//        }
//
//        EntityManager em = userDao.getEntityManager();
//        EntityTransaction tx = em.getTransaction();
//
//        try {
//            tx.begin();
//            demoteUser(em, user, newRole);
//            tx.commit();
//            response.sendRedirect("list");
//        } catch (Exception e) {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
//            handleError(request, response, "Failed to demote user: " + e.getMessage());
//        } finally {
//            em.close();
//        }
//    }
//
//    private String determineNewRole(String currentRole) {
//        switch (currentRole) {
//            case ROLE_HEAD:
//                return ROLE_LEAD;
//            case ROLE_LEAD:
//                return ROLE_EMPLOYEE;
//            default:
//                return null;
//        }
//    }
//
//    private void demoteUser(EntityManager em, User user, String newRole) {
//        if (user.getRole().equals(ROLE_HEAD)) {
//            handleHeadDemotion(em, user);
//        } else if (user.getRole().equals(ROLE_LEAD)) {
//            handleManagerDemotion(em, user);
//        }
//
//        // Update user's role
//        user.setRole(newRole);
//        em.merge(user);
//    }
//
//    private void handleHeadDemotion(EntityManager em, User user) {
//        Division division = divisionDao.get(user.getDivisionId());
//
//        // Clear division head
//        division.setDivisionHead(null);
//        em.merge(division);
//
//        // Set all managers' managerId to null (they will be managed by the new head)
//        em.createQuery("UPDATE User u SET u.managerId = NULL "
//                + "WHERE u.divisionId = :divisionId "
//                + "AND u.role = :managerRole")
//                .setParameter("divisionId", user.getDivisionId())
//                .setParameter("managerRole", ROLE_LEAD)
//                .executeUpdate();
//
//        // Keep all employees currently managed by this head under their management
//        // The user's role will be changed to manager but their managerId will remain null
//        user.setManagerId(null);
//    }
//
//    private void handleManagerDemotion(EntityManager em, User user) {
//        // For manager demotion, set manager to division head
//        Division division = divisionDao.get(user.getDivisionId());
//        String headId = division.getDivisionHead();
//        user.setManagerId(headId);
//
//        // Update all employees who were managed by this manager to be managed by the head
//        em.createQuery("UPDATE User u SET u.managerId = :headId "
//                + "WHERE u.managerId = :oldManagerId "
//                + "AND u.divisionId = :divisionId ")
//                .setParameter("headId", headId)
//                .setParameter("oldManagerId", user.getUserId())
//                .setParameter("divisionId", user.getDivisionId())
//                .executeUpdate();
//    }
//}
