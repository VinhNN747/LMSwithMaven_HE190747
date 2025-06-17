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

        if (user.getRole().equals(ROLE_HEAD)) {
            handleHeadDivisionChange(em, user, oldDivision, newDivision);
        } else {
            handleRegularUserDivisionChange(em, user, newDivision);
        }
    }

    private void handleHeadDivisionChange(EntityManager em, User user, Division oldDivision, Division newDivision) {
        try {
            removeOldDivisionHead(em, oldDivision);
            handleNewDivisionHead(em, user, newDivision);
            setNewDivisionHead(em, user, newDivision);
        } catch (Exception e) {
            throw e;
        }
    }

    private void removeOldDivisionHead(EntityManager em, Division oldDivision) {
        String oldDivHeadId = oldDivision.getDivisionHead();
        if (oldDivHeadId != null) {
            em.createQuery("UPDATE User u SET u.managerId = NULL "
                    + "WHERE u.divisionId = :divisionId "
                    + "AND u.managerId = :headId")
                    .setParameter("divisionId", oldDivision.getDivisionId())
                    .setParameter("headId", oldDivHeadId)
                    .executeUpdate();
            em.flush();
        }
        oldDivision.setDivisionHead(null);
        em.merge(oldDivision);
        em.flush();
    }

    private void handleNewDivisionHead(EntityManager em, User user, Division newDivision) {
        String newDivHeadId = newDivision.getDivisionHead();
        if (newDivHeadId == null) {
            return;
        }

        try {
            User oldHead = em.find(User.class, newDivHeadId);
            if (oldHead == null || oldHead.getUserId().equals(user.getUserId())) {
                return;
            }
            demoteOldHead(em, oldHead, newDivision);
        } catch (Exception e) {
            throw e;
        }
    }

    private void demoteOldHead(EntityManager em, User oldHead, Division newDivision) {
        oldHead.setRole(ROLE_LEAD);
        oldHead.setManagerId(null);
        em.merge(oldHead);
        em.flush();

        em.createQuery("UPDATE User u SET u.managerId = NULL "
                + "WHERE u.divisionId = :divisionId "
                + "AND u.role = :managerRole")
                .setParameter("divisionId", newDivision.getDivisionId())
                .setParameter("managerRole", ROLE_LEAD)
                .executeUpdate();
        em.flush();
    }

    private void setNewDivisionHead(EntityManager em, User user, Division newDivision) {
        // Set new division head
        newDivision.setDivisionHead(user.getUserId());
        em.merge(newDivision);
        em.flush();

        // Update head's information
        user.setManagerId(null);
        user.setDivisionId(newDivision.getDivisionId());
        em.merge(user);
        em.flush();

        // Set all users in the division who don't have a manager to be managed by the new head
        em.createQuery("UPDATE User u SET u.managerId = :headId "
                + "WHERE u.divisionId = :divisionId "
                + "AND u.managerId IS NULL "
                + "AND u.userId != :headId")  // Don't set the head as their own manager
                .setParameter("headId", user.getUserId())
                .setParameter("divisionId", newDivision.getDivisionId())
                .executeUpdate();
        em.flush();
    }

    private void handleRegularUserDivisionChange(EntityManager em, User user, Division newDivision) {
        // If the user is a lead, handle their subordinates first
        if (user.getRole().equals(ROLE_LEAD)) {
            // Update any users who were managed by this lead to be managed by the division head
            em.createQuery("UPDATE User u SET u.managerId = :headId "
                    + "WHERE u.divisionId = :divisionId "
                    + "AND u.managerId = :userId")
                    .setParameter("headId", user.getManagerId())
                    .setParameter("divisionId", user.getDivisionId())
                    .setParameter("userId", user.getUserId())
                    .executeUpdate();
            em.flush();
        }

        // Update the user's division and manager
        user.setDivisionId(newDivision.getDivisionId());
        user.setManagerId(newDivision.getDivisionHead());
        em.merge(user);
    }
}
