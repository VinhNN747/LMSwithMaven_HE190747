<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("activeTab", "role");
%>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <div class="d-flex">
            <%@ include file="/view/common_jsp_components/sidebar.jspf" %>
            <main class="main-content flex-grow-1">
                <div class="container">
                    <div class="row justify-content-center">
                        <div class="col-md-8">
                            <div class="card">
                                <div class="card-header">
                                    <h4>Assign Features to Role: ${role.roleName}</h4>
                                </div>
                                <div class="card-body">
                                    <c:if test="${not empty error}">
                                        <p class="error">${error}</p>
                                    </c:if>
                                    <form action="assignFeatures" method="post">
                                        <input type="hidden" name="roleId" value="${role.roleId}"/>

                                        <div class="mb-3">
                                            <label class="form-label">Select Features:</label>
                                            <div class="row">
                                                <c:forEach items="${allFeatures}" var="feature">
                                                    <div class="col-md-6 mb-2">
                                                        <div class="form-check">
                                                            <input class="form-check-input" type="checkbox" 
                                                                   name="featureIds" value="${feature.featureId}" 
                                                                   id="feature_${feature.featureId}"
                                                                   ${assignedFeatureIds.contains(feature.featureId) ? 'checked' : ''}>
                                                            <label class="form-check-label" for="feature_${feature.featureId}">
                                                                ${feature.featureName}
                                                                <small class="text-muted d-block">${feature.endpoint}</small>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>

                                        <button type="submit" class="btn btn-primary">Save Assignments</button>
                                        <a href="${pageContext.request.contextPath}/role/list" class="btn btn-secondary">Cancel</a>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html> 