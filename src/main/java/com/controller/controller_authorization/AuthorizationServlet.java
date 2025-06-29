/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_authorization;

import com.controller.controller_authentication.AuthenticationServlet;
import com.dao.UserDao;
import com.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base class for servlets that require authorization. Provides
 * role-based access control using database-driven permissions.
 *
 * Features: - Real-time permission checking - Comprehensive logging and
 * audit trails - Proper error handling - Security best practices
 *
 * @author vinhnnpc
 */
public abstract class AuthorizationServlet extends AuthenticationServlet {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationServlet.class.getName());

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            if (isAuthorized(request, user)) {
                processGet(request, response, user);
            } else {
                handleUnauthorizedAccess(request, response, user);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doGet for user: " + user.getUserId(), e);
            handleError(request, response, "An internal error occurred");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            if (isAuthorized(request, user)) {
                processPost(request, response, user);
            } else {
                handleUnauthorizedAccess(request, response, user);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doPost for user: " + user.getUserId(), e);
            handleError(request, response, "An internal error occurred");
        }
    }

    /**
     * Checks if the user is authorized to access the requested resource.
     * Performs real-time database check for each request.
     *
     * @param request the HTTP request
     * @param user the authenticated user
     * @return true if authorized, false otherwise
     */
    private boolean isAuthorized(HttpServletRequest request, User user) {
        if (user == null) {
            LOGGER.warning("Authorization check failed: user is null");
            return false;
        }

        String currentPath = request.getServletPath();
        if (currentPath == null || currentPath.trim().isEmpty()) {
            LOGGER.warning("Authorization check failed: invalid servlet path");
            return false;
        }

        // Check database for permission
        boolean hasPermission = userDao.hasPermission(user.getUserId(), currentPath);

        // Log authorization decision
        LOGGER.info("Authorization check for user: " + user.getUserId()
                + ", path: " + currentPath + ", result: " + hasPermission);

        return hasPermission;
    }

    /**
     * Handles unauthorized access attempts. Logs the attempt and sends
     * appropriate error response.
     */
    private void handleUnauthorizedAccess(HttpServletRequest request, HttpServletResponse response, User user) {
        String currentPath = request.getServletPath();
        String userAgent = request.getHeader("User-Agent");
        String remoteAddr = request.getRemoteAddr();

        LOGGER.warning("Unauthorized access attempt - User: " + user.getUserId()
                + ", Path: " + currentPath + ", IP: " + remoteAddr
                + ", User-Agent: " + userAgent);

        try {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "You are not authorized to access this resource.");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error sending unauthorized response", ex);
        }
    }

    /**
     * Handles general errors by logging and sending error response.
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String message) {
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error sending error response", ex);
        }
    }

    /**
     * Abstract method to be implemented by subclasses for GET requests. This
     * method will be called only if the user is authorized.
     */
    protected abstract void processGet(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception;

    /**
     * Abstract method to be implemented by subclasses for POST requests. This
     * method will be called only if the user is authorized.
     */
    protected abstract void processPost(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception;
}
