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
        processPost(request, response, user);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String roleIdStr = request.getParameter("id");

        Integer roleId = Integer.valueOf(roleIdStr);
        Role role = rdb.get(roleId);

        // Check if role is being used by any users
        if (!rdb.getUsersWithRoleId(roleId).isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        // Delete the role
        rdb.delete(role);

        response.sendRedirect("list");

    }
}
