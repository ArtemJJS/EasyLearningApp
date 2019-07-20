<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/20/2019
  Time: 12:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyLearning</title>
    <style>
        <%@include file="/css/admin/approve_acc_avatar_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<c:set var="accounts" value="${pageContext.request.getAttribute('acc_avatar_approve_list')}"/>

<main>
    <div class="page_title">Change avatar requests:</div>
    <c:forEach var="account" items="${accounts}">
        <div class="acc_wrapper">
            <div class="info_block">
                <div class="avatar">
                    <div class="img_detail">New Avatar:</div>
                    <img src="${pageContext.request.contextPath}/${account.updatePhotoPath}" alt="">
                </div>
                <div class="acc_info">
                    <div class="acc_login">${account.login}</div>
                    <div class="acc_role">${account.type.toString()}</div>
                </div>
                <div class="avatar">
                    <div class="img_detail">Current Avatar:</div>
                    <img src="${pageContext.request.contextPath}/${account.pathToPhoto}">
                </div>
            </div>
            <div class="forms">
                <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
                    <input type="hidden" name="login" value="${account.login}">
                    <input type="hidden" name="command_name" value="approve_avatar_change">
                    <input type="submit" value="approve">
                </form>
                <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
                    <input type="hidden" name="login" value="${account.login}">
                    <input type="hidden" name="command_name" value="decline_avatar_change">
                    <input type="submit" value="decline">
                </form>
            </div>
        </div>
    </c:forEach>
</main>
</body>
</html>
