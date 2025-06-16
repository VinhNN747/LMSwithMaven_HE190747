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
            .hidden {
                display: none;
            }
        </style>
        <script>
            function toggleManagerField() {
                var roleSelect = document.getElementById('role');
                var managerDiv = document.getElementById('managerDiv');
                if (roleSelect.value === 'Employee') {
                    managerDiv.classList.remove('hidden');
                } else {
                    managerDiv.classList.add('hidden');
                }
            }
        </script>
    </head>
    <body>
        <div class="form-container">
            <h2>Edit User</h2>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="edit" method="post">
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
                    <label>Division:</label>
                    <select name="divisionId" required>
                        <option value="">Select Division</option>
                        <c:forEach items="${divisions}" var="division">
                            <option value="${division.divisionId}" 
                                    ${user.divisionId == division.divisionId ? 'selected' : ''}>
                                ${division.divisionName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label>Role:</label>
                    <select name="role" id="role" onchange="toggleManagerField()" required>
                        <option value="">Select Role</option>
                        <option value="Employee" ${user.role == 'Employee' ? 'selected' : ''}>Employee</option>
                        <option value="Director" ${user.role == 'Director' ? 'selected' : ''}>Director</option>
                        <option value="Manager" ${user.role == 'Manager' ? 'selected' : ''}>Manager</option>
                    </select>
                </div>
                <div>
                    <label>Active:</label>
                    <select name="isActive">
                        <option value="true" ${user.isActive ? 'selected' : ''}>Yes</option>
                        <option value="false" ${!user.isActive ? 'selected' : ''}>No</option>
                    </select>
                </div>
                <div id="managerDiv" class="${user.role != 'Employee' ? 'hidden' : ''}">
                    <label>Manager:</label>
                    <select name="managerId">
                        <option value="">Select Manager</option>
                        <c:forEach items="${managers}" var="manager">
                            <option value="${manager.userId}" 
                                    ${user.managerId == manager.userId ? 'selected' : ''}>
                                ${manager.fullName} (${manager.role})
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div style="margin-top: 20px;">
                    <input type="submit" value="Save"/>
                    
                    <a href="list">Cancel</a>
                </div>
            </form>
        </div>
        <script>
            // Initialize manager field visibility on page load
            toggleManagerField();
        </script>
    </body>
</html>
