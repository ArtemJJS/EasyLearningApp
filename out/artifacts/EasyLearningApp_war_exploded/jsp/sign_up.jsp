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
        <img src="http://localhost:8080/easyLearning/resources/2.jpg" alt="logo" width="180px" height="100px">
    </div>
</header>
<main>
    <div class="header1">Registration form:</div>
    <form id="reg_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="param0">
            <label for="role-field">Role: </label>
                <select id="role-field" name="role" required autofocus>
                    <option>User</option>
                    <option>Author</option>
                </select>
        </div>
        <div class="param1">
            <label for="login-field">Login*: </label>
            <input placeholder="login" type="text" id="login-field" name="login" required pattern="[A-z0-9_ -]{3,30}">
            <c:if test="${pageContext.request.getAttribute('wrong-login') == 'true'}">
                <p class="field_desc" style="color: red">This login is in use already, try another one! </p>
            </c:if>
            <p class="field_desc">Available characters: English letters, digits, _ and - symbols, space. Min 3, max 30 symbols. </p>
        </div>
        <div class="param2">
            <label for="password-field">Password*: </label>
            <input type="password" id="password-field" name="password" placeholder="password" required pattern=".{5,50}">
            <p class="field_desc">Min 5, max 50 symbols.</p>
        </div>
        <div class="param3">
            <label for="name-field">Name*: </label>
            <input type="text" id="name-field" name="name" placeholder="name" required pattern="[A-z]{2,45}">
            <p class="field_desc">Latin letters only, min 2, max 45 symbols.</p>
        </div>
        <div class="param4">
            <label for="surname-field">Surname*: </label>
            <input type="text" id="surname-field" name="surname" placeholder="surname" required pattern="[A-z]{2,45}">
            <p class="field_desc">Latin letters only, min 2, max 45 symbols.</p>
        </div>
        <div class="param5">
            <label for="email-field">Email*: </label>
            <input type="email" id="email-field" name="email" placeholder="email" required>
        </div>
        <div class="param6">
            <label for="birthdate-field">Birthdate*: </label>
            <input type="date" id="birthdate-field" name="birthdate" placeholder="birthdate" required>
        </div>
        <div class="param7 ">
            <label for="phonenumber-field">Phone number: </label>
            <input type="text" id="phonenumber-field" name="phonenumber" placeholder="+1234567890" pattern="+[0-9]{,19}">
            <p class="field_desc">Phone number. Format: +1234567890</p>
        </div>
        <div class="param8">
            <p>About:</p>
            <textarea id="about-field" name="about" placeholder="Write something about yourself." cols="25" rows="5" maxlength="250"></textarea>
        </div>
        <div>
            <input type="hidden" name="command_name" value="sign_up_new_user">
            <input class="submit_btn" type="submit" value="Submit!">
        </div>
    </form>
</main>
</body>

</html>
