package com.controller.division_controller;

import com.entity.Division;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/division/edit")
public class DivisionEditServlet extends BaseDivisionServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Division division = divisionDao.findById(id);
        if (division == null) {
            request.setAttribute("error", "Division not found");
            response.sendRedirect("list");
        } else {
            request.setAttribute("division", division);
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("divisionId"));
        String divisionName = request.getParameter("divisionName");
        String divisionDirector = request.getParameter("divisionDirector");

        // Validation
        if (divisionName == null || divisionName.trim().isEmpty()) {
            request.setAttribute("error", "Division name cannot be empty");
            request.setAttribute("division", divisionDao.findById(id));
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
            return;
        }
        if (divisionName.length() > 50) {
            request.setAttribute("error", "Division name must not exceed 50 characters");
            request.setAttribute("division", divisionDao.findById(id));
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
            return;
        }
        if (divisionDirector != null && divisionDirector.length() > 10) {
            request.setAttribute("error", "Director ID must not exceed 10 characters");
            request.setAttribute("division", divisionDao.findById(id));
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
            return;
        }

        Division division = new Division();
        division.setDivisionId(id);
        division.setDivisionName(divisionName);
        division.setDivisionDirector(divisionDirector != null && !divisionDirector.isEmpty() ? divisionDirector : null);

        try {
            divisionDao.edit(division);
            response.sendRedirect("list");
        } catch (Exception e) {
            request.setAttribute("error", "Error updating division: " + e.getMessage());
            request.setAttribute("division", division);
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
        }
    }
} 