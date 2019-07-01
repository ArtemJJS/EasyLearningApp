<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 6/29/2019
  Time: 7:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="start_page.css">
</head>
<body>
<header>
    <div class="logo">
        <img alt="logo" src="${pageContext.request.contextPath}/resources/account_avatar/1.png">
<%--        <c:out value="${pageContext.request.contextPath}/resources/account_avatar/1.png"/>--%>
    </div>
</header>
<main>
    <div class="header1">Registration form:</div>
    <form action="http://localhost:8080/easyLearning/basic_servlet">
        <div class="param0">
            <label for="role-field">Role: </label>
            <input type="text" id="role-field" name="role" value="role">
        </div>
        <div class="param1">
            <label for="login-field">Login: </label>
            <input type="text" id="login-field" name="login" value="login">
        </div>
        <div class="param2">
            <label for="password-field">Password: </label>
            <input type="password" id="password-field" name="password" value="password">
        </div>
        <div class="param3">
            <label for="name-field">Name: </label>
            <input type="text" id="name-field" name="name" value="name">
        </div>
        <div class="param4">
            <label for="surname-field">Surname: </label>
            <input type="text" id="surname-field" name="surname" value="surname">
        </div>
        <div class="param5">
            <label for="email-field">Email: </label>
            <input type="text" id="email-field" name="email" value="email">
        </div>
        <div class="param6">
            <label for="birthdate-field">Birthdate: </label>
            <input type="text" id="birthdate-field" name="birthdate" value="birthdate">
        </div>
        <div class="param7">
            <label for="phonenumber-field">Phone number: </label>
            <input type="text" id="phonenumber-field" name="phonenumber" value="phonenumber">
        </div>
        <div class="param8">
            <label for="about-field">About: </label>
            <input type="text" id="about-field" name="about" value="about   ">
        </div>
        <div>
            <input type="hidden" name="command_name" value="sign_up_new_user">
            <input type="submit" value="Submit!">
        </div>
    </form>
</main>
</body>

</html>
