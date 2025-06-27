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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "LeaveRequestOwnServlet", urlPatterns = "/leaverequest/myrequests")
public class LeaveRequestOwnServlet extends LeaveRequestBaseServlet {

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
        HttpSession session = request.getSession(false);
        if (session != null) {
            User userSession = (User) session.getAttribute("user");
            if (userSession != null) {
                // Get current page from request
                int page = 1;
                int pageSize = 4;
                if (request.getParameter("myPage") != null) {
                    try {
                        page = Integer.parseInt(request.getParameter("myPage"));
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
                }

                // Use database-level pagination for better performance
                List<LeaveRequest> pagedRequests = ldb.listOfPaginated(userSession.getUserId(), page, pageSize);
                long totalCount = ldb.getTotalCountForUser(userSession.getUserId());
                
                PaginationUtil.paginateFromDatabase(request, "myPage", "myRequests", "myTotalPages", "myCurrentPage", 
                        pagedRequests, totalCount, pageSize);
            }
        }
        request.getRequestDispatcher("/view/leaverequest/myrequests.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, null);
    }

}
