<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("activeTab", "role");
%>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <div class="d-flex">
            <!-- Sidebar -->
            <%@ include file="/view/common_jsp_components/sidebar.jspf" %>

            <!-- Main Content -->
            <main class="main-content flex-grow-1 p-4">
                <div class="card">
                    <div class="card-header">
                        <h4>Role Management</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                            <% session.removeAttribute("error"); %>
                        </c:if>
                        <c:if test="${not empty sessionScope.error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${sessionScope.error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                            <% session.removeAttribute("error"); %>
                        </c:if>
                        <c:if test="${not empty sessionScope.successMessage}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                ${sessionScope.successMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                            <% session.removeAttribute("successMessage");%>
                        </c:if>
                        <div class="mb-3">
                            <a href="${pageContext.request.contextPath}/role/create" class="btn btn-primary">Add New Role</a>
                        </div>
                        <div class="scrollable-list">
                            <table class="table table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <!--<th>ID</th>-->
                                        <th>Role Name</th>
                                        <th>Description</th>
                                        <th>Level</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="role" items="${roles}">
                                        <tr>
                                            <!--<td>${role.roleId}</td>-->
                                            <td>${role.roleName}</td>
                                            <td>${role.roleDescription}</td>
                                            <td>${role.roleLevel}</td>

                                            <td>
                                                <c:choose>
                                                    <c:when test="${role.roleLevel == 99}">
                                                        <span class="badge bg-warning text-dark">Protected Role</span>
                                                        <small class="text-muted d-block">Cannot be edited or deleted</small>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="edit?id=${role.roleId}" class="btn btn-sm btn-secondary">Edit</a>
                                                        <form action="${pageContext.request.contextPath}/role/delete" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this role?');">
                                                            <input type="hidden" name="id" value="${role.roleId}" />
                                                            <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                                <a href="${pageContext.request.contextPath}/role/assignFeatures?id=${role.roleId}" class="btn btn-sm btn-warning ms-1">Assign Features</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html> 