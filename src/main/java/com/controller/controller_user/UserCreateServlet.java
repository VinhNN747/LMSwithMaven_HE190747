/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.controller_user;

import com.dao.RoleDao;
import com.entity.Division;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "UserCreateServlet", urlPatterns = {"/user/create"})
public class UserCreateServlet extends UserBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        List<Division> divisions = ddb.list();
        request.setAttribute("divisions", divisions);
        request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {

        String fullName = request.getParameter("fullName");
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String genderStr = request.getParameter("gender");
        String divisionIdStr = request.getParameter("divisionId");
        // Create new user object
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUsername(userName);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setRoleId(new RoleDao().findByName("Employee").getRoleId()); // set default role to be Employee
        // Parse gender
        if (genderStr != null && !genderStr.trim().isEmpty()) {
            newUser.setGender(Boolean.valueOf(genderStr));
        }
        // Parse division ID
        if (divisionIdStr != null && !divisionIdStr.trim().isEmpty()) {
            try {
                Integer divisionId = Integer.valueOf(divisionIdStr);
                newUser.setDivisionId(divisionId);

                // Validate division exists
                Division division = ddb.get(divisionId);

                // Set manager to division head if division has one
                if (division.getDivisionHead() != null) {
                    newUser.setManagerId(division.getDivisionHead());
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid division ID format");
                request.setAttribute("divisions", ddb.list());
                request.getRequestDispatcher("../view/user/create.jsp").forward(request, response);
                return;
            }
        }

        // Validate the new user
        String userValidation = validateNewUser(newUser);
        if (userValidation != null) {
            request.setAttribute("error", userValidation);
            request.setAttribute("divisions", ddb.list());
            request.getRequestDispatcher("../view/user/create.jsp").forward(request, response);
            return;
        }

        // Create the user
        udb.create(newUser);

        response.sendRedirect("list");

    }
}
