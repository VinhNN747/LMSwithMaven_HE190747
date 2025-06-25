package com.controller.controller_user;

import com.controller.controller_authorization.AuthorizationServlet;
import com.dao.DivisionDao;
import com.dao.UserDao;
import com.entity.User;

public abstract class UserBaseServlet extends AuthorizationServlet {

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
            return "Password must be at least 1 characters.";
        }
        if (!isFullNameValid(user.getFullName())) {
            return "Full name is required.";
        }
        if (!isUniqueUsername(user.getUsername())) {
            return "Username has already been taken";
        }
        if (!isUniqueEmail(user.getEmail())) {
            return "Email has already been taken";
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
        return password != null && password.length() >= 1;
    }

    protected boolean isFullNameValid(String fullName) {
        return fullName != null && !fullName.trim().isEmpty();
    }

    protected boolean isUniqueUsername(String userName) {
        return !udb.existsByUsername(userName);
    }

    protected boolean isUniqueEmail(String email) {
        return !udb.existsByEmail(email);
    }
}
