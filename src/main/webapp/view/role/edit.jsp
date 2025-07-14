<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <div class="d-flex">
            <%@ include file="/view/common_jsp_components/sidebar.jspf" %>
            <main class="main-content flex-grow-1">
                <div class="container">
                    <div class="row justify-content-center">
                        <div class="col-md-8">
                            <div class="card">
                                <div class="card-header">
                                    <h4>Edit Role: ${role.roleName}</h4>
                                </div>
                                <div class="card-body">
                                    <c:if test="${not empty error}">
                                        <p class="error">${error}</p>
                                    </c:if>
                                    <form action="edit" method="post">
                                        <input type="hidden" name="roleId" value="${role.roleId}"/>
                                        
                                        <div class="mb-3">
                                            <input type="text" id="roleName" name="roleName" class="form-control" placeholder="Role Name" value="${role.roleName}" maxlength="50" required/>
                                        </div>
                                        <div class="mb-3">
                                            <textarea id="roleDescription" name="roleDescription" class="form-control" placeholder="Description" rows="3" maxlength="50">${role.roleDescription}</textarea>
                                        </div>
                                        <div class="mb-3">
                                            <select id="roleLevel" name="roleLevel" class="form-select">
                                                <option value="">Select Level</option>
                                                <option value="1" ${role.roleLevel == 1 ? 'selected' : ''}>Level 1</option>
                                                <option value="2" ${role.roleLevel == 2 ? 'selected' : ''}>Level 2</option>
                                                <option value="3" ${role.roleLevel == 3 ? 'selected' : ''}>Level 3</option>
                                                <option value="4" ${role.roleLevel == 4 ? 'selected' : ''}>Level 4</option>
                                                <option value="5" ${role.roleLevel == 5 ? 'selected' : ''}>Level 5</option>
                                                <option value="6" ${role.roleLevel == 6 ? 'selected' : ''}>Level 6</option>
                                                <option value="7" ${role.roleLevel == 7 ? 'selected' : ''}>Level 7</option>
                                                <option value="8" ${role.roleLevel == 8 ? 'selected' : ''}>Level 8</option>
                                                <option value="9" ${role.roleLevel == 9 ? 'selected' : ''}>Level 9</option>
                                                <option value="10" ${role.roleLevel == 10 ? 'selected' : ''}>Level 10</option>
                                            </select>
                                        </div>

                                        <button type="submit" class="btn btn-primary">Save Changes</button>
                                        <a href="${pageContext.request.contextPath}/role/list" class="btn btn-secondary">Cancel</a>
                                        <a href="${pageContext.request.contextPath}/role/assignFeatures?roleId=${role.roleId}" class="btn btn-warning ms-2">Assign Features</a>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html> 