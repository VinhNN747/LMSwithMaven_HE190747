package com.controller.controller_role;

import com.entity.Role;
import com.entity.Feature;
import com.entity.User;
import com.dao.FeatureDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@WebServlet(name = "RoleAssignFeaturesServlet", urlPatterns = {"/role/assignFeatures"})
public class RoleAssignFeaturesServlet extends RoleBaseServlet {

    private FeatureDao fdb = new FeatureDao();

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String roleIdStr = request.getParameter("id");
        
        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/role/list");
            return;
        }

        try {
            Integer roleId = Integer.valueOf(roleIdStr);
            Role role = rdb.findById(roleId);
            
            if (role == null) {
                request.setAttribute("error", "Role not found");
                response.sendRedirect(request.getContextPath() + "/role/list");
                return;
            }

            // Get all features
            List<Feature> allFeatures = fdb.list();
            
            // Get current role features and create a Set of feature IDs for easy checking
            List<Feature> roleFeatures = rdb.getFeaturesByRoleId(roleId);
            Set<Integer> assignedFeatureIds = new HashSet<>();
            for (Feature feature : roleFeatures) {
                assignedFeatureIds.add(feature.getFeatureId());
            }

            request.setAttribute("role", role);
            request.setAttribute("allFeatures", allFeatures);
            request.setAttribute("assignedFeatureIds", assignedFeatureIds);
            request.getRequestDispatcher("/view/role/assignFeatures.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid role ID format");
            response.sendRedirect(request.getContextPath() + "/role/list");
        }
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String roleIdStr = request.getParameter("roleId");
        String[] featureIds = request.getParameterValues("featureIds");
        
        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/role/list");
            return;
        }

        try {
            Integer roleId = Integer.valueOf(roleIdStr);
            Role role = rdb.findById(roleId);
            
            if (role == null) {
                request.setAttribute("error", "Role not found");
                response.sendRedirect(request.getContextPath() + "/role/list");
                return;
            }

            // Clear existing role features
            rdb.clearRoleFeatures(roleId);

            // Add new role features
            if (featureIds != null) {
                for (String featureIdStr : featureIds) {
                    try {
                        Integer featureId = Integer.valueOf(featureIdStr);
                        Feature feature = fdb.findById(featureId);
                        if (feature != null) {
                            rdb.addRoleFeature(roleId, featureId);
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid feature IDs
                        continue;
                    }
                }
            }

            // Redirect to role list on success
            response.sendRedirect(request.getContextPath() + "/role/list");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid role ID format");
            response.sendRedirect(request.getContextPath() + "/role/list");
        } catch (Exception e) {
            request.setAttribute("error", "Failed to assign features: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/role/list");
        }
    }
} 