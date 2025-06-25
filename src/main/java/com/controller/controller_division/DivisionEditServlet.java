package com.controller.controller_division;

import com.entity.Division;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        
    }
}
