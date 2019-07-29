<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 6/29/2019
  Time: 7:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <style>
        <%@include file="/css/signup_page.css"%>
    </style>
</head>
<body>
<header>
    <div class="logo">
        <img src="${pageContext.request.contextPath}/img/resources/Elephant.jpg" alt="logo" width="180px" height="100px">
    </div>
</header>
<main>
    <div class="header1"><fmt:message key='registr.Registration_form' bundle='${rb}'/></div>
    <div class="message">${pageContext.request.getAttribute("message")}</div>
    <form id="reg_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="param0">
            <label for="role-field"><fmt:message key='registr.role' bundle='${rb}'/></label>
            <select id="role-field" name="role" required autofocus>
                <option><fmt:message key='registr.user' bundle='${rb}'/></option>
                <option><fmt:message key='registr.author' bundle='${rb}'/></option>
            </select>
        </div>
        <div class="param1">
            <label for="login-field"><fmt:message key='registr.login' bundle='${rb}'/></label>
            <input placeholder='<fmt:message key='global.login' bundle='${rb}'/>' type="text" id="login-field"
                   name="login" required pattern="[A-Za-zА-я0-9_ -]{3,30}">
            <c:if test="${pageContext.request.getAttribute('wrong-login') == 'true'}">
                <p class="field_desc" style="color: red"><fmt:message key='registr.This_login_is_in_use_already'
                                                                      bundle='${rb}'/></p>
            </c:if>
            <p class="field_desc"><fmt:message key='registr.login_pattern' bundle='${rb}'/></p>
        </div>
        <div class="param2">
            <label for="password-field"><fmt:message key='registr.password' bundle='${rb}'/></label>
            <input type="password" id="password-field" name="password"
                   placeholder='<fmt:message key='global.password' bundle='${rb}'/>' required pattern=".{5,50}">
            <p class="field_desc"><fmt:message key='registr.password_pattern' bundle='${rb}'/></p>
        </div>
        <div class="param3">
            <label for="name-field"><fmt:message key='registr.name' bundle='${rb}'/></label>
            <input type="text" id="name-field" name="name" placeholder='<fmt:message key='global.name' bundle='${rb}'/>'
                   required pattern="[A-Za-zА-я]{2,30}">
            <p class="field_desc"><fmt:message key='registr.name_pattern' bundle='${rb}'/></p>
        </div>
        <div class="param4">
            <label for="surname-field"><fmt:message key='registr.surname' bundle='${rb}'/></label>
            <input type="text" id="surname-field" name="surname"
                   placeholder='<fmt:message key='global.surname' bundle='${rb}'/>' required pattern="[A-Za-zА-я]{2,30}">
            <p class="field_desc"><fmt:message key='registr.surname_pattern' bundle='${rb}'/></p>
        </div>
        <div class="param5">
            <label for="email-field"><fmt:message key='registr.email' bundle='${rb}'/></label>
            <input type="email" id="email-field" name="email"
                   placeholder='<fmt:message key='global.email' bundle='${rb}'/>' required>
        </div>
        <div class="param6">
            <label for="birthdate-field"><fmt:message key='registr.birthdate' bundle='${rb}'/></label>
            <input type="date" id="birthdate-field" name="birthdate"
                   placeholder='<fmt:message key='global.birthdate' bundle='${rb}'/>' required>
        </div>
        <div class="param7 ">
            <label for="phonenumber-field"><fmt:message key='registr.phone' bundle='${rb}'/></label>
            <input type="text" id="phonenumber-field" name="phonenumber" placeholder="+1234567890"
                   pattern="\+[0-9]{2,19}">
            <p class="field_desc"><fmt:message key='registr.phone_pattern' bundle='${rb}'/></p>
        </div>
        <div class="param8">
            <p><fmt:message key='registr.about' bundle='${rb}'/></p>
            <textarea id="about-field" name="about"
                      placeholder='<fmt:message key='global.about_placeholder' bundle='${rb}'/>' cols="25" rows="5"
                      maxlength="500"></textarea>
        </div>
        <div>
            <input type="hidden" name="command_name" value="sign_up_new_user">
            <input class="submit_btn" type="submit" value="<fmt:message key='btn.submit' bundle='${rb}'/>">
        </div>
    </form>
</main>
</body>

</html>
