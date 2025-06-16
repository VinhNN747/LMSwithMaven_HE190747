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
                return ROLE_MANAGER;
            case ROLE_MANAGER:
                return ROLE_DIRECTOR;
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
        if (newRole.equals(ROLE_DIRECTOR)) {
            handleDirectorPromotion(em, user);
        } else if (newRole.equals(ROLE_MANAGER)) {
            handleManagerPromotion(em, user);
        }
        
        // Update user's role
        user.setRole(newRole);
        em.merge(user);
    }

    private void handleDirectorPromotion(EntityManager em, User user) {
        Division division = divisionDao.get(user.getDivisionId());
        String oldDirectorId = division.getDivisionDirector();
        
        if (oldDirectorId != null) {
            handleExistingDirector(em, oldDirectorId, user.getUserId());
        }

        // Update division's director
        division.setDivisionDirector(user.getUserId());
        em.merge(division);

        // Reassign all users in division to be managed by new director
        updateDivisionUsers(em, user, division);

        // Set new director's manager to null
        user.setManagerId(null);
    }

    private void handleExistingDirector(EntityManager em, String oldDirectorId, String newDirectorId) {
        User oldDirector = userDao.findById(oldDirectorId);
        if (oldDirector != null && !oldDirector.getUserId().equals(newDirectorId)) {
            // Demote old director to manager
            oldDirector.setRole(ROLE_MANAGER);
            oldDirector.setManagerId(newDirectorId);
            em.merge(oldDirector);
        }
    }

    private void updateDivisionUsers(EntityManager em, User newDirector, Division division) {
        em.createQuery("UPDATE User u SET u.managerId = :newDirectorId " +
                "WHERE u.divisionId = :divisionId " +
                "AND u.userId != :newDirectorId " +
                "AND u.role != :directorRole")
                .setParameter("newDirectorId", newDirector.getUserId())
                .setParameter("divisionId", division.getDivisionId())
                .setParameter("directorRole", ROLE_DIRECTOR)
                .executeUpdate();
    }

    private void handleManagerPromotion(EntityManager em, User user) {
        // For manager promotion, set manager to division director
        Division division = divisionDao.get(user.getDivisionId());
        String directorId = division.getDivisionDirector();
        user.setManagerId(directorId);
    }
} 