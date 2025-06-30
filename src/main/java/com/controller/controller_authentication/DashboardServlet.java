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
        
        // Fetch user's division and manager information
        User fullUser = udb.getById(userId);
        session.setAttribute("userDivision", fullUser.getDivision());
        
        // Fetch manager information if user has a manager
        if (fullUser.getManagerId() != null) {
            User manager = udb.getById(fullUser.getManagerId());
            session.setAttribute("userManager", manager);
        } else {
            session.setAttribute("userManager", null);
        }
        
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
            List<LeaveRequest> myRequests = leaveRequestDao.listOf(userId);
            request.setAttribute("myRequests", myRequests);
        }

        if (canViewSubs) {
            List<LeaveRequest> subsRequests = leaveRequestDao.leaveRequestsOfSubs(userId);
            request.setAttribute("subRequests", subsRequests);
            // Also get direct subordinates' requests for review tab
            List<LeaveRequest> directSubRequests = leaveRequestDao.leaveRequestsOfDirectSubs(userId);
            request.setAttribute("directSubRequests", directSubRequests);
        }

        if (canViewAll) {
            List<LeaveRequest> allRequests = leaveRequestDao.list();
            request.setAttribute("allRequests", allRequests);
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
