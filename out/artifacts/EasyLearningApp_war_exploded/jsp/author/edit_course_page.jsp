<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/19/2019
  Time: 12:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyLearning</title>
    <style>
        <%@include file="/css/author/edit_course_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <c:set var="content" value="${requestScope.currentCourseContent}"/>
    <c:set var="course" value="${requestScope.requestedCourse}" property=""/>
    <div class="page_title">Edit course:</div>
    <form class="main_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="param">
            <label for="course_title_field">Course title:</label>
            <div class="input_and_pattern">
                <input placeholder="${course.name}" type="text" id="course_title_field" disabled
                       pattern="[A-z0-9_ -]{5,200}">
                <%--                <div class="previous_operation_msg">${requestScope.course_exists_msg}</div>--%>
                <p class="field_desc">Available characters: English letters, digits, _ and - symbols, space. Min 5, max
                    200
                    symbols. </p>
            </div>
        </div>
        <div class="param">
            <label for="price-field">Price in USD:</label>
            <div class="input_and_pattern">
                <input type="text" id="price-field" name="course_price" value="${course.price}" required
                       pattern="[0-9]{1,8}(\.[0-9]{1,2})?">
                <p class="field_desc">Format: 12345678.12</p>
            </div>
        </div>
        <div class="param">
            <label for="course_desc">Course description:</label>
            <textarea id="course_desc" name="course_description" cols="25"
                      rows="7">${course.description}</textarea>
        </div>

        <%--    =================Chapters======================    --%>
        <c:set var="i" value="1"/>
        <c:forEach var="entry" items="${content}">
            <c:set var="chapter" value="${entry.getKey()}"/>
            <div id="${i}" class="chapter">
                <div class="param chapter">
                    <label for="chapter-field_${i}">Chapter ${i}:</label>
                    <input type="text" id="chapter-field_${i}" name="chapter_name" value="${chapter.name}" disabled
                           pattern='[A-z0-9_ -]{3,200}'>
                        <%--            <p class="field_desc">Format: 12345678.12</p>--%>
                </div>
                <div class="lessons">
                    <c:set var="j" value="1"/>
                    <c:forEach var="lesson" items="${entry.getValue()}">
                        <div class="param lesson">
                            <label for="lesson_field">Lesson ${j}:</label>
                            <div class="lesson_params">
                                <input type="text" id="lesson_field" name="lesson_title_${i}" value="${lesson.name}"
                                       disabled
                                       pattern='[A-z0-9_ -]{3,100}'>
                                <input type="text" name="lesson_content_${i}" placeholder="Path to content" disabled>
                                <input type="text" name="lesson_duration_${i}" placeholder="Duration" disabled>
                            </div>
                        </div>
                        <c:set value="${j + '1'}" target="${j}"/>
                    </c:forEach>
                    <div class="add_lesson">add lesson</div>
                </div>
            </div>
<%--            <c:set value="${i = i+1}" />--%>
        </c:forEach>


        <div class="add_chapter">add chapter</div>

        <input type="hidden" name="command_name" value="add_course_to_review">
        <input class="submit_btn" type="submit" value="Send to review">
    </form>
    <script> var abc = ${content};
    <%--let lessonsListArray = ${requestScope.currentCourseContent.values()};--%>
    </script>
    <script>
                <%@include file="/js/add_lesson.js"%>
    </script>
    <script>
        <%@include file="/js/edit_course.js"%>
    </script>
</main>
</body>
</html>
