/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_authentication;

import com.entity.User;
import com.dao.LeaveRequestDao;
import com.dao.UserDao;
import com.entity.LeaveRequest;
import com.controller.PaginationUtil;
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
            // Get current page for my requests
            int myPage = 1;
            if (request.getParameter("myPage") != null) {
                try {
                    myPage = Integer.parseInt(request.getParameter("myPage"));
                } catch (NumberFormatException e) {
                    myPage = 1;
                }
            }
            
            List<LeaveRequest> myRequests = leaveRequestDao.listOfPaginated(userId, myPage, 4);
            long myTotalCount = leaveRequestDao.getTotalCountForUser(userId);
            PaginationUtil.paginateFromDatabase(request, "myPage", "myRequests", "myTotalPages", "myCurrentPage", 
                    myRequests, myTotalCount, 4);
        }
        
        if (canViewSubs) {
            // Get current page for subs requests
            int subsPage = 1;
            if (request.getParameter("subsPage") != null) {
                try {
                    subsPage = Integer.parseInt(request.getParameter("subsPage"));
                } catch (NumberFormatException e) {
                    subsPage = 1;
                }
            }
            
            List<LeaveRequest> subsRequests = leaveRequestDao.leaveRequestsOfSubsPaginated(userId, subsPage, 4);
            long subsTotalCount = leaveRequestDao.getTotalCountForSubs(userId);
            PaginationUtil.paginateFromDatabase(request, "subsPage", "subRequests", "subsTotalPages", "subsCurrentPage", 
                    subsRequests, subsTotalCount, 4);
        }
        
        if (canViewAll) {
            // Get current page for all requests
            int allPage = 1;
            if (request.getParameter("allPage") != null) {
                try {
                    allPage = Integer.parseInt(request.getParameter("allPage"));
                } catch (NumberFormatException e) {
                    allPage = 1;
                }
            }
            
            List<LeaveRequest> allRequests = leaveRequestDao.listPaginated(allPage, 5);
            long allTotalCount = leaveRequestDao.getTotalCount();
            PaginationUtil.paginateFromDatabase(request, "allPage", "allRequests", "allTotalPages", "allCurrentPage", 
                    allRequests, allTotalCount, 5);
        }

        request.getRequestDispatcher("/view/dashboard.jsp").forward(request, response);
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
