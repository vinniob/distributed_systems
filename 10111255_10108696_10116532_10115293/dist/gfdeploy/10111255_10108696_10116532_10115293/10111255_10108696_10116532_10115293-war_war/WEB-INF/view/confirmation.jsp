<%-- 
    Document   :    confirmation
    Author     :    Rory Burns, 10108696
                    Vincent O'Brien, 10111255
                    Jamie Chambers, 10116532
                    Ger Lynch, 10115293
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
    <jsp:attribute name="header">
        <title>Confirmation</title>
    </jsp:attribute>
    <jsp:body>
        <h1>Checkout Complete</h1>
        <p>You purchased the following items</p>
        <c:choose>
            <c:when test="${empty requestScope.errorMessage}">
                <table width="500px" cellspacing="0" cellpadding="1" border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Quantity</th>
                            <th>Subtotal</th>
                            <th></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>                        
                        <c:forEach var="line" items="${checkoutCart.items}" varStatus="iter">
                        <form action="updateCart" method="POST">
                            <tr>
                                <td><input type="hidden" name="id" value="${line.product.id}">${line.product.id}</td>
                                <td>${line.product.title}</td>
                                <td>&euro;${line.product.price}</td>
                                <td>${line.quantity}</td>
                                <td><input type="number" name="quantity" value="${line.quantity}"></td>
                                <td>&euro;
                                    <fmt:formatNumber value="${line.getTotal()}" 
                                                      maxFractionDigits="2" minFractionDigits="2"/></td>
                                <td><input type="submit" value="Update" name="update"></td>
                                <td><input type="submit" value="Remove" name="remove"></td>
                            </tr>
                        </form>
                    </c:forEach>
                </tbody>                
            </table>
        </c:when>
        <c:otherwise>
            <p>${errorMessage}</p>
        </c:otherwise>
    </c:choose>
</jsp:body>
</t:template>
