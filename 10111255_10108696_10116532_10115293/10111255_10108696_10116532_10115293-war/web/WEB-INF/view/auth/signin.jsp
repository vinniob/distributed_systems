<%-- 
    Document   :    signin
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
        <title>Sign In</title>
    </jsp:attribute>
    <jsp:body>
        <h1>Sign In</h1>
        <p>Please Enter Username and Password Below</p>
        <form action="signin" method="POST">
            <label for="username">Username:</label>
            <input type="text" name="username" id="username">
            <label for="password">Password:</label>
            <input type="password" name="password" id="password">
            <br>
            <%--select whether an admin or customer account--%>
            <p>Administrator or Customer</p>
            <br>
            <input id="admin_radio" type="radio" name="admin_or_user" value="a">
            <label for="admin_radio">Administrator</label>
            <input id="user_radio" type="radio" name="admin_or_user" value="u" checked="">
            <label for="user_radio">Customer</label>
            <input type="submit" name="Submit">
        </form>
        <form action="signup">
            <br>
            <p>Not a member yet?</p>
            <br>
            <input type="submit" value="Sign Up" name="SignUp">  
        </form>
    </jsp:body>
</t:template>
