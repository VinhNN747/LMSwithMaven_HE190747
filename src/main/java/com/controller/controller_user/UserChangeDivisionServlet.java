/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_user;

import com.entity.User;
import com.entity.Role;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(urlPatterns = "/user/changedivision")
public class UserChangeDivisionServlet extends UserBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("id");

        Integer userId = Integer.valueOf(userIdStr);
        User existingUser = udb.get(userId);

        // Get subordinates to show impact
        List<User> subordinates = udb.listUsers(null, null, null, userId, null, null, null);

        // Get current role to check if user is Division Head
        Role currentRole = udb.getUserRole(userId);

        request.setAttribute("divisions", ddb.list());
        request.setAttribute("user", existingUser);
        request.setAttribute("subordinates", subordinates);
        request.setAttribute("currentRole", currentRole);
        request.getRequestDispatcher("../view/user/changedivision.jsp").forward(request, response);

    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("userId");
        String divisionIdStr = request.getParameter("divisionId");

        Integer userId = Integer.valueOf(userIdStr);
        Integer divisionId = Integer.valueOf(divisionIdStr);

        User targetUser = udb.get(userId);

        // Store old division for comparison
        Integer oldDivisionId = targetUser.getDivisionId();

        // Check if user is already in the selected division
        if (oldDivisionId != null && oldDivisionId.equals(divisionId)) {
            response.sendRedirect("list");
            return;
        }

        // Check if user is a Division Head - prevent moving Division Heads
        Role currentRole = udb.getUserRole(userId);
        if (currentRole != null && currentRole.getRoleLevel() != null && currentRole.getRoleLevel() == 99) {
            response.sendRedirect("/list");
            return;
        }

        // Update user's division
        targetUser.setDivisionId(divisionId);
        targetUser.setDivision(ddb.get(divisionId));
        udb.edit(targetUser);

        // Handle organizational relationships
        handleOrganizationalRelationships(targetUser, oldDivisionId, divisionId);

        // Redirect to user list
        response.sendRedirect("list");

    }

    /**
     * Handle organizational relationships when a user moves to a new division
     *
     * @param user The user being moved
     * @param oldDivisionId The old division ID
     * @param newDivisionId The new division ID
     * @return Description of changes made
     */
    private void handleOrganizationalRelationships(User user, Integer oldDivisionId, Integer newDivisionId) {
        StringBuilder changes = new StringBuilder();

        // Handle subordinates (users who have this user as their manager)
        int subordinatesRemoved = removeSubordinateRelationships(user.getUserId());
        if (subordinatesRemoved > 0) {
            changes.append("Removed manager relationship from ").append(subordinatesRemoved)
                    .append(" subordinate(s). ");
        }

        // Handle manager assignment - assign to division head if available
        boolean managerAssigned = assignToDivisionHead(user, newDivisionId);
        if (managerAssigned) {
            changes.append("Assigned to division head as manager. ");
        } else {
            // If no division head, remove existing manager if in different division
            boolean managerRemoved = removeManagerIfDifferentDivision(user, newDivisionId);
            if (managerRemoved) {
                changes.append("Removed manager assignment (manager is in different division). ");
            }
        }

    }

    /**
     * Remove manager relationships for users who have the specified user as
     * their manager
     *
     * @param managerId The ID of the user who was a manager
     * @return Number of subordinates affected
     */
    private int removeSubordinateRelationships(Integer managerId) {
        // Find all users who have this user as their manager
        List<User> subordinates = udb.listUsers(null, null, null, managerId, null, null, null);

        for (User subordinate : subordinates) {
            subordinate.setManagerId(null);
            udb.edit(subordinate);
        }

        return subordinates.size();
    }

    /**
     * Assign user to the division head if one exists
     *
     * @param user The user being moved
     * @param divisionId The division ID
     * @return true if assigned to division head, false otherwise
     */
    private boolean assignToDivisionHead(User user, Integer divisionId) {
        // Get the division to find the division head
        com.entity.Division division = ddb.get(divisionId);
        if (division == null || division.getDivisionHead() == null) {
            return false; // No division head assigned
        }

        // Check if the division head is different from the current user
        if (division.getDivisionHead().equals(user.getUserId())) {
            return false; // User is the division head, no need to assign manager
        }

        // Get the division head user
        User divisionHead = udb.get(division.getDivisionHead());
        if (divisionHead == null) {
            return false; // Division head user doesn't exist
        }

        // Check if division head has a role level higher than user's role level
        Role userRole = udb.getUserRole(user.getUserId());
        Role headRole = udb.getUserRole(divisionHead.getUserId());

        if (userRole != null && headRole != null
                && userRole.getRoleLevel() != null && headRole.getRoleLevel() != null) {

            // Only assign if division head has higher role level
            if (headRole.getRoleLevel() > userRole.getRoleLevel()) {
                user.setManagerId(divisionHead.getUserId());
                udb.edit(user);
                return true;
            }
        }

        return false;
    }

    /**
     * Remove manager assignment if the manager is in a different division
     *
     * @param user The user being moved
     * @param newDivisionId The new division ID
     * @return true if manager was removed, false otherwise
     */
    private boolean removeManagerIfDifferentDivision(User user, Integer newDivisionId) {
        if (user.getManagerId() == null) {
            return false; // No manager to remove
        }

        User manager = udb.get(user.getManagerId());
        if (manager == null) {
            return false; // Manager doesn't exist
        }

        // If manager is in different division, remove the relationship
        if (!newDivisionId.equals(manager.getDivisionId())) {
            user.setManagerId(null);
            udb.edit(user);
            return true;
        }

        return false;
    }

}
