package com.controller.controller_role;

import com.entity.Role;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RoleDeleteServlet", urlPatterns = {"/role/delete"})
public class RoleDeleteServlet extends RoleBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        response.sendRedirect(request.getContextPath() + "/role/list");
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String roleIdStr = request.getParameter("id");

        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Role ID is required");
            response.sendRedirect(request.getContextPath() + "/role/list");
            return;
        }

        try {
            Integer roleId = Integer.valueOf(roleIdStr);
            Role role = rdb.findById(roleId);
            
            if (role == null) {
                request.getSession().setAttribute("error", "Role not found");
                response.sendRedirect(request.getContextPath() + "/role/list");
                return;
            }

            // Prevent deleting Division Head role (level 99)
            if (role.getRoleLevel() != null && role.getRoleLevel() == 99) {
                request.getSession().setAttribute("error", "Cannot delete Division Head role - it is protected.");
                response.sendRedirect(request.getContextPath() + "/role/list");
                return;
            }

            // Check if role is being used by any users
            if (!role.getUserRoles().isEmpty()) {
                request.getSession().setAttribute("error", "Cannot delete role: It is assigned to " + role.getUserRoles().size() + " user(s)");
                response.sendRedirect(request.getContextPath() + "/role/list");
                return;
            }

            // Delete the role
            rdb.delete(role);
            
            // Set success message
            request.getSession().setAttribute("successMessage", "Role '" + role.getRoleName() + "' deleted successfully.");
            
            response.sendRedirect(request.getContextPath() + "/role/list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid role ID format");
            response.sendRedirect(request.getContextPath() + "/role/list");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Failed to delete role: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/role/list");
        }
    }
}
