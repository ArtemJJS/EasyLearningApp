<%@ page import="by.anelkin.easylearning.entity.Account" %><%--
  Created by IntelliJ IDEA.
  User: User
  Date: 6/26/2019
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>EasyLearning</title>
    <style>
        <%@include  file="/css/main_page.css" %>
    </style>
</head>
<body>
<%@include file="header.jsp" %>
<main>
    <div class="banner"><fmt:message key='main.learn_easy' bundle='${rb}'/><br><fmt:message
            key='main.everytime_everywhere' bundle='${rb}'/></div>
    <div class="greeting"><fmt:message key='main.welcome_back' bundle='${rb}'/>${sessionScope.user.name}! <fmt:message
            key='main.are_you_ready_to_study' bundle='${rb}'/></div>
    <div class="courses_block">
        <c:set var="courses_available" value="${sessionScope.coursesAvailable}"/>
        <c:choose>
            <c:when test="${courses_available == null}">
                <div><fmt:message key='main.you_do_not_have_available_courses' bundle='${rb}'/></div>
            </c:when>
            <c:otherwise>
                <div class="course_block_header"><fmt:message key='main.courses_that_you_have_purchased_already'
                                                              bundle='${rb}'/></div>
                <c:forEach var="course" items="${courses_available}">
                    <div class="about_course">
                        <div class="block1">
                            <a class="image_link" href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                                <img class="course_avatar"
                                     src="${pageContext.request.contextPath}/img${course.pathToPicture}"
                                     alt='<fmt:message key='global.course_image' bundle='${rb}'/>'/>
                            </a>
                        </div>
                        <div class="block2">
                            <div class="name_and_desc">
                                <div class="course_name">
                                    <a href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                                            ${course.name}
                                    </a>
                                </div>
                                <div class="course_desc">${course.description}</div>
                            </div>
                            <ctg:course-options course="${course}"/>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="courses_block">
        <c:set var="courses_recommended"
               value="${sessionScope.get('courses_recommended')}"/>
        <div class="course_block_header"><fmt:message key='main.courses_that_are_recommended' bundle='${rb}'/></div>
        <c:forEach var="course" items="${courses_recommended}">
            <div class="about_course">
                <div class="block1">
                    <a href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                        <img class="course_avatar"
                             src="${pageContext.request.contextPath}/img${course.pathToPicture}"
                             alt='<fmt:message key='global.course_image' bundle='${rb}'/>'/>
                    </a>
                </div>
                <div class="block2">
                    <div>
                        <div class="course_name">
                            <a href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                                    ${course.name}
                            </a>
                        </div>
                        <div class="course_desc">${course.description}</div>
                    </div>
                    <ctg:course-options course="${course}"/>
                </div>
            </div>
        </c:forEach>
    </div>
</main>
</body>
</html>
