<%@page import="com.entity.Division"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<html>
    <head><title>All Divisions</title></head>
    <body>
        <h2>Division List</h2>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Director</th>
            </tr>
            <%
                List<Division> divisions = (List<Division>) request.getAttribute("divisions");
                for (Division d : divisions) {
            %>
            <tr>
                <td><%= d.getDivisionID()%></td>
                <td><%= d.getDivisionName()%></td>
                <td><%= d.getDivisionDirector() != null ? d.getDivisionDirector() : "-"%></td>
            </tr>
            <% }%>
        </table>
    </body>
</html>
