<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/12/2019
  Time: 2:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>Easy Learning</title>
    <style>
        <%@include file="/css/course/learn_course.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <c:set var="course" value="${pageContext.request.getAttribute('requestedCourse')}"/>
    <c:set var="author" value="${pageContext.request.getAttribute('author_of_course')}"/>
    <c:set var="course_content" value="${requestScope.get('currentCourseContent')}"/>
<%--    <c:out value="${course_content}">--%>
<%--        .get(0).getValue().get(0).pathToContent--%>
    <section class="section_1">
        <div class="video_content">
            <iframe id="video_player" src="https://drive.google.com/file/d/1iEEp7Yxcta_g6tRhC8b0J_EzTEbvacqi/preview"
                    width="100%" height="100%" allowfullscreen></iframe>
        </div>
        <div class="course_about">
            <div class="course_title">${course.name}</div>
            <div class="rating_and_author">
                <div class="course_rating"><fmt:message key='global.rating' bundle='${rb}'/>: ${course.avgMark}</div>
                <div class="course_author"><a
                        href="${pageContext.request.contextPath}/author-info/${author.login}"><fmt:message key='global.author' bundle='${rb}'/>: ${author.login}</a>
                </div>
            </div>
            <div class="course_description">${course.description}</div>
        </div>
    </section>
    <section id="content_section" class="section_2">
        <div id="sidebar" class="content"><fmt:message key='course.course_content' bundle='${rb}'/>:
            <div class="lessons_minutes">
                <div class='lessons'>${course.getLessonAmount()} <fmt:message key='course.lessons' bundle='${rb}'/></div>
                <div class='seconds'>
                    <ctg:time-prettier secondsAmount='${course.getDuration()}'/></div>
            </div>
        </div>

        <c:forEach var="chapter_lesson" items="${course_content}">
            <c:set var="chapter" value="${chapter_lesson.getKey()}"/>
            <div class="chapter">
                <div class="materials_title">${chapter.getName()}</div>
                <div class="lessons_minutes">
                    <div class='lessons'>${chapter.getLessonAmount()} <fmt:message key='course.lessons' bundle='${rb}'/></div>
                    <div class='seconds'>
                        <ctg:time-prettier secondsAmount='${chapter.getDuration()}'/></div>
                </div>
            </div>
            <c:forEach var="lesson" items="${chapter_lesson.getValue()}">
                <div id="${lesson.getId()}" class='lesson'>
                    <div class="lesson_title">${lesson.getName()}</div>
                    <div class="lesson_duration"><ctg:time-prettier secondsAmount="${lesson.getDuration()}"/></div>
                </div>
            </c:forEach>
        </c:forEach>
    </section>
</main>
<script>let lessons_list = "${pageContext.request.getAttribute('currentCourseContent').values()}"</script>
<script type="text/javascript">
    <%@include file="/js/learn_course.js"%>
</script>
</body>
</html>
