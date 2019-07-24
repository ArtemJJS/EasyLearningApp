<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/23/2019
  Time: 1:16 AM
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
        <%@include file="/css/user/mark_author.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp"%>
<main>
    <div class="page_title"><fmt:message key='global.mark_author' bundle='${rb}'/>:</div>
    <form class="mark_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="params">
            <div class="param">
                <label for="mark_value_field_1"><fmt:message key='mark.mark' bundle='${rb}'/>: </label>
                <div class="mark_selectors">
                    <label for="mark_value_field_1">1</label>
                    <input id="mark_value_field_1" type="radio" name="mark_value" value="1">
                    <label for="mark_value_field_2">2</label>
                    <input id="mark_value_field_2" type="radio" name="mark_value" value="2">
                    <label for="mark_value_field_3">3</label>
                    <input id="mark_value_field_3" type="radio" name="mark_value" value="3">
                    <label for="mark_value_field_4">4</label>
                    <input id="mark_value_field_4" type="radio" name="mark_value" value="4">
                    <label for="mark_value_field_5">5</label>
                    <input id="mark_value_field_5" type="radio" name="mark_value" value="5">
                </div>
            </div>
            <div class="param">
                <div class="label_and_pattern">
                    <label for="mark_comment"><fmt:message key='mark.comment' bundle='${rb}'/>:</label>
                    <div class="pattern-info"><fmt:message key='mark.max_1000_symbols' bundle='${rb}'/></div>
                </div>
                <textarea id="mark_comment" name="comment" placeholder='<fmt:message key='mark.write_your_comment' bundle='${rb}'/>' rows="5"
                          cols="40"></textarea>
            </div>
        </div>
        <input type="hidden" name="command_name" value="mark_author"/>
        <input type="hidden" name="author_id" value="${pageContext.request.getParameter("author-id")}"/>
        <input class="submit_btn" type="submit" value='<fmt:message key='btn.submit' bundle='${rb}'/>'>
    </form>
</main>
</body>
</html>
