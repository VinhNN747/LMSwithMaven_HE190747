package com.controller.controller_division;

import com.entity.Division;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DivisionCreateServlet", urlPatterns = "/division/create")
public class DivisionCreateServlet extends BaseDivisionServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String divisionName = request.getParameter("divisionName");
        Division newDivision = new Division();
        newDivision.setDivisionName(divisionName);
        String divisionValidation = validateDivision(newDivision);
        if (divisionValidation != null) {
            request.setAttribute("error", divisionValidation);
            request.getRequestDispatcher("/view/division/create.jsp").forward(request, response);
            return;
        }
        ddb.create(newDivision);
        response.sendRedirect(request.getContextPath() + "/division/list");

    }
}
