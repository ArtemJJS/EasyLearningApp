<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/5/2019
  Time: 11:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>EasyLearning</title>
    <style>
        <%@include file="/css/header.css"%>
    </style>
</head>
<body>
<header>
    <div class="header-content">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/easyLearning"><img
                    src="${pageContext.request.contextPath}/resources/logo.jpg" alt="logo"></a>
        </div>
        <%--        <div class="panel">--%>
        <div class="choose_lang">
            <a href="${pageContext.request.contextPath}/change-lang?command_name=change_lang&locale=en_US"><fmt:message
                    key="header.lang_eng" bundle="${rb}"/></a>
            <a href="${pageContext.request.contextPath}/change-lang?command_name=change_lang&locale=ru_RU"><fmt:message
                    key="header.lang_ru" bundle="${rb}"/></a>
            <a href="${pageContext.request.contextPath}/change-lang?command_name=change_lang&locale=fr_FR"><fmt:message
                    key="header.lang_fr" bundle="${rb}"/></a>
        </div>
        <form id="header_search_form" action="${pageContext.request.contextPath}/search">
            <input id="header_hidden_input" type="hidden" name="command_name" value="search_course">
            <input class="header_search_field" type="text" name="search_key"
                   placeholder='<fmt:message key='header.search_placeholder' bundle='${rb}'/>'>
            <input class="header_search_btn" type="submit"
                   value='<fmt:message key="header.Search" bundle="${rb}"/>'>
        </form>
        <%--        </div>--%>
<%--        <div class="user">--%>
            <c:choose>
                <c:when test="${sessionScope.user == null}">
                    <div class="header_links">
                        <a href="${pageContext.request.contextPath}/login"><fmt:message key="global.login"
                                                                                        bundle="${rb}"/></a>
                        <a href="${pageContext.request.contextPath}/sign-up"><fmt:message key="btn.sign_up"
                                                                                          bundle="${rb}"/></a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="header_logged_user_info">
                        <img src="${pageContext.request.contextPath}/img/${sessionScope.user.pathToPhoto}" alt="avatar"/>
<%--                        <img src="${pageContext.request.contextPath}/${sessionScope.user.pathToPhoto}" alt="avatar"/>--%>
                        <div class="user_login"><a
                                href="${pageContext.request.contextPath}/account">${sessionScope.user.login}</a></div>
                        <form id="header_logout_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
                            <input type="hidden" name="command_name" value="log_out">
                            <input class="header_logout_btn" type="submit" name="LogOut" value='<fmt:message key="btn.log_out" bundle="${rb}"/>'/>
                        </form>
                    </div>
                </c:otherwise>
            </c:choose>
<%--        </div>--%>
    </div>
</header>
</body>
</html>
