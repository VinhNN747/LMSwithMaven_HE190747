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

    private void processRequest(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String status = request.getParameter("status");
        String senderIdStr = request.getParameter("senderId");
        String reviewerIdStr = request.getParameter("reviewerId");
        String divisionIdStr = request.getParameter("divisionId");
        Integer senderId = (senderIdStr != null && !senderIdStr.isEmpty()) ? Integer.valueOf(senderIdStr) : null;
        Integer reviewerId = (reviewerIdStr != null && !reviewerIdStr.isEmpty()) ? Integer.valueOf(reviewerIdStr) : null;
        Integer divisionId = (divisionIdStr != null && !divisionIdStr.isEmpty()) ? Integer.valueOf(divisionIdStr) : null;

        List<Integer> senderIds = (senderId != null) ? java.util.List.of(senderId) : null;
        List<LeaveRequest> allRequests = ldb.listRequests(senderIds, status, reviewerId, divisionId);
        UserDao userDao = new UserDao();
        DivisionDao divisionDao = new DivisionDao();
        request.setAttribute("allUsers", userDao.list());
        request.setAttribute("allDivisions", divisionDao.list());
        request.setAttribute("allRequests", allRequests);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("selectedSenderId", senderId);
        request.setAttribute("selectedReviewerId", reviewerId);
        request.setAttribute("selectedDivisionId", divisionId);
        request.getRequestDispatcher("/view/leaverequest/allrequests.jsp").forward(request, response);
    }

}
