<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/20/2019
  Time: 12:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>Easy Learning</title>
    <style>
        <%@include file="/css/admin/approve_acc_avatar_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<c:set var="accounts" value="${pageContext.request.getAttribute('acc_avatar_approve_list')}"/>
<main>
    <div class="page_title"><fmt:message key='change.change_acc_avatar_requests' bundle='${rb}'/>:</div>
    <div class="message">${pageContext.request.getAttribute("message")}</div>
    <c:forEach var="account" items="${accounts}">
        <div class="acc_wrapper">
            <div class="info_block">
                <div class="avatar">
                    <div class="img_detail"><fmt:message key='change.new_avatar' bundle='${rb}'/>:</div>
                    <img src="${pageContext.request.contextPath}/img/${account.updatePhotoPath}" alt="">
                </div>
                <div class="acc_info">
                    <div class="acc_login">${account.login}</div>
                    <div class="acc_role">${account.type.toString()}</div>
                </div>
                <div class="avatar">
                    <div class="img_detail"><fmt:message key='change.current_avatar' bundle='${rb}'/>:</div>
                    <img src="${pageContext.request.contextPath}/img/${account.pathToPhoto}">
                </div>
            </div>
            <div class="forms">
                <form method="post" action="${pageContext.request.contextPath}/admin/approve-acc-avatar">
                    <input type="hidden" name="login" value="${account.login}">
                    <input type="hidden" name="command_name" value="approve_avatar_change">
                    <input type="submit" value='<fmt:message key='btn.approve' bundle='${rb}'/>'>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/admin/approve-acc-avatar">
                    <input type="hidden" name="login" value="${account.login}">
                    <input type="hidden" name="command_name" value="decline_avatar_change">
                    <input type="submit" value='<fmt:message key='btn.decline' bundle='${rb}'/>'>
                </form>
            </div>
        </div>
    </c:forEach>
</main>
<script>
    <%@include file="/js/localization_hidden.js"%>
</script>
</body>
</html>
