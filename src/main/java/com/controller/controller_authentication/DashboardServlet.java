/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_authentication;

import com.entity.User;
import com.dao.LeaveRequestDao;
import com.dao.UserDao;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends AuthenticationServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDao udb = new UserDao();
        int userId = user.getUserId();
        boolean canViewOwn = udb.hasPermission(userId, "/leaverequest/myrequests");
        boolean canViewSubs = udb.hasPermission(userId, "/leaverequest/subs");
        boolean canViewAll = udb.hasPermission(userId, "/leaverequest/list");
        boolean canCreate = udb.hasPermission(userId, "/leaverequest/create");
        LeaveRequestDao leaveRequestDao = new LeaveRequestDao();
        session.setAttribute("canViewOwn", canViewOwn);
        session.setAttribute("canViewSubs", canViewSubs);
        session.setAttribute("canViewAll", canViewAll);
        session.setAttribute("canCreate", canCreate);

        if (canViewOwn) {
            List<com.entity.LeaveRequest> myRequests = leaveRequestDao.listOf(userId);
            paginate(request, "myPage", "myRequests", "myTotalPages", "myCurrentPage", myRequests);
        }
        if (canViewSubs) {
            List<com.entity.LeaveRequest> subsRequests = leaveRequestDao.leaveRequestsOfSubs(userId);
            paginate(request, "subsPage", "subRequests", "subsTotalPages", "subsCurrentPage", subsRequests);
        }
        if (canViewAll) {

        }

        request.getRequestDispatcher("/view/dashboard.jsp").forward(request, response);
    }

    private void paginate(HttpServletRequest request, String pageParam, String reqAttr, String totalPagesAttr,
            String currentPageAttr, List<com.entity.LeaveRequest> allRequests) {

        int page = 1;
        int recordsPerPage = 3;
        if (request.getParameter(pageParam) != null) {
            try {
                page = Integer.parseInt(request.getParameter(pageParam));
            } catch (NumberFormatException e) {
                page = 1; // Default to page 1 if param is not a number
            }
        }

        int totalRecords = allRequests.size();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }

        if (page < 1) {
            page = 1;
        }

        int startIndex = (page - 1) * recordsPerPage;
        int endIndex = Math.min(startIndex + recordsPerPage, totalRecords);

        List<com.entity.LeaveRequest> pagedRequests = allRequests.subList(startIndex, endIndex);

        request.setAttribute(reqAttr, pagedRequests);
        request.setAttribute(totalPagesAttr, totalPages);
        request.setAttribute(currentPageAttr, page);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            processRequest(request, response, user);
        } catch (ServletException | IOException e) {
            Logger.getLogger(DashboardServlet.class.getName()).log(Level.SEVERE, "Error in doGet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            processRequest(request, response, user);
        } catch (ServletException | IOException e) {
            Logger.getLogger(DashboardServlet.class.getName()).log(Level.SEVERE, "Error in doPost", e);
        }
    }

}
