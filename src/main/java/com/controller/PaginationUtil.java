/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Utility class for centralized pagination functionality
 * 
 * @author vinhnnpc
 */
public class PaginationUtil {

    /**
     * Centralized pagination method for any list of objects
     * 
     * @param <T>             The type of objects in the list
     * @param request         The HTTP request
     * @param pageParam       The page parameter name (e.g., "myPage", "subsPage",
     *                        "userPage")
     * @param reqAttr         The request attribute name for the paged results
     * @param totalPagesAttr  The request attribute name for total pages
     * @param currentPageAttr The request attribute name for current page
     * @param allItems        The complete list of items to paginate
     * @param recordsPerPage  Number of records per page
     */
    public static <T> void paginate(HttpServletRequest request, String pageParam, String reqAttr,
            String totalPagesAttr, String currentPageAttr, List<T> allItems, int recordsPerPage) {
        
        int page = getCurrentPage(request, pageParam);
        int totalRecords = allItems.size();
        int totalPages = calculateTotalPages(totalRecords, recordsPerPage);
        
        // Validate page number
        page = validatePageNumber(page, totalPages);
        
        int startIndex = (page - 1) * recordsPerPage;
        int endIndex = Math.min(startIndex + recordsPerPage, totalRecords);

        List<T> pagedItems = allItems.subList(startIndex, endIndex);

        setPaginationAttributes(request, reqAttr, totalPagesAttr, currentPageAttr, pagedItems, totalPages, page);
    }

    /**
     * Overloaded paginate method with default records per page (4)
     */
    public static <T> void paginate(HttpServletRequest request, String pageParam, String reqAttr,
            String totalPagesAttr, String currentPageAttr, List<T> allItems) {
        paginate(request, pageParam, reqAttr, totalPagesAttr, currentPageAttr, allItems, 4);
    }

    /**
     * Database-level pagination method for better performance
     * 
     * @param <T>             The type of objects in the list
     * @param request         The HTTP request
     * @param pageParam       The page parameter name
     * @param reqAttr         The request attribute name for the paged results
     * @param totalPagesAttr  The request attribute name for total pages
     * @param currentPageAttr The request attribute name for current page
     * @param pagedItems      The already paginated items from database
     * @param totalCount      Total count of all items (from separate count query)
     * @param recordsPerPage  Number of records per page
     */
    public static <T> void paginateFromDatabase(HttpServletRequest request, String pageParam, String reqAttr,
            String totalPagesAttr, String currentPageAttr, List<T> pagedItems, long totalCount, int recordsPerPage) {
        
        int page = getCurrentPage(request, pageParam);
        int totalPages = calculateTotalPages((int) totalCount, recordsPerPage);
        
        // Validate page number
        page = validatePageNumber(page, totalPages);
        
        setPaginationAttributes(request, reqAttr, totalPagesAttr, currentPageAttr, pagedItems, totalPages, page);
    }

    /**
     * Database-level pagination method with default page size (4)
     */
    public static <T> void paginateFromDatabase(HttpServletRequest request, String pageParam, String reqAttr,
            String totalPagesAttr, String currentPageAttr, List<T> pagedItems, long totalCount) {
        paginateFromDatabase(request, pageParam, reqAttr, totalPagesAttr, currentPageAttr, pagedItems, totalCount, 4);
    }

    /**
     * Helper method to get current page from request
     */
    private static int getCurrentPage(HttpServletRequest request, String pageParam) {
        int page = 1;
        if (request.getParameter(pageParam) != null) {
            try {
                page = Integer.parseInt(request.getParameter(pageParam));
            } catch (NumberFormatException e) {
                page = 1; // Default to page 1 if param is not a number
            }
        }
        return page;
    }

    /**
     * Helper method to calculate total pages
     */
    private static int calculateTotalPages(int totalRecords, int recordsPerPage) {
        return (int) Math.ceil((double) totalRecords / recordsPerPage);
    }

    /**
     * Helper method to validate page number
     */
    private static int validatePageNumber(int page, int totalPages) {
        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }
        if (page < 1) {
            page = 1;
        }
        return page;
    }

    /**
     * Helper method to set pagination attributes
     */
    private static <T> void setPaginationAttributes(HttpServletRequest request, String reqAttr, String totalPagesAttr,
            String currentPageAttr, List<T> pagedItems, int totalPages, int currentPage) {
        request.setAttribute(reqAttr, pagedItems);
        request.setAttribute(totalPagesAttr, totalPages);
        request.setAttribute(currentPageAttr, currentPage);
    }
}