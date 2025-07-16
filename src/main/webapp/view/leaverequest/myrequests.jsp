<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    request.setAttribute("activeTab", "myrequests");
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
                        <h4>My Leave Requests</h4>
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
                                <select name="reviewerId" class="form-select">
                                    <option value="">All Reviewers</option>
                                    <c:forEach var="user" items="${allUsers}">
                                        <option value="${user.userId}" <c:if test="${selectedReviewerId == user.userId}">selected</c:if>>${user.fullName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col">
                                <button type="submit" class="btn btn-primary">Search</button>
                                <a href="${pageContext.request.contextPath}/leaverequest/myrequests" class="btn btn-secondary ms-2">Reset</a>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${empty myRequests}">
                                <div class="alert alert-info">
                                    You have not submitted any leave requests yet.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="scrollable-list">
                                    <table class="table table-bordered table-hover">
                                        <thead>
                                            <tr>
                                                <th>Title</th>
                                                <th>Start Date</th>
                                                <th>End Date</th>
                                                <th>Status</th>
                                                <th>Reason</th>
                                                <th>Reviewer</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="req" items="${myRequests}">
                                                <tr>
                                                    <td>${req.title}</td>
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
                                                            <c:when test="${not empty req.reviewer}">${req.reviewer.fullName}</c:when>
                                                            <c:otherwise> N/A </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <!-- Pagination Controls: Input for Page Number -->
                                <c:if test="${totalPages > 1}">
                                    <nav aria-label="MyRequests pagination">
                                        <form method="get" action="" class="myrequests-pagination-form d-flex justify-content-center align-items-center mt-3" style="gap: 0.5rem;">
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
                                            <c:if test="${not empty selectedReviewerId}">
                                                <input type="hidden" name="reviewerId" value="${selectedReviewerId}" />
                                            </c:if>
                                        </form>
                                    </nav>
                                    <script>
                                        document.addEventListener('DOMContentLoaded', function () {
                                            const form = document.querySelector('.myrequests-pagination-form');
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
        </div>
    </main>
</div>
<%@ include file="/view/common_jsp_components/footer.jspf" %>
</body>
</html>
