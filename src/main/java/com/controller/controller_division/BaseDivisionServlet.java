package com.controller.controller_division;

import com.controller.controller_authorization.AuthorizationServlet;
import com.dao.DivisionDao;
import com.entity.Division;


public abstract class BaseDivisionServlet extends AuthorizationServlet {

    protected DivisionDao ddb = new DivisionDao();

    protected String validateDivision(Division division) {
        if (division == null) {
            return "Division can not be null";
        }
        if (!isValidDivisioName(division.getDivisionName())) {
            return "Division name invalid";
        }
        if (!isUniqueDivisionName(division.getDivisionName())) {
            return "Division name has already been taken";
        }
        return null;
    }

    private boolean isValidDivisioName(String divisionName) {
        return divisionName != null && !divisionName.trim().isEmpty();
    }

    private boolean isUniqueDivisionName(String divisionName) {
        return !ddb.existsByName(divisionName);
    }

}
