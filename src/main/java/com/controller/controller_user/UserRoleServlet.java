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
        
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/list");
            return;
        }

        try {
            Integer userId = Integer.valueOf(userIdStr);
            User targetUser = udb.getById(userId);
            
            if (targetUser == null) {
                request.setAttribute("error", "User not found");
                response.sendRedirect(request.getContextPath() + "/user/list");
                return;
            }

            // Get current role using direct relationship
            Role currentRole = udb.getUserRole(userId);

            request.setAttribute("targetUser", targetUser);
            request.setAttribute("currentRole", currentRole);
            request.setAttribute("allRoles", roleDao.list());
            request.getRequestDispatcher("/view/user/role.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format");
            response.sendRedirect(request.getContextPath() + "/user/list");
        }
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("userId");
        String roleIdStr = request.getParameter("roleId");
        
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/list");
            return;
        }

        try {
            Integer userId = Integer.valueOf(userIdStr);
            User targetUser = udb.getById(userId);
            
            if (targetUser == null) {
                request.setAttribute("error", "User not found");
                response.sendRedirect(request.getContextPath() + "/user/list");
                return;
            }

            // Validate role selection
            if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Please select a role");
                
                // Re-populate the form data
                List<Role> allRoles = roleDao.list();
                Role currentRole = udb.getUserRole(userId);
                
                request.setAttribute("targetUser", targetUser);
                request.setAttribute("allRoles", allRoles);
                request.setAttribute("currentRole", currentRole);
                request.getRequestDispatcher("/view/user/role.jsp").forward(request, response);
                return;
            }

            try {
                Integer roleId = Integer.valueOf(roleIdStr);
                Role selectedRole = roleDao.findById(roleId);
                
                if (selectedRole == null) {
                    request.setAttribute("error", "Selected role does not exist");
                    
                    // Re-populate the form data
                    List<Role> allRoles = roleDao.list();
                    Role currentRole = udb.getUserRole(userId);
                    
                    request.setAttribute("targetUser", targetUser);
                    request.setAttribute("allRoles", allRoles);
                    request.setAttribute("currentRole", currentRole);
                    request.getRequestDispatcher("/view/user/role.jsp").forward(request, response);
                    return;
                }

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
                udb.updateUserRole(userId, roleId);

                // Sync division head when assigning/removing Division Head role
                syncDivisionHead(targetUser, currentRole, selectedRole);

                // Check if this creates a hierarchy conflict and handle it
                boolean managerRemoved = handleHierarchyConflict(targetUser, selectedRole);

                // Set success message
                if (managerRemoved) {
                    request.getSession().setAttribute("successMessage", 
                        "Role updated successfully. Manager assignment was automatically removed due to hierarchy conflict.");
                } else {
                    request.getSession().setAttribute("successMessage", "Role updated successfully.");
                }

                // Redirect to user list on success
                response.sendRedirect(request.getContextPath() + "/user/list");
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid role ID format");
                
                // Re-populate the form data
                List<Role> allRoles = roleDao.list();
                Role currentRole = udb.getUserRole(userId);
                
                request.setAttribute("targetUser", targetUser);
                request.setAttribute("allRoles", allRoles);
                request.setAttribute("currentRole", currentRole);
                request.getRequestDispatcher("/view/user/role.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format");
            response.sendRedirect(request.getContextPath() + "/user/list");
        } catch (Exception e) {
            request.setAttribute("error", "Failed to update user role: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/list");
        }
    }

    /**
     * Handle hierarchy conflicts when user's role level becomes higher than their manager's
     * or lower than their subordinates'
     * @return true if any manager/subordinate relationships were removed, false otherwise
     */
    private boolean handleHierarchyConflict(User user, Role newRole) {
        boolean relationshipsRemoved = false;
        
        // Check if user's new role level is higher than their manager's role level
        if (user.getManagerId() != null) {
            User manager = udb.getById(user.getManagerId());
            if (manager != null) {
                Role managerRole = udb.getUserRole(user.getManagerId());
                if (managerRole != null && newRole.getRoleLevel() != null && managerRole.getRoleLevel() != null 
                        && newRole.getRoleLevel() >= managerRole.getRoleLevel()) {
                    
                    // Remove the manager assignment
                    user.setManagerId(null);
                    udb.edit(user);
                    
                    // Log the action
                    System.out.println("Manager assignment removed for user " + user.getFullName() 
                            + " due to role level conflict. New role: " + newRole.getRoleName() 
                            + " (Level " + newRole.getRoleLevel() + ")");
                    
                    relationshipsRemoved = true;
                }
            }
        }
        
        // Check if user's new role level is lower than their subordinates' role levels
        List<User> subordinates = udb.getUsersByManagerId(user.getUserId());
        for (User subordinate : subordinates) {
            Role subordinateRole = udb.getUserRole(subordinate.getUserId());
            if (subordinateRole != null && newRole.getRoleLevel() != null && subordinateRole.getRoleLevel() != null 
                    && newRole.getRoleLevel() <= subordinateRole.getRoleLevel()) {
                
                // Remove the manager assignment from subordinate
                subordinate.setManagerId(null);
                udb.edit(subordinate);
                
                // Log the action
                System.out.println("Manager assignment removed for subordinate " + subordinate.getFullName() 
                        + " due to role level conflict. User's new role: " + newRole.getRoleName() 
                        + " (Level " + newRole.getRoleLevel() + "), Subordinate's role: " + subordinateRole.getRoleName() 
                        + " (Level " + subordinateRole.getRoleLevel() + ")");
                
                relationshipsRemoved = true;
            }
        }
        
        return relationshipsRemoved;
    }

    /**
     * Handle division head conflicts (level 99)
     * @return null if no conflict, otherwise a message describing the conflict
     */
    private String handleDivisionHeadConflict(User user, Role newRole) {
        // Check if the new role is Division Head (level 99)
        if (newRole.getRoleLevel() != null && newRole.getRoleLevel() == 99) {
            // Check if there's already another user with Division Head role in the same division
            List<User> existingDivisionHeads = udb.getUsersWithRoleLevelInDivision(99, user.getDivisionId());
            
            for (User existingHead : existingDivisionHeads) {
                // Skip the current user (in case they're updating their own role)
                if (!existingHead.getUserId().equals(user.getUserId())) {
                    return "Cannot assign Division Head role: " + existingHead.getFullName() + 
                           " is already the Division Head for this division.";
                }
            }
        }
        return null;
    }

    /**
     * Sync division head when assigning/removing Division Head role
     */
    private void syncDivisionHead(User user, Role currentRole, Role newRole) {
        DivisionDao divisionDao = new DivisionDao();
        
        boolean wasDivisionHead = currentRole != null && currentRole.getRoleLevel() == 99;
        boolean isDivisionHead = newRole.getRoleLevel() == 99;
        
        if (isDivisionHead && !wasDivisionHead) {
            // This is a new Division Head assignment
            // Clear any existing division head in this division
            List<User> existingHeads = udb.getUsersWithRoleLevelInDivision(99, user.getDivisionId());
            for (User existingHead : existingHeads) {
                if (!existingHead.getUserId().equals(user.getUserId())) {
                    // Remove Division Head role from existing head
                    udb.clearUserRole(existingHead.getUserId());
                }
            }
            
            // Set this user as the division head
            Division division = divisionDao.findById(user.getDivisionId());
            if (division != null) {
                division.setDivisionHead(user.getUserId());
                divisionDao.edit(division);
            }
        } else if (wasDivisionHead && !isDivisionHead) {
            // This is removing Division Head role
            // Clear the division head
            Division division = divisionDao.findById(user.getDivisionId());
            if (division != null && user.getUserId().equals(division.getDivisionHead())) {
                division.setDivisionHead(null);
                divisionDao.edit(division);
            }
        }
    }
} 