<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/6/2019
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
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
            <div class="course_rating"><fmt:message key='course.mark_from_users' bundle='${rb}'/>: <ctg:write-rating rating='${curr_course.avgMark}'/>
                <br><fmt:message key='course.feedbacks' bundle='${rb}'/>: 34</div>
            <ctg:course-options course="${curr_course}"/>
        </div>
        <div class="course_view">
            <img class="course_avatar" src="${pageContext.request.contextPath}${curr_course.pathToPicture}"
                 alt="course">
            <div class="price">${curr_course.price}$</div>
        </div>
    </section>

    <section class="section_2">
        <div class="materials_title"><fmt:message key='course.course_materials' bundle='${rb}'/></div>
        <div class="lessons_minutes">
            <div>${curr_course.lessonAmount} <fmt:message key='course.lessons' bundle='${rb}'/></div>
            <div><ctg:time-prettier secondsAmount="${curr_course.duration}"/></div>
        </div>
    </section>

    <section class="section_3">
        <c:set var="course_content" value="${requestScope.get('currentCourseContent')}"/>
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

    <section class="section_4">
        <c:set var="author" value="${pageContext.request.getAttribute('author_of_course')}"/>
        <div class="author_image_and_stats">
            <a href="${pageContext.request.contextPath}/author-info/${author.login}"><img class="author_avatar"
                                                                                          src="${pageContext.request.contextPath}/${author.pathToPhoto}"
                                                                                          alt="author"></a>
            <div class="author_rating"><fmt:message key='global.rating' bundle='${rb}'/>: <ctg:write-rating rating="${author.avgMark}"/></div>
            <div class="author_students">322 <fmt:message key='course.students' bundle='${rb}'/></div>
        </div>
        <div class="author_about">
            <div class="author_login"><a class="author_link"
                                         href="${pageContext.request.contextPath}/author-info/${author.login}">${author.login}</a>
            </div>
            <div class="author_description">${author.about}</div>
        </div>
    </section>

    <section class="section_5">
        <div class="comments_title"><fmt:message key='course.feedback_from_users' bundle='${rb}'/>:</div>
        <c:set var="marks" value="${pageContext.request.getAttribute('currentCourseMarks')}"/>
        <c:forEach var="mark" items="${marks}">
            <div class="single_mark">
                <div class="mark_content">
                    <div class="value">Mark: ${mark.markValue}</div>
                    <div class="writer_login">${mark.accLogin}</div>
                    <div class="comment">${mark.comment}</div>
                    <div class="date">${mark.markDate}</div>
                </div>
                <img class="writer_avatar" src="${pageContext.request.contextPath}${mark.accPathToPhoto}"
                     alt="avatar"/>
            </div>
        </c:forEach>
    </section>
</main>
</body>
</html>
