/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.controller_user;

import com.entity.Division;
import com.entity.User;
import com.entity.UserRole;
import com.dao.RoleDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
                if (division == null) {
                    request.setAttribute("error", "Selected division does not exist");
                    request.setAttribute("divisions", ddb.list());
                    request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
                    return;
                }

                // Set manager to division head if division has one
                if (division.getDivisionHead() != null) {
                    newUser.setManagerId(division.getDivisionHead());
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid division ID format");
                request.setAttribute("divisions", ddb.list());
                request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
                return;
            }
        }

        // Validate the new user
        String userValidation = validateNewUser(newUser);
        if (userValidation != null) {
            request.setAttribute("error", userValidation);
            request.setAttribute("divisions", ddb.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
            return;
        }

        try {
            // Create the user
            udb.create(newUser);

            // Assign Employee role to the new user
            // Fetch the userId (should be set after persist)
            Integer newUserId = newUser.getUserId();
            if (newUserId != null) {
                // Find Employee role by name
                RoleDao roleDao = new RoleDao();
                Integer employeeRoleId = roleDao.getRoleIdByName("Employee");
                
                if (employeeRoleId != null) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(newUserId);
                    userRole.setRoleId(employeeRoleId);

                    // Persist UserRole using the same EntityManager pattern as UserDao
                    jakarta.persistence.EntityManager em = udb.getEntityManager();
                    jakarta.persistence.EntityTransaction tx = em.getTransaction();
                    try {
                        tx.begin();
                        em.persist(userRole);
                        tx.commit();
                    } catch (Exception e) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                        throw e;
                    } finally {
                        em.close();
                    }
                } else {
                    // Log warning if Employee role doesn't exist
                    System.err.println("Warning: Employee role not found in database");
                }
            }

            // Redirect to user list on success
            response.sendRedirect(request.getContextPath() + "/user/list");
        } catch (IOException e) {
            request.setAttribute("error", "Failed to create user: " + e.getMessage());
            request.setAttribute("divisions", ddb.list());
            request.getRequestDispatcher("/view/user/create.jsp").forward(request, response);
        }
    }
}
