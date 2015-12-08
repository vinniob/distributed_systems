<%-- 
    Document   :    template
    Created on :    7-Apr-2014, 13:12:51
    Author     :    Rory Burns, 10108696
                    Vincent O'Brien, 10111255
                    Jamie Chambers, 10116532
                    Ger Lynch, 10115293
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

        <style>
            h1 { font-size: 20px; font-family: Verdana; font-weight: bold }
            h2 {font-size: 18px; font-family: Verdana}
            p { font-size: 15px; font-family: Verdana }
            a { font-size: 15px; font-family: Verdana }
        </style>
        <jsp:invoke fragment="header"/>
    </head>
    <body>
        <header>            
            <h1>                             
                <a href="index.jsp">Home</a>
                <c:choose>
                    <c:when test="${not empty sessionScope.admin}">
                        <a  href="${pageContext.request.contextPath}/admin">Console</a>
                        <a  href="${pageContext.request.contextPath}/signout" 
                            title="You are signed in as admin ${sessionScope.admin.fullname}">Sign Out</a>
                    </c:when>
                    <c:when test="${not empty sessionScope.cust}">
                        <a  href="${pageContext.request.contextPath}/cart">Basket</a>
                        <a  href="${pageContext.request.contextPath}/signout" 
                            title="You are signed in as customer ${sessionScope.cust.fullname}">Sign Out</a>
                    </c:when>
                    <c:otherwise>
                        <a  href="${pageContext.request.contextPath}/signup">Sign Up</a>
                        <a href="${pageContext.request.contextPath}/signin">Sign In</a>
                    </c:otherwise>
                </c:choose>                
            </h1>    
            <form action="${pageContext.request.contextPath}/search" method="POST">
                <input type="text" title="Search" name="productId" 
                       placeholder="Search"/>
                <button type="submit" value="Go">
                    Go
                </button>
            </form>
        </header>
        <jsp:doBody/> 
    </body>
</html>