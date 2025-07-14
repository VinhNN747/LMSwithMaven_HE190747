<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("activeTab", "user");
%>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <div class="d-flex">
            <!-- Sidebar -->
            <%@ include file="/view/common_jsp_components/sidebar.jspf" %>

            <!-- Main Content -->
            <main class="main-content flex-grow-1 p-4">
                <div class="card">
                    <div class="card-header">
                        <h4>Edit User: ${user.fullName}</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <p class="error">${error}</p>
                        </c:if>
                        <form action="edit" method="post">
                            <input type="hidden" name="userId" value="${user.userId}"/>
                            
                            <div class="mb-3">
                                <input type="text" id="fullName" name="fullName" class="form-control" placeholder="Full Name" value="${user.fullName}" maxlength="100" required/>
                            </div>
                            <div class="mb-3">
                                <input type="text" id="username" name="username" class="form-control" placeholder="Username" value="${user.username}" maxlength="50" required/>
                            </div>
                            <div class="mb-3">
                                <input type="email" id="email" name="email" class="form-control" placeholder="Email" value="${user.email}" maxlength="100" required/>
                            </div>
                            <div class="mb-3">
                                <select id="gender" name="gender" class="form-select">
                                    <option value="">Select Gender</option>
                                    <option value="true" ${user.gender != null && user.gender ? 'selected' : ''}>Male</option>
                                    <option value="false" ${user.gender != null && !user.gender ? 'selected' : ''}>Female</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="managerId" class="form-label">Manager:</label>
                                <select id="managerId" name="managerId" class="form-select">
                                    <option value="">No Manager</option>
                                    <c:forEach items="${potentialManagers}" var="manager">
                                        <option value="${manager.userId}" ${user.managerId != null && user.managerId.equals(manager.userId) ? 'selected' : ''}>
                                            ${manager.fullName} (${manager.username})
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="form-text text-muted">
                                    Only users with higher role levels in the same division can be selected as managers.
                                </small>
                            </div>

                            <button type="submit" class="btn btn-primary">Save Changes</button>
                            <a href="${pageContext.request.contextPath}/user/list" class="btn btn-secondary">Cancel</a>
                            <a href="${pageContext.request.contextPath}/user/changeDivision?userId=${user.userId}" class="btn btn-warning ms-2">Change Division</a>
                        </form>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
