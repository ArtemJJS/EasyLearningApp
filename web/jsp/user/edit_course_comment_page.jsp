<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 8/11/2019
  Time: 2:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Easy Learning</title>
    <style>
        <%@include file="/css/user/edit_course_comment_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<c:set var="course" value="${requestScope.requestedCourse}"/>
<%--<c:set var="account" value="${sessionScope.user}"/>--%>
<main>
    <div class="page_title">
        <div class="title_and_price">
            <div class="title"><fmt:message key='mark.mark_next_course' bundle='${rb}'/>:</div>
            <div class="course_price"><fmt:message key='global.price' bundle='${rb}'/>: ${course.price}$</div>
        </div>
        <div class="course_name">${course.name}</div>
        <img class="course_img" src="${pageContext.request.contextPath}/img/${course.pathToPicture}"
             alt='<fmt:message key='global.course_image' bundle='${rb}'/>'>
    </div>
    <form class="mark_form" method="post" action="${pageContext.request.contextPath}/edit_comment">
        <div class="param">
            <div class="label_and_pattern">
                <label for="mark_comment"><fmt:message key='mark.comment' bundle='${rb}'/>:</label>
                <div class="pattern-info"><fmt:message key='mark.max_700_symbols' bundle='${rb}'/></div>
            </div>
            <textarea id="mark_comment" name="mark_comment" rows="7" cols="45"
                      maxlength="700">${pageContext.request.getAttribute("mark_comment")}</textarea>
        </div>
        <input type="hidden" name="command_name" value="edit_course_comment"/>
        <input type="hidden" name="course-id" value="${course.id}"/>
        <input type="hidden" name="mark_id" value="${pageContext.request.getParameter('mark_id')}"/>
        <input class="submit_btn" type="submit" value='<fmt:message key='btn.submit' bundle='${rb}'/>'>
    </form>
</main>
</body>
</html>
