<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/6/2019
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <style>
        <%@include file="/css/course/course.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <c:set var="curr_course" value="${pageContext.request.getAttribute('requestedCourse')}"/>
    <section class="section_1">
        <div class="course_title">
            <div class="course_name">${curr_course.name}</div>
            <div class="course_description">${curr_course.description}</div>
            <div class="course_rating">5.0<br>Feedbacks: 34</div>
        </div>
        <div class="course_view">
            <img class="course_avatar" src="${pageContext.request.contextPath}${curr_course.pathToPicture}"
                 alt="course">
            <div class="price">${curr_course.price}</div>
        </div>
    </section>

    <section class="section_2">
        <div class="materials_title">Course materials</div>
        <div class="lessons_minutes">
            <div>0 lessons</div>
            <div>0 minutes</div>
        </div>
    </section>

    <section class="section_3">
        <c:set var="course_content" value="${requestScope.get('currentCourseContent')}"/>
        <c:forEach var="chapter_lesson" items="${course_content}">
            <c:set var="chapter" value="${chapter_lesson.getKey()}"/>
            <div class="chapter">
                <div class="materials_title">${chapter.name}</div>
                <div class="lessons_minutes">
                    <div>0 lessons</div>
                    <div>0 minutes</div>
                </div>
            </div>
            <C:set var="lessons" value="${chapter_lesson.getValue()}"/>
            <c:forEach var="lesson" items="${lessons}">
                <div class="lesson">${lesson.name}</div>
            </c:forEach>
        </c:forEach>
    </section>

</main>
</body>
</html>
