<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
    prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <c:set var="isAdmin" value="false" />
    <c:set var="isEmployee" value="false" />
    <c:set var="isLead" value="false" />
    <c:set var="isHead" value="false" />
    <c:set var="hasOtherRole" value="false" />
    <c:forEach items="${sessionScope.roles}" var="role">
        <c:if test="${role.roleName == 'Admin'}"
              ><c:set var="isAdmin" value="true"
                /></c:if>
    </c:forEach>

    <c:set var="activeTab" value="${param.tab != null ? param.tab : 'my'}" />

    <!DOCTYPE html>
    <html>
        <%@ include file="/view/common_jsp_components/head.jspf" %>
        <body>
            <%@ include file="/view/common_jsp_components/navbar.jspf" %>
            <div class="container-fluid main-container">
                <h2 class="text-center page-title">Leave Management System</h2>
                <!-- Admin View -->
                <c:if test="${isAdmin}">
                    <div class="row h-100 g-2">
                        <div class="col-md-3">
                            <div class="card p-2">
                                <div class="card-header">User Management</div>
                                <div
                                    class="card-body d-flex justify-content-center align-items-center"
                                    >
                                    <a
                                        href="${pageContext.request.contextPath}/user/list"
                                        class="btn btn-primary"
                                        >Manage Users</a
                                    >
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card p-2">
                                <div class="card-header">Role Management</div>
                                <div
                                    class="card-body d-flex justify-content-center align-items-center"
                                    >
                                    <a
                                        href="${pageContext.request.contextPath}/role/list"
                                        class="btn btn-primary"
                                        >Manage Roles</a
                                    >
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card p-2">
                                <div class="card-header">Division Management</div>
                                <div
                                    class="card-body d-flex justify-content-center align-items-center"
                                    >
                                    <a
                                        href="${pageContext.request.contextPath}/division/list"
                                        class="btn btn-primary"
                                        >Manage Divisions</a
                                    >
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card p-2">
                                <div class="card-header">Leave Management</div>
                                <div
                                    class="card-body d-flex justify-content-center align-items-center"
                                    >
                                    <c:if test="${sessionScope.canViewAll}">
                                        <a
                                            href="${pageContext.request.contextPath}/leaverequest/list"
                                            class="btn btn-primary"
                                            >Manage Leave Requests</a
                                        >
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                <div class="row h-100">
                    <c:if test="${sessionScope.canCreate}">
                        <div class="col-4">
                            <div class="card mt-5">
                                <div class="card-header">
                                    <h4>Submit Leave Request</h4>
                                </div>
                                <%@ include file="/view/leaverequest/create-form.jspf" %>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${sessionScope.canViewOwn and not sessionScope.canViewSubs}">
                        <div class="col-8">
                            <%@ include file="/view/leaverequest/myrequests.jsp" %>
                        </div>
                    </c:if>
                    <c:if test="${sessionScope.canViewOwn and sessionScope.canViewSubs}">
                        <div class="col-8">
                            <ul class="nav nav-tabs" id="tabs" role="tablist">
                                <li class="nav-item" role="presentation">
                                    <button
                                        class="nav-link ${activeTab == 'my' ? 'active' : ''}"
                                        id="my-tab"
                                        data-bs-toggle="tab"
                                        data-bs-target="#my"
                                        type="button"
                                        role="tab"
                                        >
                                        My Requests
                                    </button>
                                </li>
                                <li class="nav-item" role="presentation">
                                    <button
                                        class="nav-link ${activeTab == 'subs' ? 'active' : ''}"
                                        id="subs-tab"
                                        data-bs-toggle="tab"
                                        data-bs-target="#subs"
                                        type="button"
                                        role="tab"
                                        >
                                        Subordinates
                                    </button>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div
                                    class="tab-pane fade ${activeTab == 'my' ? 'show active' : ''} h-100"
                                    id="my"
                                    role="tabpanel"
                                    >
                                    <%@ include file="/view/leaverequest/myrequests-table.jspf" %>
                                </div>
                                <div class="tab-pane fade ${activeTab == 'subs' ? 'show active' : ''} h-100" id="subs" role="tabpanel">
                                    <%@ include file="/view/leaverequest/subsrequests-table.jspf" %>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
            <%@ include file="/view/common_jsp_components/footer.jspf" %>
        </body>
    </html>
