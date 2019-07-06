<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 6/26/2019
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>EasyLearning</title>
<%--    <link type="text/css" href="${pageContext.request.contextPath}/css/start_page.css">--%>
<%--           <c:import url="${pageContext.request.contextPath}/css/start_page.css" charEncoding="utf-8"/>--%>
    <style>
        <%@include file="/css/start_page.css" %>
    </style>
</head>
<body>
<section class="section1">
    <div class="header">EasyLearning</div>
    <div class="description">The best learning site ever!!!</div>
</section>
<section class="section2">
    <form class="log_in" action="http://localhost:8080/easyLearning/basic_servlet">
        <input class="text_field" type="text" name="login" placeholder="login">
        <input class="text_field" type="text" name="password" placeholder="password">
        <input type="hidden" name="command_name" value="login">
        <input type="submit" value="Login!">
    </form>
</section>
        <c:set var="wrong_login" value="${pageContext.request.getAttribute('wrong-login')}"/>
        <c:if test="${wrong_login.toString() == true}">
            <div class="inv_login_message">${"Username or Password is incorrect! Try again, please!"}</div>
        </c:if>
<section class="section3">
    <div class="desc_sign_up"> Still don't have an account? Register now for free!</div>
    <form class="sign_up" action="http://localhost:8080/easyLearning/basic_servlet" >
        <input type="hidden" name="command_name" value="sign_up">
        <input class="sign_up_button" type="submit" value="Sign UP!">
    </form>
</section>
<img src="${pageContext.request.contextPath}/resources/Elephant.jpg" alt="logo" width="80%" height="60%">
</body>
</html>
