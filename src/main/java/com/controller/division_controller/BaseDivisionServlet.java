package com.controller.division_controller;

import com.dao.DivisionDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public abstract class BaseDivisionServlet extends HttpServlet {
    protected DivisionDao divisionDao;

    @Override
    public void init() throws ServletException {
        divisionDao = new DivisionDao();
    }
} 