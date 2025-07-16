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
                        <h4>User Management</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <p class="error">${error}</p>
                        </c:if>
                        <c:if test="${not empty sessionScope.successMessage}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                ${sessionScope.successMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                            <% session.removeAttribute("successMessage");%>
                        </c:if>
                        <div class="mb-3">
                            <form method="get" action="${pageContext.request.contextPath}/user/list" class="row g-2 align-items-end">
                                <c:if test="${isAdmin}">
                                    <div class="col-md-4">
                                        <label for="divisionId" class="form-label">Division</label>
                                        <select id="divisionId" name="divisionId" class="form-select">
                                            <option value="">All Divisions</option>
                                            <c:forEach var="division" items="${divisions}">
                                                <option value="${division.divisionId}" ${selectedDivisionId != null && selectedDivisionId == division.divisionId ? 'selected' : ''}>
                                                    ${division.divisionName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </c:if>
                                <div class="col-md-4">
                                    <label for="roleId" class="form-label">Role</label>
                                    <select id="roleId" name="roleId" class="form-select">
                                        <option value="">All Roles</option>
                                        <c:forEach var="role" items="${roles}">
                                            <option value="${role.roleId}" ${selectedRoleId != null && selectedRoleId == role.roleId ? 'selected' : ''}>
                                                ${role.roleName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <button type="submit" class="btn btn-primary">Search</button>
                                    <a href="${pageContext.request.contextPath}/user/list" class="btn btn-secondary ms-2">Reset</a>
                                </div>
                            </form>
                        </div>
                        <div class="mb-3">
                            <a href="${pageContext.request.contextPath}/user/create" class="btn btn-primary">Add New User</a>
                        </div>
                        <div class="scrollable-list">
                            <table class="table table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <!--<th>ID</th>-->
                                        <th>Full Name</th>
                                        <th>Username</th>
                                        <!--                            <th>Email</th>
                                                                    <th>Gender</th>-->
                                        <th>Division</th>
                                        <th>Roles</th>
                                        <th>Manager</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="user" items="${users}">
                                        <tr>
                                            <!--<td>${user.userId}</td>-->
                                            <td>${user.fullName}</td>
                                            <td>${user.username}</td>
<!--                                <td>${user.email}</td>
                                <td>
                                            <c:choose>
                                                <c:when test="${user.gender == true}">Male</c:when>
                                                <c:when test="${user.gender == false}">Female</c:when>
                                                <c:otherwise>Unknown</c:otherwise>
                                            </c:choose>
                                        </td>-->
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty user.division}">
                                                        ${user.division.divisionName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        N/A
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge bg-dark text-white me-1">${user.role.roleName}</span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty user.manager}">
                                                        ${user.manager.fullName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        N/A
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="edit?id=${user.userId}" class="btn btn-sm btn-secondary">Edit</a>
                                                <a href="${pageContext.request.contextPath}/user/changedivision?id=${user.userId}" class="btn btn-sm btn-warning ms-1">Change Division</a>
                                                <a href="${pageContext.request.contextPath}/user/role?userId=${user.userId}" class="btn btn-sm btn-info ms-1">Change Role</a>
                                                <form action="delete" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this user?');">
                                                    <input type="hidden" name="id" value="${user.userId}" />
                                                    <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>