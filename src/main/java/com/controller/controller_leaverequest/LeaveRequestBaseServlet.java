/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_leaverequest;

import com.controller.controller_authorization.AuthorizationServlet;
import com.dao.LeaveRequestDao;
import java.util.Date;

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

}
