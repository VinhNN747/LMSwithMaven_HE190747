<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit User</title>
        <style>
            .form-container {
                width: 50%;
                margin: 20px auto;
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 5px;
            }
            label {
                display: inline-block;
                width: 120px;
                margin-bottom: 10px;
            }
            input[type="text"], input[type="email"], select {
                width: 200px;
                padding: 5px;
                border: 1px solid #ddd;
                border-radius: 3px;
            }
            input[type="submit"] {
                padding: 8px 20px;
                background-color: #0066cc;
                color: white;
                border: none;
                border-radius: 3px;
                cursor: pointer;
            }
            input[type="submit"]:hover {
                background-color: #0052a3;
            }
            .error {
                color: red;
                margin-bottom: 10px;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2>Edit User</h2>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="user" method="post">
                <input type="hidden" name="userId" value="${user.userId}"/>
                <div>
                    <label>Full Name:</label>
                    <input type="text" name="fullName" value="${user.fullName}" maxlength="100" required/>
                </div>
                <div>
                    <label>Username:</label>
                    <input type="text" name="username" value="${user.username}" maxlength="50" required/>
                </div>
                <div>
                    <label>Email:</label>
                    <input type="email" name="email" value="${user.email}" maxlength="100" required/>
                </div>
                <div>
                    <label>Gender:</label>
                    <select name="gender">
                        <option value="">Select Gender</option>
                        <option value="M" ${user.gender == 'M' ? 'selected' : ''}>Male</option>
                        <option value="F" ${user.gender == 'F' ? 'selected' : ''}>Female</option>
                    </select>
                </div>
                <div>
                    <label>Division ID:</label>
                    <input type="text" name="divisionId" value="${user.divisionId}"/>
                </div>
                <div>
                    <label>Role:</label>
                    <input type="text" name="role" value="${user.role}" maxlength="100" required/>
                </div>
                <div>
                    <label>Active:</label>
                    <select name="isActive">
                        <option value="true" ${user.isActive ? 'selected' : ''}>Yes</option>
                        <option value="false" ${!user.isActive ? 'selected' : ''}>No</option>
                    </select>
                </div>
                <div>
                    <label>Manager ID:</label>
                    <input type="text" name="managerId" value="${user.managerId}" maxlength="10"/>
                </div>
                <div style="margin-top: 20px;">
                    <input type="submit" value="Save"/>
                     
                    <a href="user?action=list">Cancel</a>
                </div>
                <input type="hidden" name="action" value="update"/>
            </form>
        </div>
    </body>
</html>
