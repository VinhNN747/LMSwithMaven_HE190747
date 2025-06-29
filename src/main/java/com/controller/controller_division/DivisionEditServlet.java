package com.controller.controller_division;

import com.entity.Division;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DivisionEditServlet", urlPatterns = "/division/edit")
public class DivisionEditServlet extends BaseDivisionServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        Integer id = Integer.valueOf(request.getParameter("id"));
        Division division = ddb.findById(id);
        if (division == null) {
            request.setAttribute("error", "Division not found");
            response.sendRedirect("list");
        } else {

            request.setAttribute("division", division);
            request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
        }
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String divisionIdStr = request.getParameter("divisionId");
        String divisionName = request.getParameter("divisionName");
        
        if (divisionIdStr == null || divisionIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Division ID is required");
            response.sendRedirect(request.getContextPath() + "/division/list");
            return;
        }
        
        if (divisionName == null || divisionName.trim().isEmpty()) {
            request.setAttribute("error", "Division name is required");
            response.sendRedirect(request.getContextPath() + "/division/list");
            return;
        }

        try {
            Integer divisionId = Integer.valueOf(divisionIdStr);
            Division division = ddb.findById(divisionId);
            
            if (division == null) {
                request.setAttribute("error", "Division not found");
                response.sendRedirect(request.getContextPath() + "/division/list");
                return;
            }
            
            // Store original name for validation
            String originalName = division.getDivisionName();
            
            // Update division name
            division.setDivisionName(divisionName.trim());
            
            // Validate the updated division
            String validationError = validateDivision(division);
            if (validationError != null) {
                request.setAttribute("error", validationError);
                request.setAttribute("division", division);
                request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);
                return;
            }
            
            // Check if name actually changed
            if (originalName.equals(division.getDivisionName())) {
                request.getSession().setAttribute("successMessage", "No changes were made.");
                response.sendRedirect(request.getContextPath() + "/division/list");
                return;
            }
            
            // Save the changes
            ddb.edit(division);
            
            request.getSession().setAttribute("successMessage", "Division updated successfully.");
            response.sendRedirect(request.getContextPath() + "/division/list");
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid division ID format");
            response.sendRedirect(request.getContextPath() + "/division/list");
        } catch (Exception e) {
            request.setAttribute("error", "Failed to update division: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/division/list");
        }
    }
}
