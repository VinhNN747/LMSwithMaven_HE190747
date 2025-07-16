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
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${empty myRequests}">
                                <div class="alert alert-info">
                                    You have not submitted any leave requests yet.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <%@ include file="/view/leaverequest/myrequests-table.jspf" %>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
