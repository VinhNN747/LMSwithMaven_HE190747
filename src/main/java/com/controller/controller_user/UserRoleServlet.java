package com.controller.controller_user;

import com.dao.RoleDao;
import com.dao.DivisionDao;
import com.entity.Role;
import com.entity.User;
import com.entity.Division;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "UserRoleServlet", urlPatterns = {"/user/role"})
public class UserRoleServlet extends UserBaseServlet {

    private RoleDao roleDao = new RoleDao();

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("userId");

        Integer userId = Integer.valueOf(userIdStr);
        User targetUser = udb.get(Integer.valueOf(userIdStr));

        // Get current role using direct relationship
        Role currentRole = udb.getUserRole(userId);

        request.setAttribute("targetUser", targetUser);
        request.setAttribute("currentRole", currentRole);
        request.setAttribute("allRoles", roleDao.list());
        request.getRequestDispatcher("/view/user/role.jsp").forward(request, response);

    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("userId");
        String roleIdStr = request.getParameter("roleId");

        Integer userId = Integer.valueOf(userIdStr);
        User targetUser = udb.get(userId);

        Integer roleId = Integer.valueOf(roleIdStr);
        Role selectedRole = roleDao.get(roleId);

        // Check for division head conflicts (level 99) BEFORE updating role
        String divisionHeadConflict = handleDivisionHeadConflict(targetUser, selectedRole);
        if (divisionHeadConflict != null) {
            request.getSession().setAttribute("error", divisionHeadConflict);
            response.sendRedirect(request.getContextPath() + "/user/list");
            return;
        }

        // Get current role before update
        Role currentRole = udb.getUserRole(userId);

        // Update user role
        targetUser.setRole(selectedRole);
        targetUser.setRoleId(selectedRole.getRoleId());
        udb.edit(targetUser);

        // Sync division head when assigning/removing Division Head role
        syncDivisionHead(targetUser, currentRole, selectedRole);

        // Check if this creates a hierarchy conflict and handle it
        handleHierarchyConflict(targetUser, selectedRole);

        // Set success message
        request.getSession().setAttribute("successMessage", "Role updated successfully.");

        // Redirect to user list on success
        response.sendRedirect(request.getContextPath() + "/user/list");

    }

    /**
     * Handle hierarchy conflicts when user's role level becomes higher than
     * their manager's or lower than their subordinates'
     *
     * @return true if any manager/subordinate relationships were removed, false
     * otherwise
     */
    private void handleHierarchyConflict(User user, Role newRole) {

        // Check if user's new role level is higher than their manager's role level
        if (user.getManagerId() != null) {
            User manager = udb.get(user.getManagerId());
            if (manager != null) {
                Role managerRole = udb.getUserRole(user.getManagerId());
                if (managerRole != null && newRole.getRoleLevel() != null && managerRole.getRoleLevel() != null
                        && newRole.getRoleLevel() >= managerRole.getRoleLevel()) {

                    // Remove the manager assignment
                    user.setManagerId(null);
                    udb.edit(user);

                }
            }
        }

        // Check if user's new role level is lower than their subordinates' role levels
        List<User> subordinates = udb.listUsers(null, null, null, user.getUserId(), null);
        for (User subordinate : subordinates) {
            Role subordinateRole = udb.getUserRole(subordinate.getUserId());
            if (subordinateRole != null && newRole.getRoleLevel() != null && subordinateRole.getRoleLevel() != null
                    && newRole.getRoleLevel() <= subordinateRole.getRoleLevel()) {

                // Remove the manager assignment from subordinate
                subordinate.setManagerId(null);
                udb.edit(subordinate);

            }
        }
    }

    /**
     * Handle division head conflicts (level 99)
     *
     * @return null if no conflict, otherwise a message describing the conflict
     */
    private String handleDivisionHeadConflict(User user, Role newRole) {
        // Check if the new role is Division Head (level 99)
        if (newRole.getRoleLevel() != null && newRole.getRoleLevel() == 99) {
            // Check if there's already another user with Division Head role in the same
            // division
            if (user.getDivision().getHead() != null && user.getDivision().getHead() != user) {
                return user.getDivision().getHead().getFullName() + " is already the Head of this division!";
            }
        }
        return null;
    }

    /**
     * Sync division head when assigning/removing Division Head role
     */
    private void syncDivisionHead(User user, Role currentRole, Role newRole) {
        DivisionDao divisionDao = new DivisionDao();
        Division division = divisionDao.get(user.getDivisionId());

        boolean wasDivisionHead = currentRole != null && currentRole.getRoleLevel() != null && currentRole.getRoleLevel() == 99;
        boolean isDivisionHead = newRole != null && newRole.getRoleLevel() != null && newRole.getRoleLevel() == 99;

        if (!wasDivisionHead && isDivisionHead) {
            // promote to head
            division.setDivisionHead(user.getUserId());
            divisionDao.edit(division);
            List<User> users = udb.listUsers(null, user.getDivisionId(), null, null, null);
            for (User otherUser : users) {
                if (otherUser.getManagerId() == null && !otherUser.getUserId().equals(user.getUserId())) {
                    otherUser.setManagerId(user.getUserId());
                    udb.edit(otherUser);
                }
            }
        } else if (wasDivisionHead && !isDivisionHead) {
            // demote from head
            division.setDivisionHead(null);
            divisionDao.edit(division);
        }
    }
}
