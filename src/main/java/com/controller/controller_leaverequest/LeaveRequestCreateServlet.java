/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.entity.LeaveRequest;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "LeaveRequestCreateServlet", urlPatterns = "/leaverequest/create")
public class LeaveRequestCreateServlet extends LeaveRequestBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        request.getRequestDispatcher("/view/leaverequest/create.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String title = request.getParameter("title");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String reason = request.getParameter("reason");

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);
        } catch (Exception e) {
            request.setAttribute("error", "Invalid date format.");
            request.getRequestDispatcher("/dashboard").forward(request, response);
            return;
        }
        if (title == null || title.trim().isEmpty() || title.length() > 255) {
            request.setAttribute("error", "Title is required and must be less than 255 characters.");
            request.getRequestDispatcher("/dashboard").forward(request, response);
            return;
        }
        // Validate date interval
        if (!isDateIntervalValid(startDate, endDate)) {
            request.setAttribute("error", "Start date must be before or equal to end date.");
            request.getRequestDispatcher("/dashboard").forward(request, response);
            return;
        }

        // Validate reason
        if (reason == null || reason.trim().isEmpty() || reason.length() > 255) {
            request.setAttribute("error", "Reason is required and must be less than 255 characters.");
            request.getRequestDispatcher("/dashboard").forward(request, response);
            return;
        }

        // Create and persist the leave request
        LeaveRequest lr = new LeaveRequest();
        lr.setSenderId(user.getUserId());
        lr.setStatus("In Progress");
        lr.setTitle(title.trim());
        lr.setReason(reason.trim());
        lr.setStartDate(startDate);
        lr.setEndDate(endDate);

        // Persist using your DAO (assume leaveRequestDao)
        ldb.create(lr);

        // Redirect to my requests page
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

}
