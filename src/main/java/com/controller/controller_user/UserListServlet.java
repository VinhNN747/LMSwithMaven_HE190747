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
            List<User> allUsers = udb.list();
            
            // Use centralized pagination method
            PaginationUtil.paginate(request, "userPage", "users", "userTotalPages", "userCurrentPage", allUsers, 10);
            
            request.setAttribute("divisions", ddb.list());
            request.getRequestDispatcher("/view/user/list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/user/list.jsp").forward(request, response);
        }
    }
}
