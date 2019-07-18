<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/18/2019
  Time: 12:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyLearning</title>
    <style>
        <%@include file="/css/author/add_course_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="page_title">Create new course and send to review:</div>
    <form class="main_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="param">
            <label for="course_title_field">Course title:</label>
            <div class="input_and_pattern">
                <input placeholder="Title" type="text" id="course_title_field" name="course_name" required
                       pattern="[A-z0-9_ -]{5,200}">
                <%--            <c:if test="${pageContext.request.getAttribute('wrong-course-name') == 'true'}">--%>
                <%--                <p class="field_desc" style="color: red">This course title is in use already, try another one! </p>--%>
                <%--            </c:if>--%>
                <p class="field_desc">Available characters: English letters, digits, _ and - symbols, space. Min 5, max
                    200
                    symbols. </p>
            </div>
        </div>
        <div class="param">
            <label for="price-field">Price in USD:</label>
            <div class="input_and_pattern">
                <input type="text" id="price-field" name="course_price" placeholder="0.00" required
                       pattern="[0-9]{1,8}(\.[0-9]{1,2})?">
                <p class="field_desc">Format: 12345678.12</p>
            </div>
        </div>
        <div class="param">
            <label for="course_desc">Course description:</label>
            <textarea id="course_desc" name="course_description" placeholder="Description" cols="25"
                      rows="7"></textarea>
        </div>

        <%--    =================Chapters======================    --%>

        <div id="1" class="chapter">
            <div class="param chapter">
                <label for="chapter-field_1">Chapter 1:</label>
                <input type="text" id="chapter-field_1" name="chapter_name" placeholder="Chapter title" required
                       pattern='[A-z0-9_ -]{3,200}'>
                <%--            <p class="field_desc">Format: 12345678.12</p>--%>
            </div>
            <div class="lessons">
                <div class="param lesson">
                    <label for="lesson_field">Lesson 1:</label>
                    <div class="lesson_params">
                        <input type="text" id="lesson_field" name="lesson_title_1" placeholder="Lesson title"
                               pattern='[A-z0-9_ -]{3,100}'>
                        <input type="text" name="lesson_content_1" placeholder="Path to content">
                        <input type="text" name="lesson_duration_1" placeholder="Duration">
                    </div>
                </div>
                <div class="add_lesson">add lesson</div>
            </div>
        </div>
        <div class="add_chapter">add chapter</div>

        <input type="hidden" name="command_name" value="add_course_to_review">
        <input class="submit_btn" type="submit" value="Send to review">
    </form>
</main>
<script>
    <%@include file="/js/add_lesson.js"%>
</script>
</body>
</html>
