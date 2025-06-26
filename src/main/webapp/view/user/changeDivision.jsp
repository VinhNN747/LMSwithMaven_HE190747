<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <%@ include file="/view/common_jsp_components/navbar.jspf" %>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">
                            <h4>Change Division for ${user.fullName}</h4>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty error}">
                                <p class="error">${error}</p>
                            </c:if>
                            <form action="changeDivision" method="post">
                                <input type="hidden" name="userId" value="${user.userId}"/>
                                <div class="mb-3">
                                    <label for="divisionId" class="form-label">New Division:</label>
                                    <select id="divisionId" name="divisionId" class="form-select" required>
                                        <option value="">Select Division</option>
                                        <c:forEach items="${divisions}" var="division">
                                            <option value="${division.divisionId}" ${user.divisionId == division.divisionId ? 'selected' : ''}>
                                                ${division.divisionName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-primary">Change Division</button>
                                <a href="${pageContext.request.contextPath}/user/list" class="btn btn-secondary">Cancel</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html> 