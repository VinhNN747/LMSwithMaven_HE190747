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

@WebServlet("/user/promote")
public class UserPromoteServlet extends BaseUserServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        User user = userDao.findById(id);

        if (user == null) {
            handleError(request, response, "User not found");
            return;
        }

        String newRole = determineNewRole(user.getRole());
        if (newRole == null) {
            handleError(request, response, "User cannot be promoted further");
            return;
        }

        EntityManager em = userDao.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            promoteUser(em, user, newRole);
            tx.commit();
            response.sendRedirect("list");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            handleError(request, response, "Failed to promote user: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private String determineNewRole(String currentRole) {
        switch (currentRole) {
            case ROLE_EMPLOYEE:
                return ROLE_LEAD;
            case ROLE_LEAD:
                return ROLE_HEAD;
            default:
                return null;
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws IOException {
        request.setAttribute("error", errorMessage);
        response.sendRedirect("list");
    }

    private void promoteUser(EntityManager em, User user, String newRole) {
        if (newRole.equals(ROLE_HEAD)) {
            handleHeadPromotion(em, user);
        } else if (newRole.equals(ROLE_LEAD)) {
            handleManagerPromotion(em, user);
        }

        // Update user's role
        System.out.println("set role");
        user.setRole(newRole);
        System.out.println("successfully");
        em.merge(user);
    }

    private void handleHeadPromotion(EntityManager em, User user) {
        Division division = divisionDao.get(user.getDivisionId());
        String oldHeadId = division.getDivisionHead();

        if (oldHeadId != null) {
            handleExistingHead(em, oldHeadId, user.getUserId());
        }

        // Reassign all users in division to be managed by new head
        updateDivisionUsers(em, user, division);
        // Update division's head
        division.setDivisionHead(user.getUserId());
        em.merge(division);
        // Set new head's manager to null
        user.setManagerId(null);
    }

    private void handleExistingHead(EntityManager em, String oldHeadId, String newHeadId) {
        User oldHead = userDao.findById(oldHeadId);
        if (oldHead != null && !oldHead.getUserId().equals(newHeadId)) {
            // Demote old head to manager
            oldHead.setRole(ROLE_LEAD);
            oldHead.setManagerId(newHeadId);
            em.merge(oldHead);
        }
    }

    private void updateDivisionUsers(EntityManager em, User newHead, Division division) {
        String oldHeadId = division.getDivisionHead();
        em.createQuery("UPDATE User u SET u.managerId = :newHeadId "
                + "WHERE u.divisionId = :divisionId "
                + "AND u.userId != :newHeadId "
                + "AND u.role != :headRole "
                + "AND (u.managerId IS NULL OR u.managerId = :oldHeadId)")
                .setParameter("newHeadId", newHead.getUserId())
                .setParameter("divisionId", division.getDivisionId())
                .setParameter("headRole", ROLE_HEAD)
                .setParameter("oldHeadId", oldHeadId)
                .executeUpdate();
    }

    private void handleManagerPromotion(EntityManager em, User user) {
        // For manager promotion, set manager to division head
        Division division = divisionDao.get(user.getDivisionId());
        String headId = division.getDivisionHead();
        user.setManagerId(headId);
    }
}
