package com.controller.user_controller;

import com.dao.DivisionDao;
import com.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public abstract class BaseUserServlet extends HttpServlet {
    protected UserDao userDao;
    protected DivisionDao divisionDao;
    
    // Role hierarchy constants
    protected static final String ROLE_EMPLOYEE = "Employee";
    protected static final String ROLE_LEAD = "Lead";
    protected static final String ROLE_HEAD = "Head";

    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
        divisionDao = new DivisionDao();
    }
    
    protected boolean isValidRoleTransition(String currentRole, String newRole) {
        if (currentRole == null || newRole == null)
            return false;

        // Get role levels
        int currentLevel = getRoleLevel(currentRole);
        int newLevel = getRoleLevel(newRole);

        // Only allow one level change
        return Math.abs(currentLevel - newLevel) == 1;
    }

    protected int getRoleLevel(String role) {
        switch (role) {
            case ROLE_EMPLOYEE:
                return 1;
            case ROLE_LEAD:
                return 2;
            case ROLE_HEAD:
                return 3;
            default:
                return 0;
        }
    }

    protected String setNewId(String fullName) {
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