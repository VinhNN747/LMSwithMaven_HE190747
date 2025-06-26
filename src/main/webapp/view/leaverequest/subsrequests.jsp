<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
                            <h4>Leave Requests to Approve</h4>
                        </div>
                        <c:choose>
                            <c:when test="${empty subRequests}">
                                <div class="alert alert-info">
                                    You have no leave requests to approve yet.
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
                </div>
            </div>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
