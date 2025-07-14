/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.entity.LeaveRequest;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for viewing all subordinates' leave requests (for monitoring/overview)
 */
@WebServlet(name = "LeaveRequestSubServlet", urlPatterns = "/leaverequest/subs")
public class LeaveRequestSubServlet extends LeaveRequestBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Get ALL subordinates' requests (recursive) for viewing/monitoring
        List<LeaveRequest> subRequests = ldb.leaveRequestsOfSubs(user.getUserId());
        request.setAttribute("subRequests", subRequests);
        request.getRequestDispatcher("/view/leaverequest/subsrequests.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        // This servlet is for viewing only, not for actions
        response.sendRedirect(request.getContextPath() + "/leaverequest/subs");
    }
}
