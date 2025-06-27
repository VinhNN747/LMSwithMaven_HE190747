/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.dao.LeaveRequestDao;
import com.entity.LeaveRequest;
import com.entity.User;
import com.controller.controller_authorization.AuthorizationServlet;
import com.controller.PaginationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "LeaveRequestListServlet", urlPatterns = "/leaverequest/subs")

public class LeaveRequestSubServlet extends LeaveRequestBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Get current page from request
        int page = 1;
        int pageSize = 4;
        if (request.getParameter("subsPage") != null) {
            try {
                page = Integer.parseInt(request.getParameter("subsPage"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Use database-level pagination for better performance
        List<LeaveRequest> pagedRequests = ldb.leaveRequestsOfSubsPaginated(user.getUserId(), page, pageSize);
        long totalCount = ldb.getTotalCountForSubs(user.getUserId());
        
        PaginationUtil.paginateFromDatabase(request, "subsPage", "subRequests", "subsTotalPages", "subsCurrentPage", 
                pagedRequests, totalCount, pageSize);

        request.getRequestDispatcher("/view/leaverequest/subsrequests.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
