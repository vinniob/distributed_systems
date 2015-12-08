<%-- 
    Document   :    signup
    Author     :    Rory Burns, 10108696
                    Vincent O'Brien, 10111255
                    Jamie Chambers, 10116532
                    Ger Lynch, 10115293
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    <jsp:attribute name="header">
        <title>Sign Up</title>
    </jsp:attribute>
    <jsp:body>
        <h1>Sign Up</h1>
        <p>Please Enter Details For A New Account Below</p>
        <form action="signup" method="POST">
            <li><label for="name">Name:</label>
            <input type="text" name="fullname" id="fullname"></li>
            <li><label for="email">Email:</label>
            <input type="text" name="email" id="email"></li>
            <li><label for="username">Username:</label>
            <input type="text" name="username" id="username"></li>
            <li><label for="password">Password:</label>
            <input type="password" name="password" id="password"></li>
            <br>
            <br>
            <br>
            <%--select whether an admin or customer account--%>
            <legend>Administrator or Customer</legend>
            <br>
            <input id="admin_radio" type="radio" name="admin_or_user" value="a">
            <label for="admin_radio">Administrator</label>
            <input id="user_radio" type="radio" name="admin_or_user" value="u" checked="">
            <label for="user_radio">Customer</label>
            <input type="submit" name="Submit">
        </form>
    </jsp:body>
</t:template>
