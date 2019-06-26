<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 6/26/2019
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EasyLearning</title>
</head>
<body>
    <% String login = request.getParameter("login");
    out.println("<h1> HELLO, " + login + "!!! </h1>");
    %>
</body>

</html>
