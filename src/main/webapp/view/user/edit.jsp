<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit User</title>
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
                            <h4>Edit User: ${user.fullName}</h4>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty error}">
                                <p class="error">${error}</p>
                            </c:if>
                            <form action="edit" method="post">
                                <input type="hidden" name="userId" value="${user.userId}"/>
                                
                                <div class="mb-3">
                                    <label for="fullName" class="form-label">Full Name:</label>
                                    <input type="text" id="fullName" name="fullName" class="form-control" value="${user.fullName}" maxlength="100" required/>
                                </div>
                                <div class="mb-3">
                                    <label for="username" class="form-label">Username:</label>
                                    <input type="text" id="username" name="username" class="form-control" value="${user.username}" maxlength="50" required/>
                                </div>
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email:</label>
                                    <input type="email" id="email" name="email" class="form-control" value="${user.email}" maxlength="100" required/>
                                </div>
                                <div class="mb-3">
                                    <label for="gender" class="form-label">Gender:</label>
                                    <select id="gender" name="gender" class="form-select">
                                        <option value="">Select Gender</option>
                                        <option value="true" ${user.gender != null && user.gender ? 'selected' : ''}>Male</option>
                                        <option value="false" ${user.gender != null && !user.gender ? 'selected' : ''}>Female</option>
                                    </select>
                                </div>

                                <button type="submit" class="btn btn-primary">Save Changes</button>
                                <a href="${pageContext.request.contextPath}/user/list" class="btn btn-secondary">Cancel</a>
                                <a href="${pageContext.request.contextPath}/user/changeDivision?userId=${user.userId}" class="btn btn-warning ms-2">Change Division</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
