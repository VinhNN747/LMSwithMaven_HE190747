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
        newRole.setRoleLevel(Integer.valueOf(roleLevelStr));

        // Validate the new role
        String roleValidation = validateRole(newRole);
        if (roleValidation != null) {
            request.setAttribute("error", roleValidation);
            request.getRequestDispatcher("/view/role/create.jsp").forward(request, response);
            return;
        }
        // Create the role
        rdb.create(newRole);
        // Redirect to role listRoles on success
        response.sendRedirect(request.getContextPath() + "/role/list");

    }
}
