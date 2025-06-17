<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit Division</title>
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
            input[type="text"], input[type="number"] {
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
            <h2>Edit Division</h2>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="edit" method="post">
                <input type="hidden" name="divisionId" value="${division.divisionId}"/>
                <div>
                    <label>Division Name:</label>
                    <input type="text" name="divisionName" value="${division.divisionName}" maxlength="50" required/>
                </div>
                <div>
                    <label>Head:</label>
                    <c:choose>
                        <c:when test="${not empty division.divisionHead}">
                            <c:set var="currentHeadUser" value="${null}"/>
                            <c:forEach items="${allLeadsAndHeads}" var="user">
                                <c:if test="${user.userId == division.divisionHead}">
                                    <c:set var="currentHeadUser" value="${user}"/>
                                </c:if>
                            </c:forEach>
                            <c:if test="${not empty currentHeadUser}">
                                <span>${currentHeadUser.fullName} (${currentHeadUser.role})</span>
                            </c:if>
                            <c:if test="${empty currentHeadUser}">
                                <span>Current Head ID: ${division.divisionHead} (User not found or not eligible)</span>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <span>No Head Assigned</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div>
                    <label>Change Head To:</label>
                    <select name="newDivisionHeadId">
                        <option value="">-- Select New Head --</option>
                        <c:forEach items="${allLeadsAndHeads}" var="user">
                            <option value="${user.userId}">${user.fullName} (${user.role} - ${user.division.divisionName})</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="margin-top: 20px;">
                    <input type="submit" value="Save"/>
                     
                    <a href="list">Cancel</a>
                </div>
            </form>
        </div>
    </body>
</html>
