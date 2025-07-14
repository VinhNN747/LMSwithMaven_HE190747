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
                                    <h4>Add New Division</h4>
                                </div>
                                <div class="card-body">
                                    <c:if test="${not empty error}">
                                        <p class="error">${error}</p>
                                    </c:if>
                                    <form action="create" method="post">
                                        <div class="mb-3">
                                            <input type="text" id="divisionName" name="divisionName" class="form-control" placeholder="Division Name" maxlength="50" required />
                                        </div>
                                        <button type="submit" class="btn btn-primary">Save Division</button>
                                        <a href="${pageContext.request.contextPath}/division/list" class="btn btn-secondary">Cancel</a>
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
