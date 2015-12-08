<%-- 
    Document   :    viewlogs
    Author     :    Rory Burns, 10108696
                    Vincent O'Brien, 10111255
                    Jamie Chambers, 10116532
                    Ger Lynch, 10115293
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
    <jsp:attribute name="header">
        <title>Admin Console: View Logs</title>
    </jsp:attribute>
    <jsp:body>
        <h1>Log Viewer</h1>
        <%--table displaying logs--%>
        <table width="500px" cellspacing="0" cellpadding="1" border="1">
            <thead>
                <tr>
                    <td>ID</td>
                    <td>Comment, Product (ID,Quantity) </td>
                    <td>Date Created</td>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="log" items="${logs}">
                    <tr>
                        <td>${log.id}</td>
                        <td>${log.comment}</td>
                        <td>${log.dateCreated}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <form action="${pageContext.request.contextPath}/admin">
        <input type="submit" value="Edit Product Database">
        </form>
    </jsp:body>
</t:template>
