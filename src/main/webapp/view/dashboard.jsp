<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="auth" uri="http://example.com/auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Dashboard</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <body class="bg-light">
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
            <div class="container">
                <a class="navbar-brand" href="#">LMS Dashboard</a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        Welcome, ${sessionScope.user.fullName}
                    </span>
                    <a class="nav-link" href="logout">Logout</a>
                </div>
            </div>
        </nav>

        <div class="container mt-4">
            <div class="row">
                <div class="col-md-12">
                    <h2>Welcome to the Leave Management System</h2>
                    <p>You are logged in as: <strong>${sessionScope.user.fullName}</strong></p>


                </div>
            </div>

            <div class="row mt-4">
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5>User Management</h5>
                        </div>

                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5>Division Management</h5>
                        </div>

                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5>Leave Management</h5>
                        </div>

                    </div>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <h5>Your Permissions</h5>
                        </div>
                        <div class="card-body">
                            <c:forEach var="permission" items="${sessionScope.permissions}">
                                <span class="badge bg-secondary me-2">${permission.endpoint}</span>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 