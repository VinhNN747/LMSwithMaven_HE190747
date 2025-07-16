<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("activeTab", "role");
%>
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
                                    <h4>Add New Role</h4>
                                </div>
                                <div class="card-body">
                                    <c:if test="${not empty error}">
                                        <p class="error">${error}</p>
                                    </c:if>
                                    <form action="create" method="post">
                                        <div class="mb-3">
                                            <input type="text" id="roleName" name="roleName" class="form-control" placeholder="Role Name" maxlength="50" required/>
                                        </div>
                                        <div class="mb-3">
                                            <textarea id="roleDescription" name="roleDescription" class="form-control" placeholder="Description" rows="3" maxlength="50"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <select id="roleLevel" name="roleLevel" class="form-select">
                                                <option value="">Select Level</option>
                                                <option value="1">Level 1</option>
                                                <option value="2">Level 2</option>
                                                <option value="3">Level 3</option>
                                                <option value="4">Level 4</option>
                                                <option value="5">Level 5</option>
                                                <option value="6">Level 6</option>
                                                <option value="7">Level 7</option>
                                                <option value="8">Level 8</option>
                                                <option value="9">Level 9</option>
                                                <option value="10">Level 10</option>
                                            </select>
                                        </div>
                                        <button type="submit" class="btn btn-primary">Save Role</button>
                                        <a href="${pageContext.request.contextPath}/role/list" class="btn btn-secondary">Cancel</a>
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