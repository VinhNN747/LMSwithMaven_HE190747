<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <c:choose>
            <c:when test="${empty myRequests}">
                <div class="alert alert-info">You have not submitted any leave requests yet.</div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>Request ID</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Status</th>
                                <th>Reason</th>
                                <th>Approver</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="req" items="${myRequests}">
                                <tr>
                                    <td>${req.leaveRequestId}</td>
                                    <td>
                                        <fmt:formatDate value="${req.startDate}" pattern="yyyy-MM-dd" />
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${req.endDate}" pattern="yyyy-MM-dd" />
                                    </td>
                                    <td>${req.status}</td>
                                    <td>${req.reason}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty req.approver}">
                                                ${req.approver.fullName}
                                            </c:when>
                                            <c:otherwise>
                                                N/A
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>