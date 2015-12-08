<%-- 
    Document   :    cart
    Author     :    Rory Burns, 10108696
                    Vincent O'Brien, 10111255
                    Jamie Chambers, 10116532
                    Ger Lynch, 10115293
--%>            


<%@page import="model.ShoppingCart"%>
<%-- <%@page import="java.util.ArrayList"%>  --%>
<%@page import="model.ShoppingCartProductInfo"%>
<%--<%@page import="java.util.Collection"%> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    <jsp:attribute name="header">
        <title>Shopping Cart</title>        
    </jsp:attribute>     
    <jsp:body> 
        <h1>Cart Overview</h1>
        <c:choose>            
            <c:when test="${cart.numberOfProducts > 1}">
                <p>Your shopping cart contains ${cart.numberOfProducts} items.</p>
            </c:when>
            <c:when test="${cart.numberOfProducts == 1}">
                <p>Your shopping cart contains ${cart.numberOfProducts} item.</p>
            </c:when>
            <c:otherwise>
                <p>Your shopping cart is empty.</p>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <%--If cart is not empty show table displaying product info--%>
            <c:when test="${cart.numberOfProducts > 0}">
                <h1>${sessionScope.cust.fullname}'s Cart</h1>
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
                        <c:forEach var="prodInfo" items="${cart.items}" varStatus="iter">
                        <form action="updateCart" method="POST">
                            <tr>
                                <td><input type="hidden" name="id" value="${prodInfo.product.id}">${prodInfo.product.id}</td>
                                <td>${prodInfo.product.title}</td>
                                <td>&euro;${prodInfo.product.price}</td>
                                <td>${prodInfo.product.stock}</td>
                                <td><input type="number" name="quantity" value="${prodInfo.quantity}"></td>
                                <td>&euro;
                                    <fmt:formatNumber value="${prodInfo.getTotal()}" 
                                                      maxFractionDigits="2" minFractionDigits="2"/></td>
                                <td><input type="submit" value="Update" name="update"></td>
                                <td><input type="submit" value="Remove" name="remove"></td>
                            </tr>
                        </form>
                    </c:forEach>
                </tbody>
            </table>        
            <form action="purchase" method="POST">
                <input type="submit" value="Checkout" name="checkout">
            </form>
            <form action="cancel" method ="POST">
                <input type="submit" value="Cancel" name="cancel" >
            </form>    
        </c:when>
    </c:choose>
</jsp:body>
</t:template>

