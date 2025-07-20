<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    request.setAttribute("activeTab", "subs");
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
                        <h4>All Subordinates' Leave Requests</h4>
                    </div>
                    <div class="card-body">
                        <form method="get" class="row g-2 mb-3">
                            <div class="col">
                                <select name="status" class="form-select">
                                    <option value="">All Statuses</option>
                                    <option value="In Progress" ${selectedStatus == 'In Progress' ? 'selected' : ''}>In Progress</option>
                                    <option value="Approved" ${selectedStatus == 'Approved' ? 'selected' : ''}>Approved</option>
                                    <option value="Rejected" ${selectedStatus == 'Rejected' ? 'selected' : ''}>Rejected</option>
                                </select>
                            </div>
                            <div class="col">
                                <select name="senderId" class="form-select">
                                    <option value="">All Senders</option>
                                    <c:forEach var="user" items="${allUsers}">
                                        <option value="${user.userId}" <c:if test="${selectedSenderId == user.userId}">selected</c:if>>${user.fullName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col">
                                <select name="reviewerId" class="form-select">
                                    <option value="">All Reviewers</option>
                                    <c:forEach var="user" items="${allUsers}">
                                        <option value="${user.userId}" <c:if test="${selectedReviewerId == user.userId}">selected</c:if>>${user.fullName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col">
                                <button type="submit" class="btn btn-primary">Search</button>
                                <a href="subs" class="btn btn-secondary ms-2">Reset</a>
                            </div>
                        </form>
                    </div>
                    <c:choose>
                        <c:when test="${empty subRequests}">
                            <div class="alert alert-info">
                                You have no subordinates' leave requests to view.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="card-body">
                                <div class="scrollable-list">
                                    <table class="table table-bordered table-hover">
                                        <thead>
                                            <tr>
                                                <th>Title</th>
                                                <th>Sender</th>
                                                <th>Start Date</th>
                                                <th>End Date</th>
                                                <th>Status</th>
                                                <th>Reason</th>
                                                <th>Reviewer</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="req" items="${subRequests}">
                                                <tr>
                                                    <td>${req.title}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty req.sender}">
                                                                ${req.sender.fullName}
                                                            </c:when>
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
                                                        <c:choose>
                                                            <c:when test="${not empty req.reviewer}">
                                                                ${req.reviewer.fullName}
                                                            </c:when>
                                                            <c:otherwise>N/A</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:if test="${req.status eq 'In Progress'}">
                                                            <form method="post" action="${pageContext.request.contextPath}/leaverequest/review" style="display:inline;">
                                                                <input type="hidden" name="requestId" value="${req.leaveRequestId}" />
                                                                <input type="hidden" name="action" value="approve" />
                                                                <button type="submit" class="btn btn-success btn-sm">Approve</button>
                                                            </form>
                                                            <form method="post" action="${pageContext.request.contextPath}/leaverequest/review" style="display:inline; margin-left: 4px;">
                                                                <input type="hidden" name="requestId" value="${req.leaveRequestId}" />
                                                                <input type="hidden" name="action" value="reject" />
                                                                <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                                            </form>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <!-- Pagination Controls: Input for Page Number -->
                                <c:if test="${totalPages > 1}">
                                    <nav aria-label="SubRequests pagination">
                                        <form method="get" action="subs" class="subsrequests-pagination-form d-flex justify-content-center align-items-center mt-3" style="gap: 0.5rem;">
                                            <button class="btn btn-outline-secondary" type="button" data-page="1" ${pageNumber == 1 ? 'disabled' : ''}>&lt;&lt;</button>
                                            <button class="btn btn-outline-secondary" type="button" data-page="${pageNumber - 1}" ${pageNumber == 1 ? 'disabled' : ''}>&lt;</button>
                                            <span>Page</span>
                                            <input type="number" min="1" max="${totalPages}" name="pageNumber" value="${pageNumber}" style="width: 60px; text-align: center;" required />
                                            <span>/ ${totalPages}</span>
                                            <button class="btn btn-outline-primary" type="submit">Go</button>
                                            <button class="btn btn-outline-secondary" type="button" data-page="${pageNumber + 1}" ${pageNumber == totalPages ? 'disabled' : ''}>&gt;</button>
                                            <button class="btn btn-outline-secondary" type="button" data-page="${totalPages}" ${pageNumber == totalPages ? 'disabled' : ''}>&gt;&gt;</button>
                                            <input type="hidden" name="pageSize" value="${pageSize}" />
                                            <c:if test="${not empty selectedStatus}">
                                                <input type="hidden" name="status" value="${selectedStatus}" />
                                            </c:if>
                                            <c:if test="${not empty selectedSenderId}">
                                                <input type="hidden" name="senderId" value="${selectedSenderId}" />
                                            </c:if>
                                            <c:if test="${not empty selectedReviewerId}">
                                                <input type="hidden" name="reviewerId" value="${selectedReviewerId}" />
                                            </c:if>
                                        </form>
                                    </nav>
                                    <script>
                                        document.addEventListener('DOMContentLoaded', function () {
                                            const form = document.querySelector('.subsrequests-pagination-form');
                                            if (!form)
                                                return;
                                            const pageInput = form.querySelector('input[name="pageNumber"]');
                                            var totalPages = parseInt('${totalPages}');
                                            form.querySelectorAll('button[data-page]').forEach(btn => {
                                                btn.addEventListener('click', function (e) {
                                                    e.preventDefault();
                                                    const page = parseInt(this.getAttribute('data-page'));
                                                    if (!isNaN(page) && page >= 1 && page <= totalPages && page !== parseInt(pageInput.value)) {
                                                        pageInput.value = page;
                                                        form.submit();
                                                    }
                                                });
                                            });
                                        });
                                    </script>
                                </c:if>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
