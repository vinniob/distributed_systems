<%-- 
    Document   : index
    Author     :    Rory Burns, 10108696
                    Vincent O'Brien, 10111255
                    Jamie Chambers, 10116532
                    Ger Lynch, 10115293
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
    <jsp:attribute name="header">
        <title>Welcome</title>
    </jsp:attribute>
    <jsp:body>
        <h1>Product Catalog</h1>
        <c:choose>
            <c:when test="${not empty sessionScope.cust}">
                <p>Welcome ${sessionScope.cust.fullname}</p>
            </c:when>
        </c:choose>    
        <c:forEach var="product" items="${products}">        
            <h2>            
                <a href="product?${product.id}">${product.id}  ${product.title}</a>
                <b>&euro; ${product.price}</b>
            </h2>      
        </c:forEach>    
    </jsp:body>
</t:template>


