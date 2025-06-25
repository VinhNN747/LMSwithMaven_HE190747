<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="isAdmin" value="false" />
<c:forEach items="${sessionScope.roles}" var="role">
    <c:if test="${role.roleName == 'ADMIN'}">
        <c:set var="isAdmin" value="true" />
    </c:if>
</c:forEach>

<!DOCTYPE html>
<html>
    <head>
        <title>Dashboard</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark fixed-top">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">LMS Dashboard</a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        Welcome, ${sessionScope.user.fullName}
                    </span>
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                </div>
            </div>
        </nav>

        <div class="container">
            <h2 class="page-title">Welcome to the Leave Management System</h2>
            <p>You are logged in as: <strong>${sessionScope.user.fullName}</strong></p>

            <c:if test="${isAdmin}">
                <div class="row">
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-header">
                                User Management
                            </div>
                            <div class="card-body">
                                <a href="${pageContext.request.contextPath}/user/list" class="btn btn-primary">Manage Users</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-header">
                                Division Management
                            </div>
                            <div class="card-body">
                                <a href="${pageContext.request.contextPath}/division/list" class="btn btn-primary">Manage Divisions</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-header">
                                Leave Management
                            </div>
                            <div class="card-body">
                                <a href="${pageContext.request.contextPath}/leaverequest" class="btn btn-primary">Manage Leave Requests</a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="card mt-4">
                <div class="card-header">
                    Your Roles
                </div>
                <div class="card-body">
                    <c:forEach var="role" items="${sessionScope.roles}">
                        <span class="badge bg-dark text-white me-2">${role.roleName}</span>
                    </c:forEach>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 