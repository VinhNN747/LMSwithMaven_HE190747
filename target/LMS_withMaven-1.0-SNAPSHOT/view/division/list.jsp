<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("activeTab", "division");
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
                        <h4>Division Management</h4>
                    </div>
                    <div class="card-body">

                        <div class="mb-3">
                            <form method="get" action="" class="row g-2 align-items-end mb-2">
                                <div class="col-md-4">
                                    <label for="divisionName" class="form-label">Division Name</label>
                                    <input type="text" id="divisionName" name="name" class="form-control" value="${name}" placeholder="Search by name..." />
                                </div>
                                <div class="col-md-4">
                                    <button type="submit" class="btn btn-primary">Search</button>
                                    <a href="list" class="btn btn-secondary ms-2">Reset</a>
                                </div>
                            </form>
                            <a href="create" class="btn btn-primary">Add New Division</a>
                        </div>
                        <div class="scrollable-list">
                            <table class="table table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Division Head</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="division" items="${divisions}">
                                        <tr>
                                            <td>${division.divisionId}</td>
                                            <td>${division.divisionName}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty division.head}">
                                                        ${division.head.fullName}
                                                    </c:when>
                                                    <c:otherwise> N/A </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="edit?id=${division.divisionId}" class="btn btn-sm btn-secondary">Edit</a>
                                                <form action="delete" method="post" style="display: inline" onsubmit="return confirm('Are you sure you want to delete this division?');">
                                                    <input type="hidden" name="id" value="${division.divisionId}" />
                                                    <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <!-- Pagination Controls: Input for Page Number -->
                        <c:if test="${2 > 1}">
                            <nav aria-label="Division pagination">
                                <form method="get" action="list" class="division-pagination-form d-flex justify-content-center align-items-center mt-3" style="gap: 0.5rem;">
                                    <button class="btn btn-outline-secondary" type="button" data-page="1" ${pageNumber == 1 ? 'disabled' : ''}>&lt;&lt;</button>
                                    <button class="btn btn-outline-secondary" type="button" data-page="${pageNumber - 1}" ${pageNumber == 1 ? 'disabled' : ''}>&lt;</button>
                                    <span>Page</span>
                                    <input type="number" min="1" max="${totalPages}" name="pageNumber" value="${pageNumber}" style="width: 60px; text-align: center;" required />
                                    <span>/ ${totalPages}</span>
                                    <button class="btn btn-outline-primary" type="submit">Go</button>
                                    <button class="btn btn-outline-secondary" type="button" data-page="${pageNumber + 1}" ${pageNumber == totalPages ? 'disabled' : ''}>&gt;</button>
                                    <button class="btn btn-outline-secondary" type="button" data-page="${totalPages}" ${pageNumber == totalPages ? 'disabled' : ''}>&gt;&gt;</button>
                                    <input type="hidden" name="pageSize" value="${pageSize}" />
                                    <c:if test="${not empty name}">
                                        <input type="hidden" name="name" value="${name}" />
                                    </c:if>
                                </form>
                            </nav>
                            <script>
                                document.addEventListener('DOMContentLoaded', function () {
                                    const form = document.querySelector('.division-pagination-form');
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
                </div>
            </main>
        </div>
        <%@ include file="/view/common_jsp_components/footer.jspf" %>
    </body>
</html>
