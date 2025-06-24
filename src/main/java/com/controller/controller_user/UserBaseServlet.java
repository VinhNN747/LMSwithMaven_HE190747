package com.controller.controller_user;

import com.dao.DivisionDao;
import com.dao.UserDao;
import com.entity.User;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserBaseServlet extends HttpServlet {

    /**
     * Validates the basic fields of a new User entity. Returns null if valid,
     * or an error message if invalid.
     */
    UserDao udb = new UserDao();
    DivisionDao ddb = new DivisionDao();

    protected String validateNewUser(User user) {
        if (user == null) {
            return "User cannot be null.";
        }
        if (!isUsernameValid(user.getUsername())) {
            return "Username is required.";
        }
        if (!isEmailPresent(user.getEmail())) {
            return "Email is required.";
        }
        if (!isEmailFormatValid(user.getEmail())) {
            return "Invalid email format.";
        }
        if (!isPasswordValid(user.getPassword())) {
            return "Password must be at least 6 characters.";
        }
        if (!isFullNameValid(user.getFullName())) {
            return "Full name is required.";
        }
        // Add more validations as needed
        return null;
    }

    protected boolean isUsernameValid(String username) {
        return username != null && !username.trim().isEmpty();
    }

    protected boolean isEmailPresent(String email) {
        return email != null && !email.trim().isEmpty();
    }

    protected boolean isEmailFormatValid(String email) {
        return email != null && email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$");
    }

    protected boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
    }

    protected boolean isFullNameValid(String fullName) {
        return fullName != null && !fullName.trim().isEmpty();
    }

    /**
     * Handles errors by setting an error message attribute and forwarding to an
     * error page.
     */
    protected void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage,
            String destination) {
        try {
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher(destination).forward(request, response);
        } catch (Exception e) {
            // If forwarding fails, print stack trace (or log)
            e.printStackTrace();
        }
    }
}
