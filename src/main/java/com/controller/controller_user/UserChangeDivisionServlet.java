/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_user;

import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(urlPatterns = "/user/changedivision")
public class UserChangeDivisionServlet extends UserBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String userIdStr = request.getParameter("id");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/list");
            return;
        }

        try {
            Integer userId = Integer.valueOf(userIdStr);
            User existingUser = udb.getById(userId);
            if (existingUser == null) {
                response.sendRedirect(request.getContextPath() + "/user/list");
                return;
            }
            request.setAttribute("divisions", ddb.list());
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("/view/user/changedivision.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/list");
        }
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
