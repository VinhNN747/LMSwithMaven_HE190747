/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.dao.LeaveRequestDao;
import com.entity.LeaveRequest;
import com.entity.User;
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
@WebServlet(name = "LeaveRequestAllServlet", urlPatterns = "/leaverequest/list")
public class LeaveRequestAllServlet extends LeaveRequestBaseServlet {

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
            // Get current page from request
            int page = 1;
            int pageSize = 5;
            if (request.getParameter("allPage") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("allPage"));
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // Use database-level pagination for better performance
            List<LeaveRequest> pagedRequests = ldb.listPaginated(page, pageSize);
            long totalCount = ldb.getTotalCount();
            
            PaginationUtil.paginateFromDatabase(request, "allPage", "allRequests", "allTotalPages", "allCurrentPage", 
                    pagedRequests, totalCount, pageSize);

            request.getRequestDispatcher("/view/leaverequest/allrequests.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/leaverequest/allrequests.jsp").forward(request, response);
        }
    }

}
