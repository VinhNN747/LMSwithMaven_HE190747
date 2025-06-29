/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.controller_role;

import com.controller.controller_authorization.AuthorizationServlet;
import com.dao.RoleDao;
import com.entity.Role;
import com.entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author vinhnnpc
 */
@WebServlet(name = "RoleBaseServlet")
public abstract class RoleBaseServlet extends AuthorizationServlet {

    protected RoleDao rdb = new RoleDao();

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    protected String validateRole(Role role) {
        if (role == null) {
            return "Role cannot be null";
        }
        if (!isValidRoleName(role.getRoleName())) {
            return "Role name is invalid";
        }
        if (!isUniqueRoleName(role.getRoleName(), role.getRoleId())) {
            return "Role name has already been taken";
        }
        if (!isValidRoleLevel(role.getRoleLevel())) {
            return "Role level is invalid";
        }
        return null;
    }

    private boolean isValidRoleName(String roleName) {
        return roleName != null && !roleName.trim().isEmpty() && roleName.length() <= 50;
    }

    private boolean isUniqueRoleName(String roleName, Integer roleId) {
        // If roleId is null, this is a new role
        // If roleId is not null, this is an update - check uniqueness excluding current role
        return !rdb.existsByName(roleName, roleId);
    }

    private boolean isValidRoleLevel(Integer roleLevel) {
        return roleLevel == null || (roleLevel >= 1 && roleLevel <= 10); // Assuming role levels 1-10
    }

    protected boolean hasPermission(User user, String permission) {
        // Check if user has the specific permission through their roles
        return rdb.hasPermission(user.getUserId(), permission);
    }
}
