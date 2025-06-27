<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <%@ include file="/view/common_jsp_components/navbar.jspf" %>
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
                                        <c:otherwise> N/A </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="edit?id=${division.divisionId}" class="btn btn-sm btn-secondary">Edit</a>
                                    <form action="delete" method="post" style="display: inline" onsubmit="return confirm('Are you sure you want to delete this division?');">
                                        <input type="hidden" name="id" value="${division.divisionId}" />
                                        <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${divisionTotalPages > 1}">
                    <nav>
                        <ul class="pagination">
                            <c:forEach begin="1" end="${divisionTotalPages}" var="i">
                                <li class="page-item ${i == divisionCurrentPage ? 'active' : ''}">
                                    <a class="page-link" href="?divisionPage=${i}">${i}</a>
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
