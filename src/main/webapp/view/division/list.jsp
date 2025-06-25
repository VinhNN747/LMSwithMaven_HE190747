<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Division List</title>
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
            <h2 class="page-title">Division Management</h2>

            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <div class="mb-3">
                <a href="${pageContext.request.contextPath}/division/create" class="btn btn-primary">Add New Division</a>
            </div>

            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Division Head</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="division" items="${divisions}">
                            <tr>
                                <td>${division.divisionId}</td>
                                <td>${division.divisionName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty division.head}">
                                            ${division.head.fullName}
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="edit?id=${division.divisionId}" class="btn btn-sm btn-secondary">Edit</a>
                                    <form action="delete" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this division?');">
                                        <input type="hidden" name="id" value="${division.divisionId}"/>
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
