package com.controller.controller_role;

import com.entity.Role;
import com.entity.User;
import com.controller.PaginationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/role/list")
public class RoleListServlet extends RoleBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        processRequest(request, response, user);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        processRequest(request, response, user);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        try {
            List<Role> allRoles = rdb.list();
            
            // Use centralized pagination method
            PaginationUtil.paginate(request, "rolePage", "roles", "roleTotalPages", "roleCurrentPage", allRoles, 10);
            
            request.getRequestDispatcher("/view/role/list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/role/list.jsp").forward(request, response);
        }
    }
} 