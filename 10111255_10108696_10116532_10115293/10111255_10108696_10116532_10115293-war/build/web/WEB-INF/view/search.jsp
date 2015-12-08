<%-- 
    Document   :    search
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
    <h1>Search Results</h1>
      <c:forEach var="product" items="${searchResults}">
         <h2>            
            <a href="product?${product.id}">${product.id}  ${product.title}</a>
            <b>&euro; ${product.price}</b>
            </h2>          
      </c:forEach>
    </jsp:body>
</t:template>