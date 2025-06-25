package com.controller.controller_division;

import com.entity.Division;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DivisionDeleteServlet", urlPatterns = "/division/delete")
public class DivisionDeleteServlet extends BaseDivisionServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        Integer id = Integer.valueOf(request.getParameter("id"));
        Division division = ddb.findById(id);
        if (division != null) {
            try {
                ddb.delete(division);
            } catch (Exception e) {
                request.setAttribute("error", "Cannot delete division: " + e.getMessage());
            }
        } else {
            request.setAttribute("error", "Division not found");
        }
        response.sendRedirect("list");
    }
}
