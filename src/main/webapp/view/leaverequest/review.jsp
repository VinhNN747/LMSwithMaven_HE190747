<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <%@ include file="/view/common_jsp_components/navbar.jspf" %>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col">
                    <div class="card mt-5">
                        <div class="card-header">
                            <h4>Review Direct Subordinates' Leave Requests</h4>
                            <small class="text-muted">Only direct subordinates' requests that need approval/denial</small>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty directSubRequests}">
                                    <div class="alert alert-info">
                                        You have no direct subordinates' leave requests to review.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-bordered">
                                            <thead>
                                                <tr>
                                                    <th>Title</th>
                                                    <th>Employee</th>
                                                    <th>Start Date</th>
                                                    <th>End Date</th>
                                                    <th>Status</th>
                                                    <th>Reason</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="req" items="${directSubRequests}">
                                                    <tr>
                                                        <td>${req.title}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${not empty req.sender}">${req.sender.fullName}</c:when>
                                                                <c:otherwise>N/A</c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td><fmt:formatDate value="${req.startDate}" pattern="yyyy-MM-dd" /></td>
                                                        <td><fmt:formatDate value="${req.endDate}" pattern="yyyy-MM-dd" /></td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${req.status eq 'In Progress'}">
                                                                    <span class="badge bg-warning">Pending</span>
                                                                </c:when>
                                                                <c:when test="${req.status eq 'Approved'}">
                                                                    <span class="badge bg-success">Approved</span>
                                                                </c:when>
                                                                <c:when test="${req.status eq 'Rejected'}">
                                                                    <span class="badge bg-danger">Rejected</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge bg-secondary">${req.status}</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>${req.reason}</td>
                                                        <td>
                                                            <c:if test="${req.status eq 'In Progress'}">
                                                                <form method="post" action="${pageContext.request.contextPath}/leaverequest/review" style="display: inline">
                                                                    <input type="hidden" name="requestId" value="${req.leaveRequestId}" />
                                                                    <input type="hidden" name="action" value="approve" />
                                                                    <button type="submit" class="btn btn-success btn-sm">Approve</button>
                                                                </form>
                                                                <form method="post" action="${pageContext.request.contextPath}/leaverequest/review" style="display: inline">
                                                                    <input type="hidden" name="requestId" value="${req.leaveRequestId}" />
                                                                    <input type="hidden" name="action" value="reject" />
                                                                    <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                                                </form>
                                                            </c:if>
                                                            <c:if test="${req.status ne 'In Progress'}">
                                                                <span class="text-muted">Already ${req.status}</span>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                        <c:if test="${reviewTotalPages > 1}">
                                            <nav>
                                                <ul class="pagination">
                                                    <c:forEach begin="1" end="${reviewTotalPages}" var="i">
                                                        <li class="page-item ${i == reviewCurrentPage ? 'active' : ''}">
                                                            <a class="page-link" href="?reviewPage=${i}">${i}</a>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </nav>
                                        </c:if>
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
