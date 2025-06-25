package com.controller.controller_authentication;

import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "AuthenticationServlet")
public abstract class AuthenticationServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationServlet.class.getName());

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response, User user);

    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response, User user);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user != null) {
            doPost(req, resp, user);
        } else {
            LOGGER.warning("Unauthenticated POST attempt from IP: " + req.getRemoteAddr());
            resp.sendError(403, "You have not yet authenticated");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user != null) {
            doGet(req, resp, user);
        } else {
            LOGGER.warning("Unauthenticated GET attempt from IP: " + req.getRemoteAddr());
            resp.sendError(403, "You have not yet authenticated");
        }
    }

    /**
     * Retrieves the authenticated user from the session, if present.
     *
     * @param req the HTTP request
     * @return the authenticated User, or null if not authenticated
     */
    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        Object userObj = session.getAttribute("user");
        return (userObj instanceof User) ? (User) userObj : null;
    }

}
