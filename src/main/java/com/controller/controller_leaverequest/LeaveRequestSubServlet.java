/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.dao.LeaveRequestDao;
import com.entity.LeaveRequest;
import com.entity.User;
import com.controller.controller_authorization.AuthorizationServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "LeaveRequestListServlet", urlPatterns = "/leaverequest/subs")

public class LeaveRequestSubServlet extends LeaveRequestBaseServlet {

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        LeaveRequestDao leaveRequestDao = new LeaveRequestDao();
        List<LeaveRequest> allSubRequests = leaveRequestDao.leaveRequestsOfSubs(user.getUserId());

        int page = 1;
        int recordsPerPage = 3;
        if (request.getParameter("subsPage") != null) {
            try {
                page = Integer.parseInt(request.getParameter("subsPage"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalRecords = allSubRequests.size();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }

        if (page < 1) {
            page = 1;
        }

        int startIndex = (page - 1) * recordsPerPage;
        int endIndex = Math.min(startIndex + recordsPerPage, totalRecords);

        List<LeaveRequest> pagedRequests = allSubRequests.subList(startIndex, endIndex);

        request.setAttribute("subRequests", pagedRequests);
        request.setAttribute("subsTotalPages", totalPages);
        request.setAttribute("subsCurrentPage", page);

        request.getRequestDispatcher("/view/leaverequest/subsrequests.jsp").forward(request, response);
    }

    private void paginate(HttpServletRequest request, String pageParam, String reqAttr, String totalPagesAttr,
            String currentPageAttr, List<com.entity.LeaveRequest> allRequests) {

        int page = 1;
        int recordsPerPage = 3;
        if (request.getParameter(pageParam) != null) {
            try {
                page = Integer.parseInt(request.getParameter(pageParam));
            } catch (NumberFormatException e) {
                page = 1; // Default to page 1 if param is not a number
            }
        }

        int totalRecords = allRequests.size();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }

        if (page < 1) {
            page = 1;
        }

        int startIndex = (page - 1) * recordsPerPage;
        int endIndex = Math.min(startIndex + recordsPerPage, totalRecords);

        List<com.entity.LeaveRequest> pagedRequests = allRequests.subList(startIndex, endIndex);

        request.setAttribute(reqAttr, pagedRequests);
        request.setAttribute(totalPagesAttr, totalPages);
        request.setAttribute(currentPageAttr, page);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
