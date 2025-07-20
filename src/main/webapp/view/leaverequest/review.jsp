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
            <%@ include file="/view/common_jsp_components/sidebar.jspf" %>
            <main class="main-content flex-grow-1 p-4">
                <div style="display: flex; justify-content: center; align-items: flex-start; min-height: 60vh;">
                    <div style="background: #e3f0fc; border: 2px solid #3a7bd5; border-radius: 8px; padding: 24px 28px; min-width: 350px; max-width: 400px; box-shadow: 0 2px 8px rgba(58,123,213,0.08);">
                        <h5 style="margin-bottom: 18px; font-weight: bold;">Leave Request Review</h5>
                        <div style="margin-bottom: 8px;">
                            Reviewed by User: <b>${leaveRequest.reviewer.fullName}</b>
                        </div>
                        <div style="margin-bottom: 8px;">
                            Role: <b>${leaveRequest.reviewer.role.roleName}</b>           
                        </div>
                        <div style="margin-bottom: 8px;">
                            Created by: <b>${leaveRequest.sender.fullName}</b>
                        </div>
                        <div style="margin-bottom: 8px;">
                            From date: <b><fmt:formatDate value="${leaveRequest.startDate}" pattern="yyyy-MM-dd" /></b>
                        </div>
                        <div style="margin-bottom: 8px;">
                            To date: <b><fmt:formatDate value="${leaveRequest.endDate}" pattern="yyyy-MM-dd" /></b>
                        </div>
                        <div style="margin-bottom: 8px;">Reason:</div>
                        <div style="background: #f4f4f4; border: 2px solid #bfc9d1; border-radius: 6px; padding: 18px 12px; font-size: 1.25em; margin-bottom: 18px; min-height: 48px;">${leaveRequest.reason}</div>
                        <c:if test="${leaveRequest.status eq 'In Progress'}">
                            <form method="post" action="review" style="display: flex; gap: 16px; justify-content: center;">
                                <input type="hidden" name="requestId" value="${leaveRequest.leaveRequestId}" />
                                <button type="submit" name="action" value="reject" style="background: #3a7bd5; color: white; border: none; border-radius: 8px; padding: 10px 28px; font-size: 1.1em; font-weight: 500; cursor: pointer;">Reject</button>
                                <button type="submit" name="action" value="approve" style="background: #3a7bd5; color: white; border: none; border-radius: 8px; padding: 10px 28px; font-size: 1.1em; font-weight: 500; cursor: pointer;">Approve</button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html> 