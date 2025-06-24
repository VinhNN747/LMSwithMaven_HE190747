package com.controller.controller_authentication;

import com.dao.RoleDao;
import com.dao.UserDao;
import com.entity.Feature;
import com.entity.Role;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AuthenticationServlet", urlPatterns = "/login2")
public abstract class AuthenticationServlet extends HttpServlet {

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response, User user);

    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response, User user);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user != null) {
            doPost(req, resp, user);
        } else {
            resp.sendError(403, "You have not yet authenticated");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user != null) {
            doGet(req, resp, user);
        } else {
            resp.sendError(403, "You have not yet authenticated");
        }
    }

    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (User) session.getAttribute("user");
    }

}
