package com.controller.controller_user;

import com.dao.RoleDao;
import com.entity.Role;
import com.entity.User;
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

    private void processRequest(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            // Parse filter parameters
            String divisionIdStr = request.getParameter("divisionId");
            String roleIdStr = request.getParameter("roleId");
            Integer divisionId = (divisionIdStr != null && !divisionIdStr.isEmpty()) ? Integer.valueOf(divisionIdStr)
                    : null;
            Integer roleId = (roleIdStr != null && !roleIdStr.isEmpty()) ? Integer.valueOf(roleIdStr) : null;

            String pageNumberStr = request.getParameter("pageNumber");
            String pageSizeStr = request.getParameter("pageSize");
            Integer pageNumber = (pageNumberStr != null && !pageNumberStr.isEmpty()) ? Integer.valueOf(pageNumberStr) : 1;
            Integer pageSize = (pageSizeStr != null && !pageSizeStr.isEmpty()) ? Integer.valueOf(pageSizeStr) : 7;

            boolean isAdmin = user.getRole().getRoleLevel() == 100;
            boolean isDivisionHead = user.getRole().getRoleLevel() == 99;
            List<User> users;
            long totalCount;
            int totalPages;

            if (isAdmin) {
                // Admin: listRoles all users, with optional filters
                users = udb.listUsers(null, divisionId, roleId, null, null, pageNumber, pageSize);
                totalCount = udb.countUsers(null, divisionId, roleId, null, null);
            } else if (isDivisionHead) {
                // Division Head: listRoles all users in their division, with optional filters
                users = udb.listUsers(null, user.getDivisionId(), roleId, null, null, pageNumber, pageSize);
                totalCount = udb.countUsers(null, user.getDivisionId(), roleId, null, null);
            } else {
                // Regular user: listRoles all subordinates (direct and indirect), with optional filters
                List<Integer> subordinateIds = udb.getAllSubordinateIds(user.getUserId());
                users = udb.listUsers(subordinateIds, divisionId, roleId, null, null, pageNumber, pageSize);
                totalCount = udb.countUsers(subordinateIds, divisionId, roleId, null, null);
            }
            totalPages = (int) Math.ceil((double) totalCount / pageSize);

            request.setAttribute("users", users);
            request.setAttribute("divisions", ddb.list());
            request.setAttribute("roles", new RoleDao().list());
            request.setAttribute("selectedDivisionId", divisionId);
            request.setAttribute("selectedRoleId", roleId);
            request.setAttribute("isAdmin", isAdmin);
            request.setAttribute("pageNumber", pageNumber);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("../view/user/list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("../view/user/list.jsp").forward(request, response);
        }
    }
}
