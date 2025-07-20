<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    request.setAttribute("activeTab", "agenda");
%>
<!DOCTYPE html>
<html>
    <%@ include file="/view/common_jsp_components/head.jspf" %>
    <body>
        <div class="d-flex">
            <%@ include file="/view/common_jsp_components/sidebar.jspf" %>
            <main class="main-content flex-grow-1 p-4">
                <div class="card">
                    <div class="card-header">
                        <h4>Division Agenda</h4>
                    </div>
                    <div class="card-body">
                        <form method="post" action="agenda" class="row g-2 align-items-end mb-4" style="max-width: 500px;">
                            <div class="col-auto">
                                <label for="from" class="form-label mb-0">From</label>
                                <input type="date" id="from" name="from" class="form-control" value="<fmt:formatDate value='${fromDate}' pattern='yyyy-MM-dd'/>" required>
                            </div>
                            <div class="col-auto">
                                <label for="to" class="form-label mb-0">To</label>
                                <input type="date" id="to" name="to" class="form-control" value="<fmt:formatDate value='${toDate}' pattern='yyyy-MM-dd'/>" required>
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-primary">View</button>
                            </div>
                            <div class="col-12">
                                <div id="date-range-error" style="color:red;"></div>
                            </div>
                        </form>
                        <script>
                            const fromInput = document.getElementById('from');
                            const toInput = document.getElementById('to');
                            const form = fromInput && toInput && fromInput.form;
                            const errorDiv = document.getElementById('date-range-error');
                            if (form) {
                                form.addEventListener('submit', function (e) {
                                    const from = new Date(fromInput.value);
                                    const to = new Date(toInput.value);
                                    const diff = (to - from) / (1000 * 60 * 60 * 24) + 1;
                                    if (from > to) {
                                        errorDiv.textContent = 'End date must be after or equal to start date!';
                                        e.preventDefault();
                                    } else if (diff < 1) {
                                        errorDiv.textContent = 'Date range must be at least 1 day!';
                                        e.preventDefault();
                                    } else if (diff > 10) {
                                        errorDiv.textContent = 'Date range cannot exceed 10 days!';
                                        e.preventDefault();
                                    } else {
                                        errorDiv.textContent = '';
                                    }
                                });
                            }
                        </script>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover agenda-table">
                                <thead>
                                    <tr>
                                        <th>Staff</th>
                                            <c:forEach var="date" items="${dateList}">
                                            <th><fmt:formatDate value="${date}" pattern="d/M"/></th>
                                            </c:forEach>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="user" items="${userList}">
                                        <tr>
                                            <td class="user-col">${user.fullName}</td>
                                            <c:forEach var="date" items="${dateList}">
                                                <c:set var="dateKey"><fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/></c:set>
                                                <c:choose>
                                                    <c:when test="${statusMap[user.userId][dateKey] == 'leave'}">
                                                        <td class="leave-cell" ></td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="work-cell"></td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
