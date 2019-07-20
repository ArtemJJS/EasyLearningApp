<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/16/2019
  Time: 1:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        <%@include file="/css/user/account.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <c:set var="user" value="${sessionScope.user}"/>
    <c:set var="curr_role" value="${sessionScope.role}"/>
    <div class="img_and_links">
        <img src="${pageContext.request.contextPath}/${user.pathToPhoto}" alt="avatar">
        <div class="role">${sessionScope.role}</div>
        <div class="balance">Balance: ${user.balance}$</div>
        <div class="links">
            <div class="link"><a href="${pageContext.request.contextPath}/account/change-picture">Change photo</a></div>
            <div class="link"><a href="${pageContext.request.contextPath}/account/change-password">Change password</a></div>
            <div class="link"><a href="${pageContext.request.contextPath}/account/payments">Payments</a></div>
            <c:choose>
                <c:when test="${curr_role.toString() == 'USER'}">
                    <div class="link"><a href="${pageContext.request.contextPath}/user/purchased-courses">Purchased courses</a></div>
                    <div class="link"><a href="${pageContext.request.contextPath}/user/deposit">Deposit</a></div>
                </c:when>
                <c:when test="${curr_role.toString() == 'AUTHOR'}">
                    <div class="link"><a href="${pageContext.request.contextPath}/author/add-new-course">Add new course</a></div>
                    <div class="link"><a href="${pageContext.request.contextPath}/author/my-courses">My courses</a></div>
                    <div class="link"><a href="${pageContext.request.contextPath}/account/author/cash-out">CashOut</a></div>
                </c:when>
                <c:when test="${curr_role.toString() == 'ADMIN'}">
                    <div class="link"><a href="${pageContext.request.contextPath}/admin/course-approval">Add course request</a></div>
                    <div class="link"><a href="${pageContext.request.contextPath}/admin/approve-acc-avatar">Change img request</a></div>
                    <div class="link"><a href="">Dashboard</a></div>
                </c:when>
            </c:choose>
        </div>
    </div>
    <div class="update_section">
        <div class="header1">Profile:</div>
        <form id="reg_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
            <div class="param1">
                <label for="login-field">Login: </label>
                <input value="${user.login}" type="text" id="login-field" name="login" required
                       pattern="[A-z0-9_ -]{3,30}">
                <p class="field_desc">Available characters: English letters, digits, _ and - symbols, space. Min 3, max
                    30 symbols. </p>
            </div>
            <div class="param2">
                <label for="name-field">Name: </label>
                <input type="text" id="name-field" name="name" value="${user.name}" required
                       pattern="[A-z]{2,45}">
                <p class="field_desc">Latin letters only, min 2, max 45 symbols.</p>
            </div>
            <div class="param3">
                <label for="surname-field">Surname: </label>
                <input type="text" id="surname-field" name="surname" value="${user.surname}" required
                       pattern="[A-z]{2,45}">
                <p class="field_desc">Latin letters only, min 2, max 45 symbols.</p>
            </div>
            <div class="param4">
                <label for="email-field">Email: </label>
                <input type="email" id="email-field" name="email" value="${user.email}" required>
            </div>
            <div class="param5 ">
                <label for="phonenumber-field">Phone number: </label>
                <input type="text" id="phonenumber-field" name="phonenumber" value="${user.phoneNumber}"
                       pattern="+[0-9]{,19}">
                <p class="field_desc">Phone number. Format: +1234567890</p>
            </div>
            <div class="param6">
                <p>About:</p>
                <textarea id="about-field" name="about" cols="35" rows="7"
                          maxlength="250">${user.about}</textarea>
            </div>
            <div>
                <input type="hidden" name="command_name" value="edit_user_info">
                <input class="submit_btn" type="submit" value="Save changes">
            </div>
        </form>
    </div>
</main>
</body>
</html>
