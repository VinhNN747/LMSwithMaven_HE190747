<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <title>User List</title>
        <style>
            table {
                border-collapse: collapse;
                width: 90%;
                margin: 20px auto;
            }

            th,
            td {
                border: 2px solid #b9b7b7;
                padding: 8px;
                text-align: left;
            }

            th {
                background-color: #d7d5d5;
            }

            /*                tr:nth-child(even) {
                                background-color: #f9f9f9;
                            }*/

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

            /* Role-based colors */
            .role-employee {
                background-color: #d0e4ff; /* Light blue, more saturated */
            }

            .role-lead {
                background-color: #a8ccff; /* Medium blue */
            }

            .role-head {
                background-color: #5ca0ff; /* Deeper blue */
                color: white;
            }

            .role-head a {
                color: white;
            }

            .role-head .button {
                background-color: white;
                color: #0056b3;
                border: 1px solid #0056b3;
            }

            .role-head .button:hover {
                background-color: #d0e4ff;
                color: #0056b3;
            }

        </style>
    </head>

    <body>
        <h2 style="text-align: center;">User List</h2>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <div style="text-align: center; margin-bottom: 10px;">
            <a href="create" class="button">Add New User</a>
        </div>
        <table>
            <tr>
                <th>ID</th>
                <th>Full Name</th>
                <th>Username</th>
                <!--                <th>Email</th>
            <th>Gender</th>-->
                <th>Division</th>
                <th>Role</th>
                <!--<th>Active</th>-->
                <th>Manager</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="user" items="${users}">
                <tr class="role-${user.role.toLowerCase()}">
                    <td>${user.userId}</td>
                    <td>${user.fullName}</td>
                    <td>${user.username}</td>
                    <!--<td>${user.email}</td>-->
                    <!--<td>${user.gender}</td>-->
                    <td>
                        <c:choose>
                            <c:when test="${not empty user.division}">
                                ${user.division.divisionName}
                            </c:when>
                            <c:otherwise>
                                ${user.divisionId}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${user.role}</td>
                    <!--<td>${user.isActive ? 'Yes' : 'No'}</td>-->
                    <td>
                        <c:choose>
                            <c:when test="${not empty user.manager}">
                                ${user.manager.fullName}
                            </c:when>
                            <c:otherwise>
                                ${user.managerId}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.role == 'Employee'}">
                                <form action="promote" method="post" style="display: inline;">
                                    <input type="hidden" name="id" value="${user.userId}" />
                                    <button type="submit" class="button">Promote</button>
                                </form>
                            </c:when>
                            <c:when test="${user.role == 'Lead'}">
                                <form action="promote" method="post" style="display: inline;">
                                    <input type="hidden" name="id" value="${user.userId}" />
                                    <button type="submit" class="button">Promote</button>
                                </form>
                                <form action="demote" method="post" style="display: inline;">
                                    <input type="hidden" name="id" value="${user.userId}" />
                                    <button type="submit" class="button">Demote</button>
                                </form>
                            </c:when>
                            <c:when test="${user.role == 'Head'}">
                                <form action="demote" method="post" style="display: inline;">
                                    <input type="hidden" name="id" value="${user.userId}" />
                                    <button type="submit" class="button">Demote</button>
                                </form>
                            </c:when>
                        </c:choose>
                        |
                        <form action="changeDivision" method="get" style="display: inline;">
                            <input type="hidden" name="id" value="${user.userId}" />
                            <button type="submit" class="button">Move Division</button>
                        </form>
                        |
                        <a href="edit?id=${user.userId}">Edit</a>
                        |
                        <form action="delete" method="post" style="display: inline;">
                            <input type="hidden" name="id" value="${user.userId}" />
                            <a href="#"
                               onclick="if (confirm('Are you sure you want to delete this user?')) {
                                                this.parentElement.submit();
                                            }
                                            return false;">Delete</a>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>

</html>