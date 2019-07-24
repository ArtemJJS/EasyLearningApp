<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/18/2019
  Time: 12:59 PM
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
        <%@include file="/css/author/add_course_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="page_title"><fmt:message key='course.create_new_course' bundle='${rb}'/>:</div>
    <form class="main_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="param">
            <label for="course_title_field"><fmt:message key='course.course_title' bundle='${rb}'/>:</label>
            <div class="input_and_pattern">
                <input placeholder='<fmt:message key='course.course_title' bundle='${rb}'/>' type="text" id="course_title_field" name="course_name" required
                       pattern="[A-z0-9_ -]{5,200}">
                <div class="previous_operation_msg">${requestScope.course_exists_msg}</div>
                <p class="field_desc"><fmt:message key='course.course_name_pattern' bundle='${rb}'/></p>
            </div>
        </div>
        <div class="param">
            <label for="price-field"><fmt:message key='course.price_in_usd' bundle='${rb}'/>:</label>
            <div class="input_and_pattern">
                <input type="text" id="price-field" name="course_price" placeholder="0.00" required
                       pattern="[0-9]{1,8}(\.[0-9]{1,2})?">
                <p class="field_desc"><fmt:message key='course.price_format' bundle='${rb}'/></p>
            </div>
        </div>
        <div class="param">
            <label for="course_desc"><fmt:message key='course.course_description' bundle='${rb}'/>:</label>
            <textarea id="course_desc" name="course_description" placeholder='<fmt:message key='course.description' bundle='${rb}'/>' cols="25"
                      rows="7"></textarea>
        </div>

        <%--    =================Chapters======================    --%>

        <div id="1" class="chapter">
            <div class="param chapter">
                <label for="chapter-field_1"><fmt:message key='course.chapter' bundle='${rb}'/> 1:</label>
                <input type="text" id="chapter-field_1" name="chapter_name" placeholder=<fmt:message key='course.chapter_title' bundle='${rb}'/> required
                       pattern='[A-z0-9_ -]{3,200}'>
            </div>
            <div class="lessons">
                <div class="param lesson">
                    <label for="lesson_field"><fmt:message key='course.lesson' bundle='${rb}'/> 1:</label>
                    <div class="lesson_params">
                        <input type="text" id="lesson_field" name="lesson_title_1" placeholder='<fmt:message key='course.lesson_title' bundle='${rb}'/>'
                               pattern='[A-z0-9_ -]{3,100}'>
                        <input type="text" name="lesson_content_1" placeholder='<fmt:message key='course.path_to_content' bundle='${rb}'/>'>
                        <input type="text" name="lesson_duration_1" placeholder='<fmt:message key='course.duration' bundle='${rb}'/>'>
                    </div>
                </div>
                <div class="add_lesson"><fmt:message key='course.add_lesson' bundle='${rb}'/></div>
            </div>
        </div>
        <div class="add_chapter"><fmt:message key='course.add_chapter' bundle='${rb}'/></div>

        <input type="hidden" name="command_name" value="add_course_to_review">
        <input class="submit_btn" type="submit" value='<fmt:message key='btn.send_to_review' bundle='${rb}'/>'>
    </form>
</main>
<script>
    <%@include file="/js/add_lesson.js"%>
</script>
</body>
</html>
