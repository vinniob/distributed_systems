<%-- 
    Document   :    product
    Author     :    Rory Burns, 10108696
                    Vincent O'Brien, 10111255
                    Jamie Chambers, 10116532
                    Ger Lynch, 10115293
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    <jsp:attribute name="header">
        <title>Product - ${selectedProduct.title}</title>
    </jsp:attribute>
    <jsp:body>
        <c:choose>
            <c:when test="${not empty selectedProduct}">
                <h1>${selectedProduct.title}</h1>
                <p>&euro;${selectedProduct.price}</p>
                <c:choose>
                    <c:when test="${selectedProduct.stock > 0}">
                        <p>${selectedProduct.stock} in stock</p>
                    </c:when>
                    <c:otherwise>
                        <p>Out of Stock</p>
                    </c:otherwise>
                </c:choose>
                <h1>Product Details</h1>
                <p>${selectedProduct.description}</p>                
                <form action="cart" method="post">
                    <input type="hidden"
                           name="productId"
                           value="${selectedProduct.id}">
                    <input type="submit"
                           name="submit"
                           value="add to cart">
                </form>                
                <h1>Comments</h1>
                <c:choose>                        
                    <c:when test="${not empty selectedProduct.getCommentCollection()}">
                        <c:forEach var="comment" items="${selectedProduct.getCommentCollection()}" varStatus="iter">                                
                            <p>${comment.comment} by ${comment.custId.fullname}</p>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>No Comments</p>
                    </c:otherwise>
                </c:choose>  
                <form action="postcomment" method="POST">
                    <h1>Add a New Comment</h1>
                    <input type="hidden" name="product_id" value="${selectedProduct.id}">
                    <textarea type="text" name="comment" 
                              placeholder="Type here to add comment"></textarea>
                    <input type="submit" name="submit" value="submit">
                </form></c:when>
            <c:otherwise>
                <h1>Product Not Found</h1>
            </c:otherwise>
        </c:choose>
    </jsp:body>
</t:template>
