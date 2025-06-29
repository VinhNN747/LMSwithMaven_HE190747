<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <%@ include file="/view/common_jsp_components/navbar.jspf" %>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">
                            <h4>Change Division for ${user.fullName}</h4>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty error}">
                                <p class="error">${error}</p>
                            </c:if>
                            
                            <div class="alert alert-warning" role="alert">
                                <h5><i class="fas fa-exclamation-triangle"></i> Important Notice</h5>
                                <p><strong>Changing a user's division will affect organizational relationships:</strong></p>
                                <ul>
                                    <li><strong>Subordinates:</strong> If this user manages other employees, their manager relationships will be removed</li>
                                    <li><strong>Manager:</strong> If this user has a manager in a different division, the manager relationship will be removed</li>
                                    <li><strong>Reassignment:</strong> You may need to manually reassign manager relationships after the change</li>
                                </ul>
                            </div>
                            
                            <c:if test="${not empty currentRole && currentRole.roleLevel == 99}">
                                <div class="alert alert-danger" role="alert">
                                    <h5><i class="fas fa-exclamation-triangle"></i> Division Head Restriction</h5>
                                    <p><strong>This user is a Division Head (Level 99).</strong></p>
                                    <ul>
                                        <li>Division Heads cannot be moved to different divisions</li>
                                        <li>To change divisions, first remove the Division Head role</li>
                                        <li>Then assign a new Division Head to the current division</li>
                                    </ul>
                                </div>
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
                                <c:choose>
                                    <c:when test="${not empty currentRole && currentRole.roleLevel == 99}">
                                        <button type="button" class="btn btn-secondary" disabled>
                                            Cannot Move Division Head
                                        </button>
                                        <small class="form-text text-muted d-block mt-2">
                                            Division Heads must remain in their assigned division. Remove the Division Head role first to change divisions.
                                        </small>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" class="btn btn-primary">Change Division</button>
                                    </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/user/list" class="btn btn-secondary">Cancel</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html> 