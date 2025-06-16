<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Division List</title>
        <style>
            table {
                border-collapse: collapse;
                width: 80%;
                margin: 20px auto;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 8px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            a {
                text-decoration: none;
                color: #0066cc;
            }
            a:hover {
                text-decoration: underline;
            }
            .button {
                padding: 5px 10px;
                background-color: #0066cc;
                color: white;
                border-radius: 3px;
            }
            .error {
                color: red;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <h2 style="text-align: center;">Division List</h2>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <div style="text-align: center; margin-bottom: 10px;">
            <a href="create" class="button">Add New Division</a>
        </div>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Director</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="division" items="${divisions}">
                <tr>
                    <td>${division.divisionId}</td>
                    <td>${division.divisionName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty division.director}">
                                ${division.director.fullName}
                            </c:when>
                            <c:otherwise>
                                ${division.director}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="edit?id=${division.divisionId}">Edit</a>
                         | 
                        <form action="delete" method="post" style="display: inline;">
                            <input type="hidden" name="id" value="${division.divisionId}"/>
                            <a href="#" onclick="if(confirm('Are you sure you want to delete this division?')) { this.parentElement.submit(); } return false;">Delete</a>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
