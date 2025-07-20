package com.controller.controller_agenda;

import com.dao.LeaveRequestDao;
import com.dao.UserDao;
import com.entity.LeaveRequest;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.controller.controller_authorization.AuthorizationServlet;

@WebServlet(name = "AgendaServlet", urlPatterns = "/agenda")
public class AgendaServlet extends AuthorizationServlet {

    private final UserDao userDao = new UserDao();
    private final LeaveRequestDao leaveRequestDao = new LeaveRequestDao();

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date fromDate = cal.getTime();
        cal.add(Calendar.DATE, 9);
        Date toDate = cal.getTime();
        showAgenda(request, response, user, fromDate, toDate, null);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String fromStr = request.getParameter("from");
        String toStr = request.getParameter("to");
        String rangeMessage = null;
        Date fromDate, toDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            fromDate = sdf.parse(fromStr);
            toDate = sdf.parse(toStr);
            long diff = (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24) + 1;
            if (diff > 10) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fromDate);
                cal.add(Calendar.DATE, 9);
                toDate = cal.getTime();
                rangeMessage = "Date range cannot exceed 10 days. Only the first 10 days are shown.";
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
            return;
        }
        showAgenda(request, response, user, fromDate, toDate, rangeMessage);
    }

    private void showAgenda(HttpServletRequest request, HttpServletResponse response, User user, Date fromDate, Date toDate, String rangeMessage) throws ServletException, IOException {
        Integer divisionId = user.getDivisionId();
        List<User> users = userDao.getUsersByDivision(divisionId);
        List<Date> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        while (!cal.getTime().after(toDate)) {
            dateList.add(cal.getTime());
            cal.add(Calendar.DATE, 1);
        }
        List<LeaveRequest> leaveRequests = leaveRequestDao.listRequests(null, "Approved", null, divisionId, null, null, fromDate, toDate);
        Map<Integer, Map<String, String>> statusMap = new HashMap<>();
        SimpleDateFormat keyFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (User u : users) {
            Map<String, String> userStatus = new HashMap<>();
            for (Date d : dateList) {
                boolean isLeave = false;
                for (LeaveRequest lr : leaveRequests) {
                    if (lr.getSenderId().equals(u.getUserId()) && !d.before(lr.getStartDate()) && !d.after(lr.getEndDate())) {
                        isLeave = true;
                        break;
                    }
                }
                userStatus.put(keyFormat.format(d), isLeave ? "leave" : "work");
            }
            statusMap.put(u.getUserId(), userStatus);
        }
        request.setAttribute("userList", users);
        request.setAttribute("dateList", dateList);
        request.setAttribute("statusMap", statusMap);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        if (rangeMessage != null) {
            request.setAttribute("rangeMessage", rangeMessage);
        }
        request.getRequestDispatcher("/view/agenda/agenda.jsp").forward(request, response);
    }
}
