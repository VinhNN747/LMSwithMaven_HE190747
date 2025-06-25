<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Add New User</title>
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
                            <h4>Add New User</h4>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty error}">
                                <p class="error">${error}</p>
                            </c:if>
                            <form action="create" method="post">
                                <div class="mb-3">
                                    <label for="fullName" class="form-label">Full Name:</label>
                                    <input type="text" id="fullName" name="fullName" class="form-control" maxlength="100" required/>
                                </div>
                                <div class="mb-3">
                                    <label for="username" class="form-label">Username:</label>
                                    <input type="text" id="username" name="username" class="form-control" maxlength="50" required/>
                                </div>

                                <div class="mb-3">
                                    <label for="password" class="form-label">Password:</label>
                                    <input type="text" id="password" name="password" class="form-control" maxlength="50" required/>
                                </div>
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email:</label>
                                    <input type="email" id="email" name="email" class="form-control" maxlength="100" required/>
                                </div>
                                <div class="mb-3">
                                    <label for="gender" class="form-label">Gender:</label>
                                    <select id="gender" name="gender" class="form-select">
                                        <option value="">Select Gender</option>
                                        <option value="true">Male</option>
                                        <option value="false">Female</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="divisionId" class="form-label">Division:</label>
                                    <select id="divisionId" name="divisionId" class="form-select">
                                        <option value="">Select a division</option>
                                        <c:forEach items="${requestScope.divisions}" var="d">
                                            <option value="${d.divisionId}">${d.divisionName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-primary">Save User</button>
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
