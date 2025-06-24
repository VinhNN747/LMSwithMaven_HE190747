package com.controller.controller_division;

import com.dao.DivisionDao;
import com.dao.UserDao;
import com.entity.Division;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public abstract class BaseDivisionServlet extends HttpServlet {

    protected DivisionDao divisionDao;
    protected UserDao userDao;

    // Role hierarchy constants
    protected static final String ROLE_EMPLOYEE = "Employee";
    protected static final String ROLE_LEAD = "Lead";
    protected static final String ROLE_HEAD = "Head";

    // Validation constants
    protected static final int MAX_DIVISION_NAME_LENGTH = 50;
    protected static final int MAX_DIVISION_HEAD_LENGTH = 10;

    @Override
    public void init() throws ServletException {
        divisionDao = new DivisionDao();
        userDao = new UserDao();
    }

    protected void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws IOException {
        request.setAttribute("error", errorMessage);
        response.sendRedirect("list");
    }

    protected boolean validateUniqueDivisionName(String divisionName, Division existingDivision) {
        if (existingDivision != null) {
            return !divisionDao.existsByName(divisionName) || Objects.equals(existingDivision.getDivisionName(), divisionName);
        }
        return !divisionDao.existsByName(divisionName);
    }

    /**
     * Validates division name field
     * @param divisionName the division name to validate
     * @return true if valid, false otherwise
     */
    protected boolean validateDivisionName(String divisionName) {
        return divisionName != null && !divisionName.trim().isEmpty() && divisionName.length() <= MAX_DIVISION_NAME_LENGTH;
    }

    /**
     * Validates division head field
     * @param divisionHead the division head to validate
     * @return true if valid, false otherwise
     */
    protected boolean validateDivisionHead(String divisionHead) {
        return divisionHead == null || divisionHead.length() <= MAX_DIVISION_HEAD_LENGTH;
    }

}
