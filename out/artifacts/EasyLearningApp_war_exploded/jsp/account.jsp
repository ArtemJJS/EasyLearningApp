<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 6/26/2019
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>EasyLearning</title>
    <%--    <c:import url="/css/account_page.css" >--%>
    <style>
        <%@include  file="/css/account_page.css" %>
    </style>
</head>
<body>
<header>
    <c:set var="user" value="${requestScope.get('user')}"/>
    <div class="logo"><img src="${pageContext.request.contextPath}/resources/logo.jpg" alt="logo"></div>
    <div class="panel">
        <div class="panel-item"><a href="">Search</a></div>
        <div class="panel-item"><a href="">News</a></div>
        <div class="panel-item"><a href="">Account</a></div>
        <div class="panel-item"><a href="">Become Author</a></div>
        <div class="panel-item"><a href="">Support</a></div>
    </div>
    <div class="user"><img src="${pageContext.request.contextPath}/resources/default_acc_avatar.png" alt="avatar"/>
    </div>
</header>
<main>
    <div class="banner">Learn EASY. <br>EVERYTIME. EVERYWHERE.</div>
</main>

</body>

</html>
