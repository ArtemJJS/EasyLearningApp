<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/5/2019
  Time: 11:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>EasyLearning</title>
    <style>
        <%@include file="/css/header.css"%>
    </style>
</head>
<body>
<header>
    <div class="header-content">
        <div class="logo"><a href="${pageContext.request.contextPath}/easyLearning"><img src="${pageContext.request.contextPath}/resources/logo.jpg" alt="logo"></a></div>
        <div class="panel">
           <form id="header_search_form" action="${pageContext.request.contextPath}/search">
               <input id="header_hidden_input" type="hidden" name="command_name" value="search_course">
               <input class="header_search_field" type="text" name="search_key" placeholder="search course">
               <input class="header_submit_btn" type="submit" value="Search!">
           </form>
        </div>
        <div class="user">
            <c:choose>
                <c:when test="${sessionScope.user == null}">
                    <form action="${pageContext.request.contextPath}/jsp/start_page.jsp">
                        <input type="submit" name="go_login" value="Login"/>
                    </form>
                    <form action="${pageContext.request.contextPath}/jsp/sign_up.jsp">
                        <input type="submit" name="go_signup" value="Sign UP!"/>
                    </form>
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/${sessionScope.user.pathToPhoto}" alt="avatar"/>
                    <div class="user_login"><a href="${pageContext.request.contextPath}/account">${sessionScope.user.login}</a></div>
                    <form method="post" action="http://localhost:8080/easyLearning/basic_servlet">
                        <input type="hidden" name="command_name" value="log_out">
                        <input type="submit" name="LogOut" value="LogOut"/>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>
</body>
</html>
