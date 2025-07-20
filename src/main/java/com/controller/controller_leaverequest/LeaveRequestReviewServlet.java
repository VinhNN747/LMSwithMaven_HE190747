/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.entity.LeaveRequest;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet for reviewing direct subordinates' leave requests (for
 * approval/denial)
 */
@WebServlet(name = "LeaveRequestReviewServlet", urlPatterns = {"/leaverequest/review"})
public class LeaveRequestReviewServlet extends LeaveRequestBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String requestIdStr = request.getParameter("requestId");
        if (requestIdStr != null) {
            int requestId = Integer.parseInt(requestIdStr);
            LeaveRequest lr = ldb.get(requestId);
            if (lr != null) {
                request.setAttribute("leaveRequest", lr);
                request.setAttribute("reviewerName", user.getFullName());
                request.setAttribute("reviewerRole", user.getRole() != null ? user.getRole().getRoleName() : "");
                request.getRequestDispatcher("../view/leaverequest/review.jsp").forward(request, response);
                return;
            }
        }
        processRequest(request, response, user);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        processRequest(request, response, user);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        String action = request.getParameter("action");
        String requestIdStr = request.getParameter("requestId");

        int requestId = Integer.parseInt(requestIdStr);
        LeaveRequest lr = ldb.get(requestId);
        if (lr != null) {
            if ("approve".equals(action)) {
                lr.setStatus("Approved");
            } else if ("reject".equals(action)) {
                lr.setStatus("Rejected");
            }
            lr.setReviewerId(user.getUserId());
            ldb.edit(lr);
        }

        response.sendRedirect("subs");

    }
}
