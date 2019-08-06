<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 8/6/2019
  Time: 12:13 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>Easy Learning</title>
    <style>
        <%@include file="/css/global/change_forgotten_pass_page.css"%>
    </style>
</head>
<body>
<main>
    <div class="page_content_wrapper">
        <div class="page_title"><fmt:message key='change.change_account_password' bundle='${rb}'/>:</div>


        <div id="result_message" class="message">${pageContext.request.getAttribute("message")}</div>


        <form method="post" action="${pageContext.request.contextPath}/change-forgotten-password">
            <div class="param">
                <label for="updated_password"><fmt:message key='change.enter_new_password' bundle='${rb}'/>: </label>
                <input type="password" id="updated_password" name="updated_password" placeholder='<fmt:message key='change.new_password' bundle='${rb}'/>' required pattern=".{5,50}">
                <p class="field_desc"><fmt:message key='registr.password_pattern' bundle='${rb}'/></p>
            </div>
            <div class="param">
                <label for="repeated_password"><fmt:message key='change.repeat_new_password' bundle='${rb}'/>: </label>
                <input type="password" id="repeated_password" name="repeated_password" placeholder='<fmt:message key='change.new_password' bundle='${rb}'/>' required pattern=".{5,50}">
                <p class="field_desc"><fmt:message key='registr.password_pattern' bundle='${rb}'/></p>
            </div>
            <input type="hidden" name="uuid" value="${pageContext.request.getParameter("uuid")}">
            <input type="hidden" name="command_name" value="change_forgotten_password">
            <input class="submit_btn" type="submit" value='<fmt:message key='btn.submit' bundle='${rb}'/>'>
        </form>
        <a class="back_to_login_ref" href="${pageContext.request.contextPath}/login"><fmt:message key="restore.back_to_login_page" bundle="${rb}"/></a>
    </div>
</main>
</body>
</html>
