<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Add New User</title>
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
            <h2>Add New User</h2>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="user" method="post">
                <div>
                    <label>Full Name:</label>
                    <input type="text" name="fullName" maxlength="100" required/>
                </div>
                <div>
                    <label>Username:</label>
                    <input type="text" name="username" maxlength="50" required/>
                </div>
                <div>
                    <label>Email:</label>
                    <input type="email" name="email" maxlength="100" required/>
                </div>
                <div>
                    <label>Gender:</label>
                    <select name="gender">
                        <option value="">Select Gender</option>
                        <option value="M">Male</option>
                        <option value="F">Female</option>
                    </select>
                </div>
                <div>                    
                    <label>Division:</label>
                    <select name="divisionId">
                        <option value="">Select a division</option>
                        <c:forEach items="${requestScope.divisions}" var="d">
                            <option value="${d.divisionId}">${d.divisionName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label>Role:</label>
                    <select name="role">
                        <option value="Employee">Employee</option>
                        <option value="Manager">Manager</option>
                        <option value="Director">Director</option>
                    </select>
                </div>
                <div>
                    <label>Active:</label>
                    <select name="isActive">
                        <option value="true">Yes</option>
                        <option value="false">No</option>
                    </select>
                </div>
<!--                <div>
                    <label>Manager ID:</label>
                    <input type="text" name="managerId" maxlength="10"/>
                </div>-->
                <div style="margin-top: 20px;">
                    <input type="submit" value="Save"/>
                     
                    <a href="user?action=list">Cancel</a>
                </div>
                <input type="hidden" name="action" value="create"/>
            </form>
        </div>
    </body>
</html>
