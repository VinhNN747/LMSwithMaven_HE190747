<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <%@ include file="/view/common_jsp_components/navbar.jspf" %>
        <div class="container">
            <h2 class="page-title">User Management</h2>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% session.removeAttribute("successMessage");%>
            </c:if>
            <div class="mb-3">
                <a href="${pageContext.request.contextPath}/user/create" class="btn btn-primary">Add New User</a>
            </div>
            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <!--<th>ID</th>-->
                            <th>Full Name</th>
                            <th>Username</th>
<!--                            <th>Email</th>
                            <th>Gender</th>-->
                            <th>Division</th>
                            <th>Roles</th>
                            <th>Manager</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <!--<td>${user.userId}</td>-->
                                <td>${user.fullName}</td>
                                <td>${user.username}</td>
<!--                                <td>${user.email}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.gender == true}">Male</c:when>
                                        <c:when test="${user.gender == false}">Female</c:when>
                                        <c:otherwise>Unknown</c:otherwise>
                                    </c:choose>
                                </td>-->
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty user.division}">
                                            ${user.division.divisionName}
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:forEach var="userRole" items="${user.userRoles}">
                                        <span class="badge bg-dark text-white me-1">${userRole.role.roleName}</span>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty user.manager}">
                                            ${user.manager.fullName}
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="edit?id=${user.userId}" class="btn btn-sm btn-secondary">Edit</a>
                                    <a href="${pageContext.request.contextPath}/user/changedivision?id=${user.userId}" class="btn btn-sm btn-warning ms-1">Change Division</a>
                                    <a href="${pageContext.request.contextPath}/user/role?userId=${user.userId}" class="btn btn-sm btn-info ms-1">Change Role</a>
                                    <form action="delete" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this user?');">
                                        <input type="hidden" name="id" value="${user.userId}" />
                                        <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${userTotalPages > 1}">
                    <nav>
                        <ul class="pagination">
                            <c:forEach begin="1" end="${userTotalPages}" var="i">
                                <li class="page-item ${i == userCurrentPage ? 'active' : ''}">
                                    <a class="page-link" href="?userPage=${i}">${i}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>