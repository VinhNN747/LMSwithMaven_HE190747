<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("activeTab", "user");
%>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <div class="d-flex">
            <!-- Sidebar -->
            <%@ include file="/view/common_jsp_components/sidebar.jspf" %>

            <!-- Main Content -->
            <main class="main-content flex-grow-1 p-4">
                <div class="card">
                    <div class="card-header">
                        <h4>Change Division for ${user.fullName}</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <p class="error">${error}</p>
                        </c:if>


                        <div class="card mb-3">
                            <div class="card-header">
                                <h6>Current Organizational Relationships</h6>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-4">
                                        <strong>Current Manager:</strong>
                                        <c:choose>
                                            <c:when test="${not empty user.manager}">
                                                <span class="badge bg-info">${user.manager.fullName}</span>
                                                <small class="text-muted d-block">(${user.manager.division.divisionName})</small>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">No manager assigned</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="col-md-4">
                                        <strong>Current Division:</strong>
                                        <c:choose>
                                            <c:when test="${not empty user.division}">
                                                <span class="badge bg-primary">${user.division.divisionName}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">No division assigned</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="col-md-4">
                                        <strong>Subordinates:</strong>
                                        <c:choose>
                                            <c:when test="${not empty subordinates}">
                                                <span class="badge bg-warning">${subordinates.size()} subordinate(s)</span>
                                                <small class="text-muted d-block">Will lose manager relationship</small>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">No subordinates</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <form action="changedivision" method="post">
                            <input type="hidden" name="userId" value="${user.userId}"/>
                            <div class="mb-3">
                                <label for="divisionId" class="form-label">New Division:</label>
                                <select id="divisionId" name="divisionId" class="form-select" required>
                                    <option value="">Select Division</option>
                                    <c:forEach items="${divisions}" var="division">
                                        <option value="${division.divisionId}" 
                                                ${user.divisionId == division.divisionId ? 'disabled' : ''}>
                                            ${division.divisionName}
                                            <c:if test="${user.divisionId == division.divisionId}"> (Current)</c:if>
                                            </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <button type="submit" class="btn btn-primary">Change Division</button>

                            <a href="${pageContext.request.contextPath}/user/list" class="btn btn-secondary">Cancel</a>
                        </form>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html> 