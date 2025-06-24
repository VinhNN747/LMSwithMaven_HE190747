package com.controller.controller_division;

//package com.controller.division_controller;
//
//import com.entity.Division;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet("/division/create")
//public class DivisionCreateServlet extends BaseDivisionServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String divisionName = request.getParameter("divisionName");
//        String divisionHead = request.getParameter("divisionHead");
//
//        // Validate input
//        if (!validateInput(request, response, divisionName, divisionHead)) {
//            return;
//        }
//
//        Division division = new Division();
//        division.setDivisionName(divisionName);
//        division.setDivisionHead(divisionHead != null && !divisionHead.isEmpty() ? divisionHead : null);
//
//        try {
//            divisionDao.create(division);
//            response.sendRedirect("list");
//        } catch (Exception e) {
//            request.setAttribute("error", "Error creating division: " + e.getMessage());
//            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
//        }
//    }
//
//    private boolean validateInput(HttpServletRequest request, HttpServletResponse response, String divisionName, String divisionHead)
//            throws ServletException, IOException {
//        
//        if (!validateDivisionName(divisionName)) {
//            request.setAttribute("error", "Division name is required and must not exceed " + MAX_DIVISION_NAME_LENGTH + " characters");
//            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
//            return false;
//        }
//        
//        if (!validateDivisionHead(divisionHead)) {
//            request.setAttribute("error", "Head ID must not exceed " + MAX_DIVISION_HEAD_LENGTH + " characters");
//            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
//            return false;
//        }
//        
//        if (divisionDao.existsByName(divisionName)) {
//            request.setAttribute("error", "A division with the name '" + divisionName + "' already exists");
//            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
//            return false;
//        }
//
//        return true;
//    }
//}
