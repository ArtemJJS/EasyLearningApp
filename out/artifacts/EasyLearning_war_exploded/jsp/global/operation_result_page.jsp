<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 8/10/2019
  Time: 12:15 AM
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
        <%@include file="/css/global/operation_result_page.css"%>
    </style>
</head>
<body>
<main>
    <div class="page_title"><fmt:message key='msg.operation_proceeded' bundle='${rb}'/></div>
    <img src="${pageContext.request.contextPath}/img/resources/operation_success.png" alt="operation proceeded">
    <a class="back_to_acc" href="${pageContext.request.contextPath}/account"><fmt:message key="btn.back_to_account" bundle="${rb}"/></a>
</main>
</body>
</html>
