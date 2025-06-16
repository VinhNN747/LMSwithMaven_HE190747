<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Change Division</title>
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
            select {
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
            <h2>Change Division for ${user.fullName}</h2>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="changeDivision" method="post">
                <input type="hidden" name="id" value="${user.userId}"/>
                <div>
                    <label>New Division:</label>
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
                <div style="margin-top: 20px;">
                    <input type="submit" value="Change Division"/>
                     | 
                    <a href="list">Cancel</a>
                </div>
            </form>
        </div>
    </body>
</html> 