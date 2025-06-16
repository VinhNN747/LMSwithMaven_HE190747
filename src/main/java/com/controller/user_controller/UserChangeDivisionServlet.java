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

@WebServlet("/user/changeDivision")
public class UserChangeDivisionServlet extends BaseUserServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        User user = userDao.findById(id);
        
        if (user == null) {
            handleError(request, response, "User not found");
            return;
        }

        showDivisionChangeForm(request, response, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String newDivisionIdStr = request.getParameter("divisionId");
        User user = userDao.findById(id);
        
        if (user == null) {
            handleError(request, response, "User not found");
            return;
        }

        Integer newDivisionId = parseDivisionId(newDivisionIdStr);
        if (newDivisionId == null) {
            handleError(request, response, "Invalid Division ID format");
            return;
        }

        if (newDivisionId.equals(user.getDivisionId())) {
            response.sendRedirect("list");
            return;
        }

        EntityManager em = userDao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            changeUserDivision(em, user, newDivisionId);
            tx.commit();
            response.sendRedirect("list");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            handleError(request, response, "Failed to change division: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private void showDivisionChangeForm(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        List<Division> divisions = divisionDao.list();
        request.setAttribute("divisions", divisions);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/view/user/changeDivision.jsp").forward(request, response);
    }

    private Integer parseDivisionId(String divisionIdStr) {
        if (divisionIdStr == null || divisionIdStr.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(divisionIdStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws IOException {
        request.setAttribute("error", errorMessage);
        response.sendRedirect("list");
    }

    private void changeUserDivision(EntityManager em, User user, Integer newDivisionId) {
        Division oldDivision = divisionDao.get(user.getDivisionId());
        Division newDivision = divisionDao.get(newDivisionId);
        
        if (user.getRole().equals(ROLE_DIRECTOR)) {
            handleDirectorDivisionChange(em, user, oldDivision, newDivision);
        } else {
            handleRegularUserDivisionChange(em, user, newDivision);
        }
    }

    private void handleDirectorDivisionChange(EntityManager em, User user, Division oldDivision, Division newDivision) {
        // Handle old division
        clearOldDivisionDirector(em, oldDivision);
        removeOldDivisionManagerAssignments(em, oldDivision);

        // Handle new division
        handleNewDivisionDirector(em, user, newDivision);
        updateNewDivisionUsers(em, user, newDivision);

        // Update director's manager
        user.setManagerId(null);
    }

    private void clearOldDivisionDirector(EntityManager em, Division oldDivision) {
        // Get the old director
        String oldDirectorId = oldDivision.getDivisionDirector();
        if (oldDirectorId != null) {
            User oldDirector = userDao.findById(oldDirectorId);
            if (oldDirector != null) {
                // Demote old director to manager
                oldDirector.setRole(ROLE_MANAGER);
                em.merge(oldDirector);

                // Update all employees who were managed by the old director to be managed by the new director
                em.createQuery("UPDATE User u SET u.managerId = :newDirectorId " +
                        "WHERE u.managerId = :oldDirectorId " +
                        "AND u.divisionId = :divisionId")
                        .setParameter("newDirectorId", oldDirectorId)
                        .setParameter("oldDirectorId", oldDirectorId)
                        .setParameter("divisionId", oldDivision.getDivisionId())
                        .executeUpdate();
            }
        }
        
        oldDivision.setDivisionDirector(null);
        em.merge(oldDivision);
    }

    private void removeOldDivisionManagerAssignments(EntityManager em, Division oldDivision) {
        em.createQuery("UPDATE User u SET u.managerId = NULL " +
                "WHERE u.divisionId = :divisionId " +
                "AND u.role != :directorRole")
                .setParameter("divisionId", oldDivision.getDivisionId())
                .setParameter("directorRole", ROLE_DIRECTOR)
                .executeUpdate();
    }

    private void handleNewDivisionDirector(EntityManager em, User user, Division newDivision) {
        String oldDirectorId = newDivision.getDivisionDirector();
        if (oldDirectorId != null) {
            User oldDirector = userDao.findById(oldDirectorId);
            if (oldDirector != null && !oldDirector.getUserId().equals(user.getUserId())) {
                demoteOldDirector(em, oldDirector, user);
            }
        }
        newDivision.setDivisionDirector(user.getUserId());
        em.merge(newDivision);
    }

    private void demoteOldDirector(EntityManager em, User oldDirector, User newDirector) {
        oldDirector.setRole(ROLE_MANAGER);
        oldDirector.setManagerId(newDirector.getUserId());
        em.merge(oldDirector);
    }

    private void updateNewDivisionUsers(EntityManager em, User newDirector, Division newDivision) {
        em.createQuery("UPDATE User u SET u.managerId = :newDirectorId " +
                "WHERE u.divisionId = :divisionId " +
                "AND u.userId != :newDirectorId " +
                "AND u.role != :directorRole")
                .setParameter("newDirectorId", newDirector.getUserId())
                .setParameter("divisionId", newDivision.getDivisionId())
                .setParameter("directorRole", ROLE_DIRECTOR)
                .executeUpdate();
    }

    private void handleRegularUserDivisionChange(EntityManager em, User user, Division newDivision) {
        user.setManagerId(newDivision.getDivisionDirector());
        user.setDivisionId(newDivision.getDivisionId());
        em.merge(user);
    }
} 