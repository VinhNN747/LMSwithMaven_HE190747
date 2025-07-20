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

        Integer roleId = Integer.valueOf(roleIdStr);
        Role role = rdb.get(roleId);

        request.setAttribute("role", role);
        request.getRequestDispatcher("../view/role/edit.jsp").forward(request, response);

    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String roleIdStr = request.getParameter("roleId");
        String roleName = request.getParameter("roleName");
        String roleDescription = request.getParameter("roleDescription");
        String roleLevelStr = request.getParameter("roleLevel");

        Integer roleId = Integer.valueOf(roleIdStr);
        Role role = rdb.get(roleId);

        // Update role properties
        role.setRoleName(roleName);
        role.setRoleDescription(roleDescription);
        role.setRoleLevel(Integer.valueOf(roleLevelStr));

        // Validate the updated role
        String roleValidation = validateRole(role);
        if (roleValidation != null) {
            request.setAttribute("role", role);
            request.getRequestDispatcher("../view/role/edit.jsp").forward(request, response);
            return;
        }

        // Update the role
        rdb.edit(role);

        response.sendRedirect("list");

    }
}
