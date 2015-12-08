<%-- 
    Document   :    error_404
    Author     :    Rory Burns, 10108696
                    Vincent O'Brien, 10111255
                    Jamie Chambers, 10116532
                    Ger Lynch, 10115293
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
  <jsp:attribute name="header">
    <title>An Error Occurred</title>
  </jsp:attribute>
  <jsp:body>
    <div style="font-size: 4em; text-align: center; font-weight: 100">
      Oppsy Daisy
      <span style="font-size: 2.5em; display: block; font-weight: 900">404</span>
      Something went wrong!!
    </div>
  </jsp:body>
</t:template>
