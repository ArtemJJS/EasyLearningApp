<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/25/2019
  Time: 8:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>Easy Learning</title>
    <%@ page isErrorPage="true" %>
    <style>
        <%@include file="/css/error404_page.css"%>
    </style>
</head>
<body>
<main>
    <img class="error_img" src="${pageContext.request.contextPath}/img/resources/error_404.jpg" alt="Error 404">
    <div class="error_message"><fmt:message key="error.page_not_found" bundle="${rb}"/></div>
    <a class="back_to_acc" href="${pageContext.request.contextPath}/account"><fmt:message key="btn.back_to_account" bundle="${rb}"/></a>
</main>
</body>
</html>
