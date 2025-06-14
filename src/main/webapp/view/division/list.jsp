<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.entity.Division"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<html>
    <head><title>All Divisions</title></head>
    <body>
        <h2>Division List</h2>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Director</th>
            </tr>
            <c:forEach var="d" items="${requestScope.divisions}">
                <tr>
                    <td><c:out value="${d.divisionId}" /></td>
                    <td><c:out value="${d.divisionName}" /></td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty d.divisionDirector}">
                                <c:out value="${d.divisionDirector}" />
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
