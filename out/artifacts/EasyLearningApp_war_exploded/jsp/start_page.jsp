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
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>EasyLearning</title>
    <style>
        <%@include file="/style/start_page.css" %>
    </style>
</head>
<body>
<section class="section1">
    <div class="header">EasyLearning</div>
    <div class="description">The best learning site ever!!!</div>
</section>
<section class="section2">
    <form class="log_in" action="http://localhost:8080/easyLearning/login">
        <input class="text_field" type="text" name="login" value="login">
        <input class="text_field" type="text" name="password" value="password">
        <input type="submit" value="login!">
    </form>
</section>
        <% String login_message = (String) request.getAttribute("wrong_login_message");
        if (login_message!=null) {
            out.println("<div class='inv_login_message'>" + login_message + "</div>");
        }
        %>
<section class="section3">
    <div class="desc_sign_up"> Still don't have an account? Register now for free!</div>
    <form class="sign_up">
        <input class="sign_up_button" type="submit" value="Sign UP!">
    </form>
</section>
</body>

</html>
