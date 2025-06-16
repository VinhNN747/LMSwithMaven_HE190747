package com.controller.division_controller;

import com.entity.Division;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/division/create")
public class DivisionCreateServlet extends BaseDivisionServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String divisionName = request.getParameter("divisionName");
        String divisionDirector = request.getParameter("divisionDirector");

        // Validation
        if (divisionName == null || divisionName.trim().isEmpty()) {
            request.setAttribute("error", "Division name cannot be empty");
            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
            return;
        }
        if (divisionName.length() > 50) {
            request.setAttribute("error", "Division name must not exceed 50 characters");
            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
            return;
        }
        if (divisionDirector != null && divisionDirector.length() > 10) {
            request.setAttribute("error", "Director ID must not exceed 10 characters");
            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
            return;
        }
        if (divisionDao.existsByName(divisionName)) {
            request.setAttribute("error", "A division with the name '" + divisionName + "' already exists");
            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
            return;
        }

        Division division = new Division();
        division.setDivisionName(divisionName);
        division.setDivisionDirector(divisionDirector != null && !divisionDirector.isEmpty() ? divisionDirector : null);

        try {
            divisionDao.create(division);
            response.sendRedirect("list");
        } catch (Exception e) {
            request.setAttribute("error", "Error creating division: " + e.getMessage());
            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
        }
    }
} 