<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                            <h4>My Leave Requests</h4>
                        </div>
                        <div class="card-body">
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
                </div>
            </div>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
