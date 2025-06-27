/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.controller.controller_authorization.AuthorizationServlet;
import com.controller.PaginationUtil;
import com.dao.LeaveRequestDao;
import com.entity.LeaveRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 *
 * @author vinhnnpc
 */
public abstract class LeaveRequestBaseServlet extends AuthorizationServlet {

    LeaveRequestDao ldb = new LeaveRequestDao();

    protected boolean isDateIntervalValid(Date start, Date end) {
        if (start == null || end == null) {
            return false;
        }
        return !start.after(end);
    }

    /**
     * Centralized pagination method for leave requests using PaginationUtil
     * @param request The HTTP request
     * @param pageParam The page parameter name (e.g., "myPage", "subsPage")
     * @param reqAttr The request attribute name for the paged results
     * @param totalPagesAttr The request attribute name for total pages
     * @param currentPageAttr The request attribute name for current page
     * @param allRequests The complete list of requests to paginate
     * @param recordsPerPage Number of records per page (default: 3)
     */
    protected void paginate(HttpServletRequest request, String pageParam, String reqAttr, 
            String totalPagesAttr, String currentPageAttr, List<LeaveRequest> allRequests, int recordsPerPage) {
        PaginationUtil.paginate(request, pageParam, reqAttr, totalPagesAttr, currentPageAttr, allRequests, recordsPerPage);
    }

    /**
     * Overloaded paginate method with default records per page (3)
     */
    protected void paginate(HttpServletRequest request, String pageParam, String reqAttr, 
            String totalPagesAttr, String currentPageAttr, List<LeaveRequest> allRequests) {
        PaginationUtil.paginate(request, pageParam, reqAttr, totalPagesAttr, currentPageAttr, allRequests);
    }
}
