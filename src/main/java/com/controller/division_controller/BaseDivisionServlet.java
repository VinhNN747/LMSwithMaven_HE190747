package com.controller.division_controller;

import com.dao.DivisionDao;
import com.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public abstract class BaseDivisionServlet extends HttpServlet {
    protected DivisionDao divisionDao;
    protected UserDao userDao;
    
    // Role hierarchy constants
    protected static final String ROLE_EMPLOYEE = "Employee";
    protected static final String ROLE_LEAD = "Lead";
    protected static final String ROLE_HEAD = "Head";

    @Override
    public void init() throws ServletException {
        divisionDao = new DivisionDao();
        userDao = new UserDao();
    }
} 