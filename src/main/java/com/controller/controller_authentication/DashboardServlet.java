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
        // Fetch and set the current user's leave requests
        UserDao udb = new UserDao();
        if (user != null && !udb.getUserRoles(user.getUserId()).isEmpty()) {
            LeaveRequestDao leaveRequestDao = new LeaveRequestDao();
            List<com.entity.LeaveRequest> myRequests = leaveRequestDao.listOf(user.getUserId());
            request.setAttribute("myRequests", myRequests);
            System.out.println("Set myRequests: " + myRequests.size() + " requests for user " + user.getUserId());

        }
        if (user != null && (udb.getUserRoleNames(user.getUserId()).contains("Lead")
                || udb.getUserRoleNames(user.getUserId()).contains("Head"))) {
            System.out.println("User " + user.getUserId() + " has Lead or Head role, fetching subordinate requests");
            LeaveRequestDao leaveRequestDao = new LeaveRequestDao();
            try {
                List<com.entity.LeaveRequest> subsRequests = leaveRequestDao.leaveRequestsOfSubs(user.getUserId());
                request.setAttribute("subRequests", subsRequests);
                System.out.println("Set subRequests: " + subsRequests.size() + " requests");
            } catch (Exception e) {
                Logger.getLogger(DashboardServlet.class.getName()).log(Level.WARNING,
                        "Error fetching subordinate requests: " + e.getMessage(), e);
                request.setAttribute("subRequests", new java.util.ArrayList<>());
                System.err.println("Error fetching subordinate requests: " + e.getMessage());
            }
        } else {
            System.out.println("User " + user.getUserId() + " does not have Lead or Head role");
        }
        request.getRequestDispatcher("/view/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            processRequest(request, response, user);
        } catch (ServletException ex) {
            Logger.getLogger(DashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            processRequest(request, response, user);
        } catch (ServletException ex) {
            Logger.getLogger(DashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
