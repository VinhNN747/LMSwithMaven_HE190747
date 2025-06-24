package com.controller.controller_division;

//package com.controller.division_controller;
//
//import com.entity.Division;
//import com.entity.User;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@WebServlet("/division/edit")
//public class DivisionEditServlet extends BaseDivisionServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        Integer id = Integer.parseInt(request.getParameter("id"));
//        Division division = divisionDao.findById(id);
//        if (division == null) {
//            request.setAttribute("error", "Division not found");
//            response.sendRedirect("list");
//        } else {
//            List<User> allLeadsAndHeads = userDao.list().stream()
//                    .filter(user -> ((user.getRole().equals(ROLE_LEAD) && Objects.equals(user.getDivisionId(), id)) || user.getRole().equals(ROLE_HEAD)))
//                    .collect(Collectors.toList());
//
//            request.setAttribute("division", division);
//            request.setAttribute("allLeadsAndHeads", allLeadsAndHeads);
//            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        Integer id = Integer.parseInt(request.getParameter("divisionId"));
//        String divisionName = request.getParameter("divisionName");
//        String divisionHead = request.getParameter("divisionHead");
//        Division existingDivision = divisionDao.findById(id);
//
//        // Validate input
//        if (!validateInput(request, response, divisionName, divisionHead, existingDivision)) {
//            return;
//        }
//
//        Division division = new Division();
//        division.setDivisionId(id);
//        division.setDivisionName(divisionName);
//        
//        // Preserve existing head if no new head is specified
//        if (divisionHead != null && !divisionHead.isEmpty()) {
//            division.setDivisionHead(divisionHead);
//        } else {
//            division.setDivisionHead(existingDivision.getDivisionHead());
//        }
//
//        try {
//            divisionDao.edit(division);
//            response.sendRedirect("list");
//        } catch (Exception e) {
//            request.setAttribute("error", "Error updating division: " + e.getMessage());
//            request.setAttribute("division", division);
//            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
//        }
//    }
//
//    private boolean validateInput(HttpServletRequest request, HttpServletResponse response, String divisionName, String divisionHead, Division existingDivision)
//            throws ServletException, IOException {
//        
//        if (!validateDivisionName(divisionName)) {
//            handleValidationError(request, response, existingDivision, "Division name is required and must not exceed " + MAX_DIVISION_NAME_LENGTH + " characters");
//            return false;
//        }
//        
//        if (!validateDivisionHead(divisionHead)) {
//            handleValidationError(request, response, existingDivision, "Head ID must not exceed " + MAX_DIVISION_HEAD_LENGTH + " characters");
//            return false;
//        }
//        
//        if ((divisionName != null && !Objects.equals(divisionName, existingDivision.getDivisionName()))) {
//            if (!validateUniqueDivisionName(divisionName, existingDivision)) {
//                handleValidationError(request, response, existingDivision, "Division name '" + divisionName + "' already exists");
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    private void handleValidationError(HttpServletRequest request, HttpServletResponse response, Division division, String errorMessage)
//            throws ServletException, IOException {
//        request.setAttribute("error", errorMessage);
//        request.setAttribute("division", division);
//        request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
//    }
//}
