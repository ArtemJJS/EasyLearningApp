<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 6/26/2019
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
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
    <div class="description"><fmt:message key='login.The_best_learning_site_ever' bundle='${rb}'/></div>
</section>
<section class="section2">
    <form class="log_in" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <input class="text_field" type="text" name="login" placeholder='<fmt:message key='btn.login' bundle='${rb}'/>'>
        <input class="text_field" type="password" name="password" placeholder='<fmt:message key='global.password' bundle='${rb}'/>'>
        <input type="hidden" name="command_name" value="login"/>
        <input type="submit" value='<fmt:message key='btn.login' bundle='${rb}'/>'/>
    </form>
</section>
        <c:set var="wrong_login" value="${pageContext.request.getAttribute('wrong-login')}"/>
        <c:if test="${wrong_login.toString() == true}">
            <div class="inv_login_message"><fmt:message key='login.Username_or_Password_is_incorrect' bundle='${rb}'/></div>
        </c:if>
<section class="section3">
    <div class="desc_sign_up"><fmt:message key='login.Still_do_not_have_an_account?' bundle='${rb}'/></div>
    <form class="sign_up" action="${pageContext.request.contextPath}/basic_servlet" >
        <input type="hidden" name="command_name" value="sign_up">
        <input class="sign_up_button" type="submit" value='<fmt:message key='btn.sign_up' bundle='${rb}'/>'>
    </form>
</section>
<img src="${pageContext.request.contextPath}/resources/Elephant.jpg" alt="logo" width="80%" height="60%">
</body>
</html>