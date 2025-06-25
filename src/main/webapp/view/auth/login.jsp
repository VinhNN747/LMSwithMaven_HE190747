<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <style>
            body, html {
                height: 100%;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .login-card {
                width: 100%;
                max-width: 400px;
            }
        </style>
    </head>
    <body class="text-center">
        <div class="login-card">
            <div class="card">
                <div class="card-header">
                    <h4>LMS Login</h4>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label visually-hidden">Username</label>
                            <input type="text" class="form-control" id="username" name="username" placeholder="Username" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label visually-hidden">Password</label>
                            <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
                        </div>

                        <c:if test="${not empty error and fn:trim(error) ne ''}">
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </c:if>


                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">Login</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html> 