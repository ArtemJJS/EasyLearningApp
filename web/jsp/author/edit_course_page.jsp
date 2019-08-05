<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/19/2019
  Time: 12:54 AM
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
        <%@include file="/css/author/edit_course_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <c:set var="course" value="${requestScope.requestedCourse}" property=""/>
    <c:set var="content" value="${requestScope.currentCourseContent}"/>
    <div class="page_title"><fmt:message key='course.edit_course' bundle='${rb}'/>:</div>
    <form class="main_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="param">
            <div class="unit_title"><fmt:message key='course.course_title' bundle='${rb}'/>:</div>
            <div class="input_and_pattern">
                <div class="param_field">${course.name}</div>
                <input type="hidden" value="${course.name}" id="course_title_field" name="course_name"
                       pattern="[A-z0-9_ -]{5,200}">
            </div>
        </div>
        <div class="param">
            <label for="price-field"><fmt:message key='course.price_in_usd' bundle='${rb}'/>:</label>
            <div class="input_and_pattern">
                <input type="text" id="price-field" name="course_price" value="${course.price}" required
                       pattern="[0-9]{1,8}(\.[0-9]{1,2})?">
                <p class="field_desc"><fmt:message key='course.price_format' bundle='${rb}'/></p>
            </div>
        </div>
        <div class="param">
            <label for="course_desc"><fmt:message key='course.course_description' bundle='${rb}'/>:</label>
            <textarea id="course_desc" name="course_description" cols="25"
                      rows="7" maxlength="1000">${course.description}</textarea>
        </div>

        <div class="info-message"><fmt:message key="msg.new_chapters_and_lessons_requirements" bundle="${rb}"/></div>
        <jsp:useBean id="i" class="by.anelkin.easylearning.entity.JspIntWrapper"/>
        <c:forEach var="entry" items="${content}">
            <c:set var="chapter" value="${entry.getKey()}"/>
            <div id="${i.value}" class="chapter">
                <div class="param chapter">
                    <div class="unit_title"><fmt:message key='course.chapter' bundle='${rb}'/> ${i.value}:</div>
                    <div class="param_field">${chapter.name}</div>
                    <input value="${chapter.name}" type="text" id="chapter_name" name="chapter_name" hidden
                           pattern="[A-z0-9_ -]{5,200}">
                </div>
                <div class="lessons">
                    <jsp:useBean id="j" class="by.anelkin.easylearning.entity.JspIntWrapper"/>
                    <c:forEach var="lesson" items="${entry.getValue()}">
                        <div class="param lesson">
                            <div class="unit_title"><fmt:message key='course.lesson' bundle='${rb}'/> ${j.receiveAndIncrement()}:</div>
                            <div class="lesson_params">
                                <div class="param_field">${lesson.name}</div>
                                <div class="param_field">${lesson.pathToContent}</div>
                                <div class="param_field">${lesson.duration}</div>
                            </div>
                        </div>
                    </c:forEach>
                    <div class="add_lesson"><fmt:message key='course.add_lesson' bundle='${rb}'/></div>
                </div>
            </div>
            ${j.resetToOne()}
            ${i.increment()}
        </c:forEach>

        <div class="add_chapter"><fmt:message key='course.add_chapter' bundle='${rb}'/></div>
        <input type="hidden" name="command_name" value="add_course_to_review">
        <input class="submit_btn" type="submit" value='<fmt:message key='btn.send_to_review' bundle='${rb}'/>'>
    </form>
    <script>let addLessonText = '<fmt:message key='course.add_lesson' bundle='${rb}'/>';
    let lessonText = '<fmt:message key='course.lesson' bundle='${rb}'/>';
    let chapterText = '<fmt:message key='course.chapter' bundle='${rb}'/>';
    let chapterTitleText = '<fmt:message key='course.chapter_title' bundle='${rb}'/>';
    let lessonTitleText = '<fmt:message key='course.lesson_title' bundle='${rb}'/>';
    let lessonDurationText = '<fmt:message key='course.duration' bundle='${rb}'/>';
    let lessonContentText = '<fmt:message key='course.path_to_content' bundle='${rb}'/>';
    </script>
    <script>
        <%@include file="/js/add_lesson.js"%>
    </script>
</main>
</body>
</html>
