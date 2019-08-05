<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/22/2019
  Time: 11:23 PM
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
        <%@include file="/css/user/mark_course.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<c:set var="course" value="${requestScope.requestedCourse}"/>
<c:set var="account" value="${sessionScope.user}"/>
<main>
    <div class="page_title">
        <div class="title_and_price">
            <div class="title"><fmt:message key='mark.mark_next_course' bundle='${rb}'/>:</div>
            <div class="course_price"><fmt:message key='global.price' bundle='${rb}'/>: ${course.price}$</div>
        </div>
        <div class="course_name">${course.name}</div>
        <img class="course_img" src="${pageContext.request.contextPath}/img/${course.pathToPicture}" alt='<fmt:message key='global.course_image' bundle='${rb}'/>'>
    </div>
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
                    <div class="pattern-info"><fmt:message key='mark.max_700_symbols' bundle='${rb}'/></div>
                </div>
                <textarea id="mark_comment" name="comment" placeholder="<fmt:message key='mark.write_your_comment' bundle='${rb}'/>" rows="5"
                          cols="40" maxlength="700"></textarea>
            </div>
        </div>
        <input type="hidden" name="command_name" value="mark_course"/>
        <input type="hidden" name="target_id" value="${pageContext.request.getParameter("course-id")}"/>
        <input class="submit_btn" type="submit" value='<fmt:message key='btn.submit' bundle='${rb}'/>'>
    </form>
</main>
</body>
</html>
