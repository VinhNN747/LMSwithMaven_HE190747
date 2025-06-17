package com.controller.division_controller;

import com.entity.Division;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
            List<User> allLeadsAndHeads = userDao.list().stream()
                    .filter(user -> (user.getRole().equals(ROLE_LEAD) || user.getRole().equals(ROLE_HEAD)))
                    .collect(Collectors.toList());

            request.setAttribute("division", division);
            request.setAttribute("allLeadsAndHeads", allLeadsAndHeads);
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("divisionId"));
        String divisionName = request.getParameter("divisionName");
        String divisionHead = request.getParameter("divisionHead");

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
        if (divisionHead != null && divisionHead.length() > 10) {
            request.setAttribute("error", "Head ID must not exceed 10 characters");
            request.setAttribute("division", divisionDao.findById(id));
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
            return;
        }

        Division division = new Division();
        division.setDivisionId(id);
        division.setDivisionName(divisionName);
        division.setDivisionHead(divisionHead != null && !divisionHead.isEmpty() ? divisionHead : null);

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



