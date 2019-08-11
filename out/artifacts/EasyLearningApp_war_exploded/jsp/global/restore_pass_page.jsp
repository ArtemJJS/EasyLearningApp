<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 8/5/2019
  Time: 9:14 PM
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
        <%@include file="/css/global/restore_pass_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp"%>
<main>
    <div class="page_title"><fmt:message key="restore.restore_password" bundle="${rb}"/></div>
    <div class="info-message"><fmt:message key="restore.info_message" bundle="${rb}"/></div>
    <div class="message">${requestScope.message}</div>
    <form method="post" action="${pageContext.request.contextPath}/restore-password">
        <div class="param">
            <label for="login_field"><fmt:message key="global.login" bundle="${rb}"/>:</label>
            <input id="login_field" type="text" name="login"
                   placeholder='<fmt:message key="global.login" bundle="${rb}"/>'>
        </div>
        <input type="hidden" name="command_name" value="restore_password">
        <input type="submit" value="<fmt:message key="btn.submit" bundle="${rb}"/>">
    </form>
    <a class="back_to_login_ref" href="${pageContext.request.contextPath}/login"><fmt:message key="restore.back_to_login_page" bundle="${rb}"/></a>
</main>
</body>
</html>
