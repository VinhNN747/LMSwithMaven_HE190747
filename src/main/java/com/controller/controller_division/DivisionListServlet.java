package com.controller.controller_division;

import com.entity.Division;
import com.entity.User;
import com.controller.PaginationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DivisionListServlet", urlPatterns = "/division/list")
public class DivisionListServlet extends BaseDivisionServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            // Get current page from request
            int page = 1;
            int pageSize = 5;
            if (request.getParameter("divisionPage") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("divisionPage"));
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // Use database-level pagination for better performance
            List<Division> pagedDivisions = ddb.listPaginated(page, pageSize);
            long totalCount = ddb.getTotalCount();
            
            PaginationUtil.paginateFromDatabase(request, "divisionPage", "divisions", "divisionTotalPages", "divisionCurrentPage", 
                    pagedDivisions, totalCount, pageSize);
            
            request.getRequestDispatcher("/view/division/list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/division/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        processRequest(request, response, user);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        processRequest(request, response, user);
    }
}
