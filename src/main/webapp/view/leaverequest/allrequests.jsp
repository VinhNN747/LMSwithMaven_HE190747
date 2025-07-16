<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("activeTab", "allrequests");
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
                        <h4>All Leave Requests</h4>
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
                                <select name="divisionId" class="form-select">
                                    <option value="">All Divisions</option>
                                    <c:forEach var="division" items="${allDivisions}">
                                        <option value="${division.divisionId}" ${selectedDivisionId != null && selectedDivisionId == division.divisionId ? 'selected' : ''}>
                                            ${division.divisionName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col">
                                <button type="submit" class="btn btn-primary">Search</button>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${empty allRequests}">
                                <div class="alert alert-info">
                                    No leave requests found.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="scrollable-list">
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
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="req" items="${allRequests}">
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

                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div> 
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