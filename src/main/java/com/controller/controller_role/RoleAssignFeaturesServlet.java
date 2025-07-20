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

        Role role = rdb.get(Integer.valueOf(roleIdStr));

        // Get all features
        List<Feature> allFeatures = fdb.list();

        // Get current role features and create a Set of feature IDs for easy checking
        List<Feature> roleFeatures = rdb.getFeaturesByRoleId(role.getRoleId());
        Set<Integer> assignedFeatureIds = new HashSet<>();
        for (Feature feature : roleFeatures) {
            assignedFeatureIds.add(feature.getFeatureId());
        }

        request.setAttribute("role", role);
        request.setAttribute("allFeatures", allFeatures);
        request.setAttribute("assignedFeatureIds", assignedFeatureIds);
        request.getRequestDispatcher("../view/role/assignFeatures.jsp").forward(request, response);

    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String roleIdStr = request.getParameter("roleId");
        String[] featureIds = request.getParameterValues("featureIds");

        Role role = rdb.get(Integer.valueOf(roleIdStr));

        // Clear existing role features
        rdb.clearRoleFeatures(role.getRoleId());

        // Add new role features
        if (featureIds != null) {
            for (String featureIdStr : featureIds) {
                Feature feature = fdb.get(Integer.valueOf(featureIdStr));
                rdb.addRoleFeature(role.getRoleId(), feature.getFeatureId());
            }
        }

        // Redirect to role listRoles on success
        response.sendRedirect("list");

    }
}
