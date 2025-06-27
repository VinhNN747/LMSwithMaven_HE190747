<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <%@ include file="/view/common_jsp_components/navbar.jspf" %>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-10">
                    <div class="card mt-5">
                        <div class="card-header">
                            <h4>All Leave Requests</h4>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty allRequests}">
                                    <div class="alert alert-info">
                                        No leave requests found.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <div class="table-responsive">
                                            <table class="table table-striped table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>Request ID</th>
                                                        <th>Sender</th>
                                                        <th>Start Date</th>
                                                        <th>End Date</th>
                                                        <th>Status</th>
                                                        <th>Reason</th>
                                                        <th>Approver</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="req" items="${allRequests}">
                                                        <tr>
                                                            <td>${req.leaveRequestId}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty req.sender}">${req.sender.fullName}</c:when>
                                                                    <c:otherwise>N/A</c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <fmt:formatDate
                                                                    value="${req.startDate}"
                                                                    pattern="yyyy-MM-dd"
                                                                    />
                                                            </td>
                                                            <td>
                                                                <fmt:formatDate
                                                                    value="${req.endDate}"
                                                                    pattern="yyyy-MM-dd"
                                                                    />
                                                            </td>
                                                            <td>${req.status}</td>
                                                            <td>${req.reason}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty req.approver}">
                                                                        ${req.approver.fullName}
                                                                    </c:when>
                                                                    <c:otherwise> N/A </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                            <c:if test="${allTotalPages > 1}">
                                                <nav>
                                                    <ul class="pagination">
                                                        <c:forEach begin="1" end="${allTotalPages}" var="i">
                                                            <li class="page-item ${i == allCurrentPage ? 'active' : ''}">
                                                                <a class="page-link" href="?allPage=${i}">${i}</a>
                                                            </li>
                                                        </c:forEach>
                                                    </ul>
                                                </nav>
                                            </c:if>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>