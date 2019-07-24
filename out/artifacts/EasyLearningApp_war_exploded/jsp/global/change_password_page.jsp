<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/17/2019
  Time: 12:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>easyLearning</title>
    <style>
        <%@include file="/css/global/change_password_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="page_content_wrapper">
        <div class="page_title"><fmt:message key='change.change_account_password' bundle='${rb}'/>:</div>
        <div id="result_message" class="previous_operation_msg">${pageContext.request.getAttribute("previous_operation_message")}</div>
        <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
            <div class="param">
                <label for="password-field"><fmt:message key='change.enter_previous_password' bundle='${rb}'/>: </label>
                <input type="password" id="password-field" name="password" placeholder='<fmt:message key='global.password' bundle='${rb}'/>' required pattern=".{5,50}">
                <p class="field_desc"><fmt:message key='registr.password_pattern' bundle='${rb}'/></p>
            </div>
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
            <input type="hidden" name="command_name" value="change_password">
            <input class="submit_btn" type="submit" value='<fmt:message key='btn.submit' bundle='${rb}'/>'>
        </form>
    </div>
</main>
<script>let operationFlag = ${requestScope.operation_result};</script>
<script>
    <%@include file="/js/change_pwd_page.js"%>
</script>
</body>
</html>
