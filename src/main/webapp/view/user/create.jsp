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
                        <h4>Add New User</h4>
                    </div>
                    <div class="card-body">
                        <form action="create" method="post">
                            <div class="mb-3">
                                <input type="text" id="fullName" name="fullName" class="form-control" placeholder="Full Name" maxlength="100" required/>
                            </div>
                            <div class="mb-3">
                                <input type="text" id="username" name="username" class="form-control" placeholder="Username" maxlength="50" required/>
                            </div>
                            <div class="mb-3">
                                <input type="text" id="password" name="password" class="form-control" placeholder="Password" maxlength="50" required/>
                            </div>
                            <div class="mb-3">
                                <input type="email" id="email" name="email" class="form-control" placeholder="Email" maxlength="100" required/>
                            </div>
                            <div class="mb-3">
                                <label for="gender" class="form-label">Gender:</label>
                                <select id="gender" name="gender" class="form-select">
                                    <option value="">Select Gender</option>
                                    <option value="true">Male</option>
                                    <option value="false">Female</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="divisionId" class="form-label">Division:</label>
                                <select id="divisionId" name="divisionId" class="form-select">
                                    <option value="">Select a division</option>
                                    <c:forEach items="${requestScope.divisions}" var="d">
                                        <option value="${d.divisionId}">${d.divisionName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary">Save User</button>
                            <a href="list" class="btn btn-secondary">Cancel</a>
                        </form>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
