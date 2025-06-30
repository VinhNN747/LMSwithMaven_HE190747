/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.entity.LeaveRequest;
import com.entity.User;
import com.controller.PaginationUtil;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet for reviewing direct subordinates' leave requests (for approval/denial)
 */
@WebServlet(name = "LeaveRequestReviewServlet", urlPatterns = {"/leaverequest/review"})
public class LeaveRequestReviewServlet extends LeaveRequestBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        // Get DIRECT subordinates' requests for reviewing
        List<LeaveRequest> directSubRequests = ldb.leaveRequestsOfDirectSubs(user.getUserId());
        request.setAttribute("directSubRequests", directSubRequests);
        
        PaginationUtil.paginate(request, "reviewPage", "directSubRequests", "reviewTotalPages", "reviewCurrentPage",
                directSubRequests, 4);

        request.getRequestDispatcher("/view/leaverequest/review.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String action = request.getParameter("action");
        String requestIdStr = request.getParameter("requestId");
        if (action != null && requestIdStr != null) {
            try {
                int requestId = Integer.parseInt(requestIdStr);
                LeaveRequest lr = ldb.find(requestId);
                if (lr != null) {
                    if ("approve".equals(action)) {
                        lr.setStatus("Approved");
                    } else if ("reject".equals(action)) {
                        lr.setStatus("Rejected");
                    }
                    lr.setReviewerId(user.getUserId());
                    ldb.edit(lr);
                }
            } catch (NumberFormatException e) {
                // Optionally log or handle error
            }
        }
        String reviewPage = request.getParameter("reviewPage");
        if (reviewPage == null || !reviewPage.matches("\\d+")) {
            reviewPage = "1";
        }
        response.sendRedirect(request.getContextPath() + "/dashboard?tab=review&reviewPage=" + reviewPage);
    }

}
