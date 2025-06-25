<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Change Division</title>
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
                            <form action="changeDivision" method="post">
                                <input type="hidden" name="userId" value="${user.userId}"/>
                                <div class="mb-3">
                                    <label for="divisionId" class="form-label">New Division:</label>
                                    <select id="divisionId" name="divisionId" class="form-select" required>
                                        <option value="">Select Division</option>
                                        <c:forEach items="${divisions}" var="division">
                                            <option value="${division.divisionId}" 
                                                    ${user.divisionId == division.divisionId ? 'selected' : ''}>
                                                ${division.divisionName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-primary">Change Division</button>
                                <a href="${pageContext.request.contextPath}/user/list" class="btn btn-secondary">Cancel</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 