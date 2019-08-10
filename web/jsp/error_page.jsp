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
        <%@include file="/css/error_page.css"%>
    </style>
</head>
<body>
<main>

    <div class="page_title"><fmt:message key="error.ooops" bundle="${rb}"/></div>
    <div class="message">${pageContext.request.getAttribute("message")}</div>

    <div class="subtitle"><fmt:message key="error.Something_went_wrong" bundle="${rb}"/></div>
<%--    Request from ${pageContext.errorData.requestURI} is failed--%>
    <%--    <br/>--%>
    <%--    Servlet name: ${pageContext.errorData.servletName}--%>
    <%--    <br/>--%>
    <div class="error_code"><fmt:message key="error.error_code" bundle="${rb}"/><span class="code">  ${pageContext.errorData.statusCode}</span></div>
    <%--    <br/>--%>
    <%--    Exception: ${pageContext.exception}--%>
    <%--    <br/>--%>
    <a class="back_to_acc" href="${pageContext.request.contextPath}/account"><fmt:message key="btn.back_to_account" bundle="${rb}"/></a>
</main>
</body>
</html>
