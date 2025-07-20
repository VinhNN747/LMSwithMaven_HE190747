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
        processPost(request, response, user);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        Division division = ddb.get(Integer.parseInt(request.getParameter("id")));
        ddb.delete(division);
        response.sendRedirect("list");
    }
}
