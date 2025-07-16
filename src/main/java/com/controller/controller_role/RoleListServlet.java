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
        String name = request.getParameter("name");
        String pageNumberStr = request.getParameter("pageNumber");
        String pageSizeStr = request.getParameter("pageSize");
        Integer pageNumber = (pageNumberStr != null && !pageNumberStr.isEmpty()) ? Integer.valueOf(pageNumberStr) : 1;
        Integer pageSize = (pageSizeStr != null && !pageSizeStr.isEmpty()) ? Integer.valueOf(pageSizeStr) : 7;
        List<Role> allRoles = rdb.listRoles(name, pageNumber, pageSize);
        long totalCount = rdb.countRoles(name);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        request.setAttribute("roles", allRoles);
        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/view/role/list.jsp").forward(request, response);

    }
}
