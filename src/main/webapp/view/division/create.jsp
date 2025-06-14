<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Add New Division</title>
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
            input[type="text"] {
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
            <h2>Add New Division</h2>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="division" method="post">
                <div>
                    <label>Division Name:</label>
                    <input type="text" name="divisionName" maxlength="50" required/>
                </div>
                <div>
                    <label>Director ID:</label>
                    <input type="text" name="divisionDirector" maxlength="10"/>
                </div>
                <div style="margin-top: 20px;">
                    <input type="submit" value="Save"/>
                     
                    <a href="division?action=list">Cancel</a>
                </div>
                <input type="hidden" name="action" value="create"/>
            </form>
        </div>
    </body>
</html>
