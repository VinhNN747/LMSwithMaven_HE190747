<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <title>My Leave Requests</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>

    <body>
        <nav class="navbar navbar-expand-lg navbar-dark fixed-top">
            <div class="container-fluid">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/view/dashboard.jsp">LMS
                    Dashboard</a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        Welcome, ${sessionScope.user.fullName}, Your role(s):
                        <c:forEach var="role" items="${sessionScope.roles}">
                            ${role.roleName}
                        </c:forEach>                    </span>
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                </div>
            </div>
        </nav>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-10">
                    <div class="card mt-5">
                        <div class="card-header">
                            <h4>All Leave Requests</h4>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty allRequests}">
                                    <div class="alert alert-info">No leave requests found</div>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="requests" value="${allRequests}" scope="request" />
                                    <div class="card-body">
                                        <%@ include file="/view/leaverequest/requests-table.jsp" %>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>