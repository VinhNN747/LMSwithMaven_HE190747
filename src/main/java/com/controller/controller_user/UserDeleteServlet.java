/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.controller_user;

import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "UserDeleteServlet", urlPatterns = {"/user/delete"})
public class UserDeleteServlet extends UserBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        processRequest(request, response, user);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        processRequest(request, response, user);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String id = request.getParameter("id");

        if (id == null || id.trim().isEmpty()) {
            request.setAttribute("error", "User ID is required");
            response.sendRedirect("list");
            return;
        }
        try {
            Integer userId = Integer.valueOf(id);
            User existingUser = udb.getById(userId);
            if (existingUser == null) {
                request.setAttribute("error", "User not found");
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                return;
            }

            udb.delete(existingUser);
            request.setAttribute("success", "User '" + existingUser.getFullName() + "' has been deleted successfully");
        } catch (ServletException | IOException | NumberFormatException e) {
            String errorMessage = "Cannot delete user!";
            if (e.getCause() != null) {
                errorMessage += e.getCause().getMessage();
            } else {
                errorMessage += e.getMessage();
            }
            request.setAttribute("error", errorMessage);
        }

        response.sendRedirect("list");
    }

}
