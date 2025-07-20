<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("activeTab", "create");
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
                        <h4>Submit Leave Request</h4>
                    </div>
                    <div class="card-body">
                        <form action="create" method="post">

                            <div class="mb-3">
                                <textarea id="title" name="title" class="form-control" placeholder="Title" rows="2" maxlength="255" required></textarea>
                            </div>
                            <div class="mb-3">
                                <input type="date" id="startDate" name="startDate" class="form-control" placeholder="Start Date" required />
                            </div>
                            <div class="mb-3">
                                <input type="date" id="endDate" name="endDate" class="form-control" placeholder="End Date" required />
                            </div>
                            <div class="mb-3">
                                <textarea id="reason" name="reason" class="form-control" placeholder="Reason" rows="2" maxlength="255" required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Submit Request</button>
                            <a href="myrequests" class="btn btn-secondary">Cancel</a>
                        </form>                     
                    </div>
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
