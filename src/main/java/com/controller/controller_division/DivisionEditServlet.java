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
        Division division = ddb.get(Integer.parseInt(request.getParameter("id")));

        request.setAttribute("division", division);
        request.getRequestDispatcher("/view/division/edit.jsp").forward(request, response);

    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String divisionIdStr = request.getParameter("divisionId");
        String divisionName = request.getParameter("divisionName");

        Division division = ddb.get(Integer.parseInt(divisionIdStr));

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
        // Save the changes
        ddb.edit(division);
        response.sendRedirect(request.getContextPath() + "/division/list");

    }
}
