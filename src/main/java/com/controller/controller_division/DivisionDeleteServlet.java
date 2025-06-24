package com.controller.controller_division;

import com.entity.Division;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/division/delete")
public class DivisionDeleteServlet extends BaseDivisionServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Division division = divisionDao.findById(id);
        if (division != null) {
            try {
                divisionDao.delete(division);
            } catch (Exception e) {
                request.setAttribute("error", "Cannot delete division: " + e.getMessage());
            }
        } else {
            request.setAttribute("error", "Division not found");
        }
        response.sendRedirect("list");
    }
} 