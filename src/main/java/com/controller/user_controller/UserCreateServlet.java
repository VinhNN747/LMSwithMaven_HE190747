package com.controller.user_controller;

import com.entity.Division;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/create")
public class UserCreateServlet extends BaseUserServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get only active divisions with pagination
        int page = 1;
        int pageSize = 10;
        request.setAttribute("divisions", divisionDao.list());
        request.setAttribute("totalDivisions", divisionDao.countActiveDivisions());
        request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String divisionIdStr = request.getParameter("divisionId");
        String role = request.getParameter("role");
        String isActiveStr = request.getParameter("isActive");
        String managerId = null;
        String id = setNewId(fullName);

        // Validation
        if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100) {
            request.setAttribute("error", "Full name is required and must not exceed 100 characters");
            request.setAttribute("divisions", divisionDao.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }
        if (username == null || username.trim().isEmpty() || username.length() > 50) {
            request.setAttribute("error", "Username is required and must not exceed 50 characters");
            request.setAttribute("divisions", divisionDao.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }
        if (email == null || email.trim().isEmpty() || email.length() > 100
                || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            request.setAttribute("error", "Valid email is required and must not exceed 100 characters");
            request.setAttribute("divisions", divisionDao.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }
        if (gender != null && !gender.isEmpty() && !gender.matches("^[MF]$")) {
            request.setAttribute("error", "Gender must be 'M' or 'F'");
            request.setAttribute("divisions", divisionDao.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }

        Integer divisionId = null;
        if (divisionIdStr != null && !divisionIdStr.trim().isEmpty()) {
            try {
                divisionId = Integer.parseInt(divisionIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid Division ID format");
                request.setAttribute("divisions", divisionDao.list());
                request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
                return;
            }
        } else {
            request.setAttribute("error", "Division is required");
            request.setAttribute("divisions", divisionDao.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }

        boolean isNewDirector = false;
        Division division = divisionDao.get(divisionId);
        if (role == null || role.trim().isEmpty() || role.length() > 100) {
            request.setAttribute("error", "Role is required and must not exceed 100 characters");
            request.setAttribute("divisions", divisionDao.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }

        if (role.trim().equals(ROLE_HEAD)) {
            if (division.getHead() != null) {
                request.setAttribute("error", "This division has already had a head");
                request.setAttribute("divisions", divisionDao.list());
                request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
                return;
            } else {
                division.setDivisionHead(id);
                isNewDirector = true;
            }
        } else {
            managerId = divisionDao.get(divisionId).getDivisionHead();
        }

        boolean isActive = Boolean.parseBoolean(isActiveStr);

        if (userDao.existsByEmail(email)) {
            request.setAttribute("error", "Email '" + email + "' already exists");
            request.setAttribute("divisions", divisionDao.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }
        if (userDao.existsByUsername(username)) {
            request.setAttribute("error", "Username '" + username + "' already exists");
            request.setAttribute("divisions", divisionDao.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUserId(id);
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setGender(gender != null && !gender.isEmpty() ? gender : null);
        user.setDivisionId(divisionId);
        user.setRole(role);
        user.setIsActive(isActive);
        user.setManagerId(managerId);

        try {
            userDao.create(user);
            if (isNewDirector) {
                divisionDao.edit(division);
            }
            response.sendRedirect("list");
        } catch (Exception e) {
            request.setAttribute("error", "Failed to create user: " + e.getMessage());
            request.setAttribute("divisions", divisionDao.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
        }
    }
}
