<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/12/2019
  Time: 2:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<html>
<head>
    <title>Title</title>
    <style>
        <%@include file="/css/course/learn_course.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <c:set var="course" value="${pageContext.request.getAttribute('requestedCourse')}"/>
    <c:set var="author" value="${pageContext.request.getAttribute('author_of_course')}"/>
    <section class="section_1">
        <div class="video_content">
            <iframe src="https://drive.google.com/file/d/1iEEp7Yxcta_g6tRhC8b0J_EzTEbvacqi/preview"
                    width="100%" height="100%" allowfullscreen></iframe>
        </div>
        <div class="course_about">
            <div class="course_title">${course.name}</div>
            <div class="rating_and_author">
                <div class="course_rating">Rating: ${course.avgMark}</div>
                <div class="course_author"><a
                        href="${pageContext.request.contextPath}/author/${author.login}">Author: ${author.login}</a></div>
            </div>
            <div class="course_description">${course.description}</div>
        </div>
    </section>
    <section class="section_2">
        <div id="sidebar" class="content">Course content:</div>
        <c:set var="course_content" value="${requestScope.get('currentCourseContent')}"/>
        <c:forEach var="chapter_lesson" items="${course_content}">
            <ctg:chapter-short-writer chapter="${chapter_lesson.getKey()}"/>
            <ctg:lesson-short-writer lessons="${chapter_lesson.getValue()}"/>
        </c:forEach>
    </section>
</main>
</body>
</html>
