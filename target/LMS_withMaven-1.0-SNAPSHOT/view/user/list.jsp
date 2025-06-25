<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <title>User List</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>

    <body>
        <nav class="navbar navbar-expand-lg navbar-dark fixed-top">
            <div class="container-fluid">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/view/dashboard.jsp">LMS Dashboard</a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        Welcome, ${sessionScope.user.fullName}
                    </span>
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                </div>
            </div>
        </nav>

        <div class="container">
            <h2 class="page-title">User Management</h2>
            
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <div class="mb-3">
                <a href="${pageContext.request.contextPath}/user/create" class="btn btn-primary">Add New User</a>
            </div>

            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Full Name</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Gender</th>
                            <th>Division</th>
                            <th>Roles</th>
                            <th>Manager</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <td>${user.userId}</td>
                                <td>${user.fullName}</td>
                                <td>${user.username}</td>
                                <td>${user.email}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.gender == true}">Male</c:when>
                                        <c:when test="${user.gender == false}">Female</c:when>
                                        <c:otherwise>Unknown</c:otherwise>
                                    </c:choose>
                                </td>
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
                                    <c:forEach var="userRole" items="${user.userRoles}">
                                        <span class="badge bg-dark text-white me-1">${userRole.role.roleName}</span>
                                    </c:forEach>
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

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>