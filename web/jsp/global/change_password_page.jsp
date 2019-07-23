<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/17/2019
  Time: 12:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <div class="page_title">Change account password:</div>
        <div id="result_message" class="previous_operation_msg">${pageContext.request.getAttribute("previous_operation_message")}</div>
        <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
            <div class="param">
                <label for="password-field">Enter previous password: </label>
                <input type="password" id="password-field" name="password" placeholder="password" required pattern=".{5,50}">
                <p class="field_desc">Min 5, max 50 symbols.</p>
            </div>
            <div class="param">
                <label for="updated_password">Enter new password: </label>
                <input type="password" id="updated_password" name="updated_password" placeholder="new password" required pattern=".{5,50}">
                <p class="field_desc">Min 5, max 50 symbols.</p>
            </div>
            <div class="param">
                <label for="repeated_password">Repeat new password: </label>
                <input type="password" id="repeated_password" name="repeated_password" placeholder="repeat new password" required pattern=".{5,50}">
                <p class="field_desc">Min 5, max 50 symbols.</p>
            </div>
            <input type="hidden" name="command_name" value="change_password">
            <input class="submit_btn" type="submit" value="Submit">
        </form>
    </div>
</main>
<script>let operationFlag = ${requestScope.operation_result};</script>
<script>
    <%@include file="/js/change_pwd_page.js"%>
</script>
</body>
</html>
