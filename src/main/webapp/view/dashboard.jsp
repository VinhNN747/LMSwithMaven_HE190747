<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="isAdmin" value="false" />
<c:set var="isEmployee" value="false" />
<c:set var="isLead" value="false" />
<c:set var="isHead" value="false" />
<c:set var="hasOtherRole" value="false" />
<c:forEach items="${sessionScope.roles}" var="role">
    <c:if test="${role.roleName == 'ADMIN'}">
        <c:set var="isAdmin" value="true" />
    </c:if>
    <c:if test="${role.roleName == 'Employee'}">
        <c:set var="isEmployee" value="true" />
    </c:if>
    <c:if test="${role.roleName == 'Lead'}">
        <c:set var="isLead" value="true" />
    </c:if>
    <c:if test="${role.roleName == 'Head'}">
        <c:set var="isHead" value="true" />
    </c:if>
    <c:if
        test="${role.roleName != 'Employee' && role.roleName != 'Lead' && role.roleName != 'Head' && role.roleName != 'ADMIN'}">
        <c:set var="hasOtherRole" value="true" />
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
                <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">LMS Dashboard</a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        Welcome, ${sessionScope.user.fullName}, Your role(s):
                        <c:forEach var="role" items="${sessionScope.roles}">
                            ${role.roleName}
                        </c:forEach>
                    </span>
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                </div>
            </div>
        </nav>

        <div class="container">
            <h2 class="page-title text-center">Welcome to the Leave Management System</h2>

            <!-- Admin Dashboard -->
            <c:if test="${isAdmin}">
                <div class="row">
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-header">
                                User Management
                            </div>
                            <div class="card-body">
                                <a href="${pageContext.request.contextPath}/user/list"
                                   class="btn btn-primary">Manage Users</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-header">
                                Division Management
                            </div>
                            <div class="card-body">
                                <a href="${pageContext.request.contextPath}/division/list"
                                   class="btn btn-primary">Manage Divisions</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-header">
                                Leave Management
                            </div>
                            <div class="card-body">
                                <a href="${pageContext.request.contextPath}/leaverequest"
                                   class="btn btn-primary">Manage Leave Requests</a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Employee Dashboard -->
            <c:if test="${isEmployee and not isAdmin and not isLead and not isHead}">
                <div class="row mt-4">
                    <div class="col-md-6">
                        <%@ include file="/view/leaverequest/create.jsp" %>
                    </div>
                    <div class="col-md-6">
                        <%@ include file="/view/leaverequest/myrequests.jsp" %>
                    </div>
                </div>
            </c:if>

            <!-- Lead/Head Dashboard -->
            <c:if test="${isLead or isHead}">
                <div class="row mt-4">
                    <div class="col-md-4">
                        <%@ include file="/view/leaverequest/create.jsp" %>
                    </div>
                    <div class="col-md-4">
                        <%@ include file="/view/leaverequest/myrequests.jsp" %>
                    </div>
                    <div class="col-md-4">
                        <%@ include file="/view/leaverequest/subsrequests.jsp" %>
                    </div>
                </div>
            </c:if>

            <!-- Fallback for users with no specific role -->
            <c:if test="${not isAdmin and not isEmployee and not isLead and not isHead}">
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                Welcome
                            </div>
                            <div class="card-body">
                                <p>Welcome to the Leave Management System. Please contact your administrator to assign appropriate roles.</p>
                                <p>Your current roles: 
                                    <c:forEach var="role" items="${sessionScope.roles}">
                                        <span class="badge">${role.roleName}</span>
                                    </c:forEach>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>