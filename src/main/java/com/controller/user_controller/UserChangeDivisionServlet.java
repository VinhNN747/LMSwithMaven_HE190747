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
        try {
            removeOldDivisionDirector(em, oldDivision);
            handleNewDivisionDirector(em, user, newDivision);
            setNewDivisionDirector(em, user, newDivision);
        } catch (Exception e) {
            throw e;
        }
    }

    private void removeOldDivisionDirector(EntityManager em, Division oldDivision) {
        String oldDivDirectorId = oldDivision.getDivisionDirector();
        if (oldDivDirectorId != null) {
            em.createQuery("UPDATE User u SET u.managerId = NULL "
                    + "WHERE u.divisionId = :divisionId "
                    + "AND u.managerId = :directorId")
                    .setParameter("divisionId", oldDivision.getDivisionId())
                    .setParameter("directorId", oldDivDirectorId)
                    .executeUpdate();
            em.flush();
        }
        oldDivision.setDivisionDirector(null);
        em.merge(oldDivision);
        em.flush();
    }

    private void handleNewDivisionDirector(EntityManager em, User user, Division newDivision) {
        String newDivDirectorId = newDivision.getDivisionDirector();
        if (newDivDirectorId == null) {
            return;
        }

        try {
            User oldDirector = em.find(User.class, newDivDirectorId);
            if (oldDirector == null || oldDirector.getUserId().equals(user.getUserId())) {
                return;
            }
            demoteOldDirector(em, oldDirector, newDivision);
        } catch (Exception e) {
            throw e;
        }
    }

    private void demoteOldDirector(EntityManager em, User oldDirector, Division newDivision) {
        oldDirector.setRole(ROLE_MANAGER);
        oldDirector.setManagerId(null);
        em.merge(oldDirector);
        em.flush();

        em.createQuery("UPDATE User u SET u.managerId = NULL "
                + "WHERE u.divisionId = :divisionId "
                + "AND u.role = :managerRole")
                .setParameter("divisionId", newDivision.getDivisionId())
                .setParameter("managerRole", ROLE_MANAGER)
                .executeUpdate();
        em.flush();
    }

    private void setNewDivisionDirector(EntityManager em, User user, Division newDivision) {
        // Set new division director
        newDivision.setDivisionDirector(user.getUserId());
        em.merge(newDivision);
        em.flush();

        // Update director's information
        user.setManagerId(null);
        user.setDivisionId(newDivision.getDivisionId());
        em.merge(user);
        em.flush();

        // Set all users in the division who don't have a manager to be managed by the new director
        em.createQuery("UPDATE User u SET u.managerId = :directorId "
                + "WHERE u.divisionId = :divisionId "
                + "AND u.managerId IS NULL "
                + "AND u.userId != :directorId")  // Don't set the director as their own manager
                .setParameter("directorId", user.getUserId())
                .setParameter("divisionId", newDivision.getDivisionId())
                .executeUpdate();
        em.flush();
    }

    private void handleRegularUserDivisionChange(EntityManager em, User user, Division newDivision) {
        // For regular users (employees and managers), just update their division and manager
        user.setDivisionId(newDivision.getDivisionId());
        user.setManagerId(newDivision.getDivisionDirector());
        em.merge(user);
    }
}
