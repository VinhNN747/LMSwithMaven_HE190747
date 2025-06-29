package com.controller.controller_role;

import com.entity.Role;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RoleCreateServlet", urlPatterns = {"/role/create"})
public class RoleCreateServlet extends RoleBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        request.getRequestDispatcher("/view/role/create.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String roleName = request.getParameter("roleName");
        String roleDescription = request.getParameter("roleDescription");
        String roleLevelStr = request.getParameter("roleLevel");

        // Create new role object
        Role newRole = new Role();
        newRole.setRoleName(roleName);
        newRole.setRoleDescription(roleDescription);

        // Parse role level
        if (roleLevelStr != null && !roleLevelStr.trim().isEmpty()) {
            try {
                Integer roleLevel = Integer.valueOf(roleLevelStr);
                newRole.setRoleLevel(roleLevel);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid role level format");
                request.getRequestDispatcher("/view/role/create.jsp").forward(request, response);
                return;
            }
        }

        // Validate the new role
        String roleValidation = validateRole(newRole);
        if (roleValidation != null) {
            request.setAttribute("error", roleValidation);
            request.getRequestDispatcher("/view/role/create.jsp").forward(request, response);
            return;
        }

        try {
            // Create the role
            rdb.create(newRole);

            // Redirect to role list on success
            response.sendRedirect(request.getContextPath() + "/role/list");
        } catch (Exception e) {
            request.setAttribute("error", "Failed to create role: " + e.getMessage());
            request.getRequestDispatcher("/view/role/create.jsp").forward(request, response);
        }
    }
} 