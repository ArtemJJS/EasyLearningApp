<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 6/26/2019
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>EasyLearning</title>
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
        <input class="text_field" type="text" name="login"
               placeholder='<fmt:message key='global.login' bundle='${rb}'/>'>
        <input class="text_field" type="password" name="password"
               placeholder='<fmt:message key='global.password' bundle='${rb}'/>'>
        <input type="hidden" name="command_name" value="login"/>
        <input class="submit_btn" type="submit" value='<fmt:message key='btn.login' bundle='${rb}'/>'/>
    </form>
</section>
<c:set var="wrong_login" value="${pageContext.request.getAttribute('wrong-login')}"/>
<c:if test="${wrong_login.toString() == true}">
    <div class="inv_login_message"><fmt:message key='login.Username_or_Password_is_incorrect' bundle='${rb}'/></div>
</c:if>
<section class="section3">
    <a class="forgot_pass" href="${pageContext.request.contextPath}/restore-pass"><fmt:message key="login.forgot_password" bundle="${rb}"/></a>
    <div class="desc_sign_up"><fmt:message key='login.Still_do_not_have_an_account?' bundle='${rb}'/></div>
    <a href="${pageContext.request.contextPath}/sign-up"><fmt:message key="btn.sign_up" bundle="${rb}"/></a>
</section>
<img src="${pageContext.request.contextPath}/img/resources/acc_banner.jpg" alt="logo" width="80%" height="60%">
</body>
</html>