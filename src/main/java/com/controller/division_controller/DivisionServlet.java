/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.controller.division_controller;

import com.dao.DivisionDao;
import com.entity.Division;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DivisionServlet", urlPatterns = {"/division"})
public class DivisionServlet extends HttpServlet {

    private DivisionDao divisionDao;

    @Override
    public void init() throws ServletException {
        divisionDao = new DivisionDao();
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
                    deleteDivision(request, response);
                    break;
                case "list":
                default:
                    listDivisions(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            listDivisions(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                createDivision(request, response);
            } else if ("update".equals(action)) {
                updateDivision(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            listDivisions(request, response);
        }
    }

    private void listDivisions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Division> divisions = divisionDao.list();
        request.setAttribute("divisions", divisions);
        request.getRequestDispatcher("/view/division/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Division division = divisionDao.findById(id);
        if (division == null) {
            request.setAttribute("error", "Division not found");
            listDivisions(request, response);
        } else {
            request.setAttribute("division", division);
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
        }
    }

    private void createDivision(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String divisionName = request.getParameter("divisionName");
        String divisionDirector = request.getParameter("divisionDirector");
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
            response.sendRedirect("division?action=list");
        } catch (Exception e) {
            request.setAttribute("error", "Error creating division: " + e.getMessage());
            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
        }
    }

    private void updateDivision(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("divisionId"));
        String divisionName = request.getParameter("divisionName");
        String divisionDirector = request.getParameter("divisionDirector");
        if (divisionName == null || divisionName.trim().isEmpty()) {
            request.setAttribute("error", "Division name cannot be empty");
            request.setAttribute("division", divisionDao.findById(id));
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
            return;
        }
        if (divisionName.length() > 50) {
            request.setAttribute("error", "Division name must not exceed 50 characters");
            request.setAttribute("division", divisionDao.findById(id));
            request.getRequestDispatcher("/WEB-INF/divisionForm.jsp").forward(request, response);
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
            response.sendRedirect("division?action=list");
        } catch (Exception e) {
            request.setAttribute("error", "Error updating division: " + e.getMessage());
            request.setAttribute("division", division);
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
        }
    }

    private void deleteDivision(HttpServletRequest request, HttpServletResponse response)
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
        response.sendRedirect("division?action=list");
    }
}
