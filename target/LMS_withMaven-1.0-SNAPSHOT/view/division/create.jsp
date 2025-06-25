<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Add New Division</title>
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
                            <h4>Add New Division</h4>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty error}">
                                <p class="error">${error}</p>
                            </c:if>
                            <form action="create" method="post">
                                <div class="mb-3">
                                    <label for="divisionName" class="form-label">Division Name:</label>
                                    <input type="text" id="divisionName" name="divisionName" class="form-control" maxlength="50" required/>
                                </div>
                                <button type="submit" class="btn btn-primary">Save Division</button>
                                <a href="${pageContext.request.contextPath}/division/list" class="btn btn-secondary">Cancel</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
