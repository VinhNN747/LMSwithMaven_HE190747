<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<h2>User List</h2>
<a href="UserCreate.jsp">Create New User</a>
<table border="1">
    <tr><th>ID</th><th>Name</th><th>Actions</th></tr>
            <c:forEach var="user" items="${requestScope.users}">
        <tr>
            <td>${user.userId}</td>
            <td>${user.fullName}</td>
            <td>
                <a href="UserEdit?id=${user.userId}">Edit</a>
                <a href="UserDelete?id=${user.userId}" onclick="return confirm('Delete?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
