/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_authentication;

import com.entity.User;
import com.dao.UserDao;
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
        User fullUser = udb.get(userId);
        session.setAttribute("userDivision", fullUser.getDivision());

        // Fetch manager information if user has a manager
        if (fullUser.getManagerId() != null) {
            User manager = udb.get(fullUser.getManagerId());
            session.setAttribute("userManager", manager);
        } else {
            session.setAttribute("userManager", null);
        }

        setPermission(session, "canViewOwn", "/leaverequest/myrequests", udb, userId);
        setPermission(session, "canViewSubs", "/leaverequest/subs", udb, userId);
        setPermission(session, "canViewAll", "/leaverequest/list", udb, userId);
        setPermission(session, "canCreate", "/leaverequest/create", udb, userId);
        setPermission(session, "canViewUser", "/user/list", udb, userId);
        setPermission(session, "canViewRole", "/role/list", udb, userId);
        setPermission(session, "canViewDivision", "/division/list", udb, userId);
        setPermission(session, "canViewAgenda", "/agenda", udb, userId);

        request.getRequestDispatcher("view/dashboard.jsp").forward(request, response);
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

    private void setPermission(HttpSession session, String permission, String endpoint, UserDao udb, int userId) {
        session.setAttribute(permission, udb.hasPermission(userId, endpoint));
    }
}
