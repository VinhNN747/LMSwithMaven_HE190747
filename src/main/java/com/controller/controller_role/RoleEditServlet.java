package com.controller.controller_role;

import com.entity.Role;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RoleEditServlet", urlPatterns = {"/role/edit"})
public class RoleEditServlet extends RoleBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String roleIdStr = request.getParameter("id");
        
        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/role/list");
            return;
        }

        try {
            Integer roleId = Integer.valueOf(roleIdStr);
            Role role = rdb.findById(roleId);
            
            if (role == null) {
                response.sendRedirect(request.getContextPath() + "/role/list");
                return;
            }

            // Prevent editing Division Head role (level 99)
            if (role.getRoleLevel() != null && role.getRoleLevel() == 99) {
                request.getSession().setAttribute("error", "Cannot edit Division Head role - it is protected.");
                response.sendRedirect(request.getContextPath() + "/role/list");
                return;
            }

            request.setAttribute("role", role);
            request.getRequestDispatcher("/view/role/edit.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/role/list");
        }
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String roleIdStr = request.getParameter("roleId");
        String roleName = request.getParameter("roleName");
        String roleDescription = request.getParameter("roleDescription");
        String roleLevelStr = request.getParameter("roleLevel");

        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/role/list");
            return;
        }

        try {
            Integer roleId = Integer.valueOf(roleIdStr);
            Role role = rdb.findById(roleId);
            
            if (role == null) {
                request.setAttribute("error", "Role not found");
                response.sendRedirect(request.getContextPath() + "/role/list");
                return;
            }

            // Update role properties
            role.setRoleName(roleName);
            role.setRoleDescription(roleDescription);

            // Parse and set role level
            if (roleLevelStr != null && !roleLevelStr.trim().isEmpty()) {
                try {
                    Integer roleLevel = Integer.valueOf(roleLevelStr);
                    role.setRoleLevel(roleLevel);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid role level format");
                    request.setAttribute("role", role);
                    request.getRequestDispatcher("/view/role/edit.jsp").forward(request, response);
                    return;
                }
            } else {
                role.setRoleLevel(null);
            }

            // Validate the updated role
            String roleValidation = validateRole(role);
            if (roleValidation != null) {
                request.setAttribute("error", roleValidation);
                request.setAttribute("role", role);
                request.getRequestDispatcher("/view/role/edit.jsp").forward(request, response);
                return;
            }

            // Update the role
            rdb.edit(role);
            
            response.sendRedirect(request.getContextPath() + "/role/list");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid role ID format");
            response.sendRedirect(request.getContextPath() + "/role/list");
        } catch (Exception e) {
            request.setAttribute("error", "Failed to update role: " + e.getMessage());
            Role role = rdb.findById(Integer.valueOf(roleIdStr));
            request.setAttribute("role", role);
            request.getRequestDispatcher("/view/role/edit.jsp").forward(request, response);
        }
    }
} 