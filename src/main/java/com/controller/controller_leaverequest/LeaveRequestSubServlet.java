/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.entity.LeaveRequest;
import com.entity.User;
import com.dao.UserDao;
import com.dao.DivisionDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LeaveRequestSubServlet", urlPatterns = "/leaverequest/subs")
public class LeaveRequestSubServlet extends LeaveRequestBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String status = request.getParameter("status");
        String senderIdStr = request.getParameter("senderId");
        String reviewerIdStr = request.getParameter("reviewerId");
        String divisionIdStr = request.getParameter("divisionId");
        Integer senderId = (senderIdStr != null && !senderIdStr.isEmpty()) ? Integer.valueOf(senderIdStr) : null;
        Integer reviewerId = (reviewerIdStr != null && !reviewerIdStr.isEmpty()) ? Integer.valueOf(reviewerIdStr) : null;
        Integer divisionId = (divisionIdStr != null && !divisionIdStr.isEmpty()) ? Integer.valueOf(divisionIdStr) : null;

        UserDao udb = new UserDao();
        // Get all subordinate IDs
        java.util.List<Integer> subordinateIds = udb.getAllSubordinateIds(user.getUserId());
        // If filtering by sender, restrict to that sender only (if in subordinates)
        java.util.List<Integer> filteredSenderIds = subordinateIds;
        if (senderId != null && subordinateIds.contains(senderId)) {
            filteredSenderIds = java.util.List.of(senderId);
        }
        List<LeaveRequest> subRequests = ldb.listRequests(filteredSenderIds, status, reviewerId, divisionId);

        // Fetch all users and divisions for filter dropdowns
        UserDao userDao = new UserDao();
        DivisionDao divisionDao = new DivisionDao();
        request.setAttribute("allUsers", userDao.list());
        request.setAttribute("allDivisions", divisionDao.list());
        request.setAttribute("subRequests", subRequests);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("selectedSenderId", senderId);
        request.setAttribute("selectedReviewerId", reviewerId);
        request.setAttribute("selectedDivisionId", divisionId);

        request.getRequestDispatcher("/view/leaverequest/subsrequests.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        // This servlet is for viewing only, not for actions
        response.sendRedirect(request.getContextPath() + "/leaverequest/subs");
    }
}
