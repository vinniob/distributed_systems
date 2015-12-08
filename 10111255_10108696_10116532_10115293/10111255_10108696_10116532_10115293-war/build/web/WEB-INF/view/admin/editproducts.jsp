<%-- 
    Document   :    editproducts
    Author     :    Rory Burns, 10108696
                    Vincent O'Brien, 10111255
                    Jamie Chambers, 10116532
                    Ger Lynch, 10115293
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%--calls template--%>
<t:template>
    <jsp:attribute name="header">
        <title>Admin Console: Edit Products Database</title>
    </jsp:attribute>
    <jsp:body>
        <h1>Product Catalog</h1>
        <%--table to display products and related Info--%>
        <table width="500px" cellspacing="0" cellpadding="1" border="1">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <%--for each product in product add another row to table--%>
                <c:forEach var="product" items="${products}">
                <form action="admin/editproducts" method="POST">
                    <tr>
                        <td><input type="hidden" name="id" value="${product.id}">${product.id}</td>
                        <td><input type="text" name="title" value="${product.title}"></td>          
                        <td><input type="text" name="description" value="${product.description}"></td>
                        <td><input type="number" name="price" value="${product.price}"></td>
                        <td><input type="number" name="stock" value="${product.stock}"></td>
                        <td><input type="submit" value="Update" name="update"></td>
                        <td><input type="submit" value="Remove" name="remove"></td>
                    </tr>
                </form>
            </c:forEach>
            <%--row to add product to database--%>    
            <form action="admin/addproduct" method="POST" >
                <tr>
                    <td></td>
                    <td><input type="text" name="title"></td>          
                    <td><input type="text" name="description"></td>
                    <td><input type="number" name="price"></td>
                    <td><input type="number" name="stock"></td>
                    <td><input type="submit" value="Add" name="submit"></td>
                    <td></td>
                </tr>
            </form>
        </tbody>
    </table>
        <form action="${pageContext.request.contextPath}/admin/log" method="POST">
        <input type="submit" value="View Logs" name="submit">
        </form>
</jsp:body>
</t:template>