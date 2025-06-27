package com.controller.controller_user;

import com.entity.User;
import com.controller.PaginationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/user/list")
public class UserListServlet extends UserBaseServlet {

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
            // Get current page from request
            int page = 1;
            int pageSize = 10;
            if (request.getParameter("userPage") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("userPage"));
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // Use database-level pagination for better performance
            List<User> pagedUsers = udb.listPaginated(page, pageSize);
            long totalCount = udb.getTotalCount();
            
            PaginationUtil.paginateFromDatabase(request, "userPage", "users", "userTotalPages", "userCurrentPage", 
                    pagedUsers, totalCount, pageSize);
            
            request.setAttribute("divisions", ddb.list());
            request.getRequestDispatcher("/view/user/list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/user/list.jsp").forward(request, response);
        }
    }
}
