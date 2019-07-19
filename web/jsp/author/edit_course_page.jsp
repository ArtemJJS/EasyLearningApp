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
    <c:set var="course" value="${requestScope.requestedCourse}" property=""/>
    <c:set var="content" value="${requestScope.currentCourseContent}"/>
    <div class="page_title">Edit course:</div>
    <form class="main_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="param">
            <div class="unit_title">Course title:</div>
            <div class="input_and_pattern">
                <div class="param_field">${course.name}</div>
                <input value="${course.name}" type="text" id="course_title_field" name="course_name" hidden
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
        <jsp:useBean id="i" class="by.anelkin.easylearning.entity.JspIntWrapper"/>
        <c:forEach var="entry" items="${content}">
            <c:set var="chapter" value="${entry.getKey()}"/>
            <div id="${i.value}" class="chapter">
                <div class="param chapter">
                    <div class="unit_title">Chapter ${i.value}:</div>
                    <div class="param_field">${chapter.name}</div>
                    <input value="${chapter.name}" type="text" id="chapter_name" name="chapter_name" hidden
                           pattern="[A-z0-9_ -]{5,200}">
                </div>
                <div class="lessons">
                    <jsp:useBean id="j" class="by.anelkin.easylearning.entity.JspIntWrapper"/>
                    <c:forEach var="lesson" items="${entry.getValue()}">
                        <div class="param lesson">
                            <div class="unit_title">Lesson ${j.receiveAndIncrement()}:</div>
                            <div class="lesson_params">
                                <div class="param_field">${lesson.name}</div>
                                <div class="param_field">${lesson.pathToContent}</div>
                                <div class="param_field">${lesson.duration}</div>
                            </div>
                        </div>
                    </c:forEach>
                    <div class="add_lesson">add lesson</div>
                </div>
            </div>
            ${j.resetToOne()}
            ${i.increment()}
        </c:forEach>

        <div class="add_chapter">add chapter</div>
        <input type="hidden" name="command_name" value="add_course_to_review">
        <input class="submit_btn" type="submit" value="Send to review">
    </form>
    <script>
        <%@include file="/js/add_lesson.js"%>
    </script>
</main>
</body>
</html>
