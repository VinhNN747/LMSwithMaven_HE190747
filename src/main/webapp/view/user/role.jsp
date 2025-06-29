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
                            <h4>Change Role for User: ${targetUser.fullName}</h4>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty error}">
                                <p class="error">${error}</p>
                            </c:if>
                            
                            <div class="mb-3">
                                <strong>Current Role:</strong>
                                <c:choose>
                                    <c:when test="${currentRole != null}">
                                        <span class="badge bg-primary">${currentRole.roleName}</span>
                                        <c:if test="${currentRole.roleLevel != null}">
                                            <small class="text-muted">(Level ${currentRole.roleLevel})</small>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">No role assigned</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            
                            <c:if test="${not empty targetUser.manager}">
                                <div class="alert alert-warning" role="alert">
                                    <strong>Note:</strong> If you assign a role with a higher level than the user's current manager 
                                    (${targetUser.manager.fullName}), the manager assignment will be automatically removed to maintain 
                                    organizational hierarchy.
                                </div>
                            </c:if>
                            
                            <form action="role" method="post">
                                <input type="hidden" name="userId" value="${targetUser.userId}"/>
                                
                                <div class="mb-3">
                                    <label for="roleId" class="form-label">Select New Role:</label>
                                    <select id="roleId" name="roleId" class="form-select" required>
                                        <option value="">Choose a role...</option>
                                        <c:forEach items="${allRoles}" var="role">
                                            <option value="${role.roleId}" 
                                                    ${currentRole != null && currentRole.roleId.equals(role.roleId) ? 'selected' : ''}>
                                                ${role.roleName}
                                                <c:if test="${role.roleLevel != null}"> (Level ${role.roleLevel})</c:if>
                                                <c:if test="${not empty role.roleDescription}"> - ${role.roleDescription}</c:if>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <button type="submit" class="btn btn-primary">Update Role</button>
                                <a href="${pageContext.request.contextPath}/user/list" class="btn btn-secondary">Cancel</a>
                                <a href="${pageContext.request.contextPath}/user/edit?id=${targetUser.userId}" class="btn btn-warning ms-2">Edit User</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html> 