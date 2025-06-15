/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.user_controller;

import com.dao.DivisionDao;
import com.dao.UserDao;
import com.entity.Division;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {

    private final DivisionDao divisionDao = new DivisionDao();
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "list";

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                case "list":
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            listUsers(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                createUser(request, response);
            } else if ("update".equals(action)) {
                updateUser(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            listUsers(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userDao.list();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/view/user/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Division> divisions = divisionDao.list();
        request.setAttribute("divisions", divisions);
        request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        User user = userDao.findById(id);
        if (user == null) {
            request.setAttribute("error", "User not found");
            listUsers(request, response);
        } else {
            List<Division> divisions = divisionDao.list();
            request.setAttribute("divisions", divisions);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Division> divisions = divisionDao.list();
        request.setAttribute("divisions", divisions);
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
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }
        if (username == null || username.trim().isEmpty() || username.length() > 50) {
            request.setAttribute("error", "Username is required and must not exceed 50 characters");
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }
        if (email == null || email.trim().isEmpty() || email.length() > 100 || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            request.setAttribute("error", "Valid email is required and must not exceed 100 characters");
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }
        if (gender != null && !gender.isEmpty() && !gender.matches("^[MF]$")) {
            request.setAttribute("error", "Gender must be 'M' or 'F'");
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }
        Integer divisionId = null;
        if (divisionIdStr != null && !divisionIdStr.trim().isEmpty()) {
            try {
                divisionId = Integer.parseInt(divisionIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid Division ID format");
                request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
                return;
            }
        }
        boolean isNewDirector = false;
        Division division = divisionDao.get(divisionId);
        if (role == null || role.trim().isEmpty() || role.length() > 100) {
            request.setAttribute("error", "Role is required and must not exceed 100 characters");
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }
        if (role.trim().equals("Director")) {

            if (division.getDirector() != null) {
                request.setAttribute("error", "This division has already had a director");
                request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
                return;
            } else {
                division.setDivisionDirector(id);
                isNewDirector = true;

            }
        } else {
            managerId = divisionDao.get(divisionId).getDivisionDirector();
        }
        boolean isActive = Boolean.parseBoolean(isActiveStr);

//        if (managerId != null && !managerId.trim().isEmpty()) {
//            if (managerId.length() > 10) {
//                request.setAttribute("error", "Manager ID must not exceed 10 characters");
//                request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
//                return;
//            }
//            if (!userDao.existsById(managerId)) {
//                request.setAttribute("error", "Invalid Manager ID");
//                request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
//                return;
//            }
//        }
        if (userDao.existsByUsernameOrEmail(username, email)) {
            request.setAttribute("error", "Username '" + username + "' or email '" + email + "' already exists");
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
            response.sendRedirect("user?action=list");
        } catch (Exception e) {
            System.out.println(e);
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String divisionIdStr = request.getParameter("divisionId");
        String role = request.getParameter("role");
        String isActiveStr = request.getParameter("isActive");
        String managerId = request.getParameter("managerId");

        // Validation
        if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100) {
            request.setAttribute("error", "Full name is required and must not exceed 100 characters");
            request.setAttribute("user", userDao.findById(userId));
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }
        if (username == null || username.trim().isEmpty() || username.length() > 50) {
            request.setAttribute("error", "Username is required and must not exceed 50 characters");
            request.setAttribute("user", userDao.findById(userId));
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }
        if (email == null || email.trim().isEmpty() || email.length() > 100 || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            request.setAttribute("error", "Valid email is required and must not exceed 100 characters");
            request.setAttribute("user", userDao.findById(userId));
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }
        if (gender != null && !gender.isEmpty() && !gender.matches("^[MF]$")) {
            request.setAttribute("error", "Gender must be 'M' or 'F'");
            request.setAttribute("user", userDao.findById(userId));
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }
        Integer divisionId = null;
        if (divisionIdStr != null && !divisionIdStr.trim().isEmpty()) {
            try {
                divisionId = Integer.parseInt(divisionIdStr);
                if (!userDao.existsByDivisionId(divisionId)) {
                    request.setAttribute("error", "Invalid Division ID");
                    request.setAttribute("user", userDao.findById(userId));
                    request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid Division ID format");
                request.setAttribute("user", userDao.findById(userId));
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                return;
            }
        }
        if (role == null || role.trim().isEmpty() || role.length() > 100) {
            request.setAttribute("error", "Role is required and must not exceed 100 characters");
            request.setAttribute("user", userDao.findById(userId));
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
            return;
        }
        boolean isActive = Boolean.parseBoolean(isActiveStr);
        if (managerId != null && !managerId.trim().isEmpty()) {
            if (managerId.length() > 10) {
                request.setAttribute("error", "Manager ID must not exceed 10 characters");
                request.setAttribute("user", userDao.findById(userId));
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                return;
            }
            if (!userDao.existsById(managerId)) {
                request.setAttribute("error", "Invalid Manager ID");
                request.setAttribute("user", userDao.findById(userId));
                request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
                return;
            }
        }

        User user = new User();
        user.setUserId(userId);
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setGender(gender != null && !gender.isEmpty() ? gender : null);
        user.setDivisionId(divisionId);
        user.setRole(role);
        user.setIsActive(isActive);
        user.setManagerId(managerId != null && !managerId.isEmpty() ? managerId : null);

        try {
            userDao.edit(user);
            response.sendRedirect("user?action=list");
        } catch (Exception e) {
            request.setAttribute("error", "Error updating user: " + e.getMessage());
            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/user/edit.jsp").forward(request, response);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        User user = userDao.findById(id);
        if (user == null) {
            request.setAttribute("error", "User not found");
        } else {
            try {
                userDao.delete(user);
            } catch (Exception e) {
                request.setAttribute("error", "Cannot delete user: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            }
        }
        listUsers(request, response);
    }

    private String setNewId(String fullName) {
        // Build acronym from FullName (e.g., John Doe -> JD)
        StringBuilder acronym = new StringBuilder();
        String[] nameParts = fullName.trim().split("\\s+");
        for (String part : nameParts) {
            if (!part.isEmpty()) {
                acronym.append(part.charAt(0));
            }
        }

        // Get max index for users with the same acronym
        int index = userDao.getMaxIndexForAcronym(acronym.toString().toUpperCase()) + 1;

        // Format ID like JD001
        return acronym.toString().toUpperCase() + String.format("%03d", index);
    }
}
