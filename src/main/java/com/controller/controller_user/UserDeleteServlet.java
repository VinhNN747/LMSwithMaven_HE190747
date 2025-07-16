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

        Integer userId = Integer.valueOf(id);
        User existingUser = udb.get(userId);

        udb.delete(existingUser);
        request.setAttribute("success", "User '" + existingUser.getFullName() + "' has been deleted successfully");

        response.sendRedirect("list");
    }

}
