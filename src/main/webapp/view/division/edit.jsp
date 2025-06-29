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
                            <h4>Edit Division: ${division.divisionName}</h4>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty error}">
                                <p class="error">${error}</p>
                            </c:if>
                            <form action="edit" method="post">
                                <input type="hidden" name="divisionId" value="${division.divisionId}" />
                                <div class="mb-3">
                                    <input type="text" id="divisionName" name="divisionName" class="form-control" placeholder="Division Name" value="${division.divisionName}" maxlength="50" required />
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Current Head:</label>
                                    <div>
                                        <c:choose>
                                            <c:when test="${not empty division.head}">
                                                <span class="form-control-plaintext">${division.head.fullName}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="form-control-plaintext">No Head Assigned</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <small class="form-text text-muted">
                                        Division heads are managed through role assignments. To change the head, 
                                        assign the "Division Head" role to a user in this division.
                                    </small>
                                </div>
                                <button type="submit" class="btn btn-primary">Save Changes</button>
                                <a href="${pageContext.request.contextPath}/division/list" class="btn btn-secondary">Cancel</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
