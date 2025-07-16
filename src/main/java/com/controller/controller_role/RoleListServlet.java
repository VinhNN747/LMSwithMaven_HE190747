package com.controller.controller_role;

import com.entity.Role;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/role/list")
public class RoleListServlet extends RoleBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response,
            User user) throws Exception {
        processRequest(request, response, user);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response,
            User user) throws Exception {
        processRequest(request, response, user);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        List<Role> allRoles = rdb.list();
        request.setAttribute("roles", allRoles);
        request.getRequestDispatcher("/view/role/list.jsp").forward(request, response);

    }
}
