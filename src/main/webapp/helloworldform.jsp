<%-- 
    Document   : helloworldform
    Created on : Jul 1, 2012, 5:21:30 PM
    Author     : piter
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<style>
    .lf {
        width: 100px;
        float: left;
    }
</style>
<html>
    <%
        pageContext.setAttribute("person", request.getAttribute("person"));
        pageContext.setAttribute("error", request.getAttribute("error"));
    %>
    <jsp:useBean id="person" type="domain.Person" />
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Title</title>
    </head>
    <body>
        <form action="/javaflow.web/FlowServlet">
            <label class="lf" for="name">Name</label> 
            <input type="text"
                name="name" value="${person.name}" /> <br /> <label class="lf"
                for="lastname">Last name</label> <input type="text" name="lastname"
                value="${person.lastName}" /> <br />
            <div>${error}</div>
            <input type="submit" />
        </form>
    </body>
</html>
