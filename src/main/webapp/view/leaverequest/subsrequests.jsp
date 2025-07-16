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
                        <small class="text-muted">View all subordinates' requests (including subordinates of subordinates) for monitoring</small>
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
                            <c:set var="requests" value="${subRequests}" scope="request" />
                            <div class="card-body">
                                <%@ include file="/view/leaverequest/subsrequests-table.jspf" %>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
