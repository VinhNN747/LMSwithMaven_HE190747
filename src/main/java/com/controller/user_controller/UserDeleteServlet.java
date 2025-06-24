//package com.controller.user_controller;
//
//import com.entity.User;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet("/user/delete")
//public class UserDeleteServlet extends BaseUserServlet {
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String id = request.getParameter("id");
//        
//        if (id == null || id.trim().isEmpty()) {
//            request.setAttribute("error", "User ID is required");
//            response.sendRedirect("list");
//            return;
//        }
//        
//        User user = userDao.findById(id);
//        if (user == null) {
//            request.setAttribute("error", "User not found with ID: " + id);
//            response.sendRedirect("list");
//            return;
//        }
//        
//        try {
//            userDao.delete(user);
////            request.setAttribute("success", "User '" + user.getFullName() + "' has been deleted successfully");
//        } catch (Exception e) {
//            String errorMessage = "Cannot delete user '" + user.getFullName() + "': ";
//            if (e.getCause() != null) {
//                errorMessage += e.getCause().getMessage();
//            } else {
//                errorMessage += e.getMessage();
//            }
//            request.setAttribute("error", errorMessage);
//        }
//        
//        response.sendRedirect("list");
//    }
//} 