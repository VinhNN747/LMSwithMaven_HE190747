package com.controller.controller_division;

import com.entity.Division;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DivisionListServlet", urlPatterns = "/division/list")
public class DivisionListServlet extends BaseDivisionServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        // Get all divisions and handle pagination in servlet
        String name = request.getParameter("name");
        String pageNumberStr = request.getParameter("pageNumber");
        String pageSizeStr = request.getParameter("pageSize");
        
        Integer pageNumber = (pageNumberStr != null && !pageNumberStr.isEmpty()) ? Integer.valueOf(pageNumberStr) : 1;
        Integer pageSize = (pageSizeStr != null && !pageSizeStr.isEmpty()) ? Integer.valueOf(pageSizeStr) : 7;
        List<Division> divisions = ddb.list(name, pageNumber, pageSize);
        long totalCount = ddb.countUsers(null);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        request.setAttribute("divisions", divisions);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("pageSize", pageSize);
        request.getRequestDispatcher("/view/division/list.jsp").forward(request, response);

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
