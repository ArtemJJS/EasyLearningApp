<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/12/2019
  Time: 1:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyLearning</title>
    <style>
        <%@include file="/css/author.css"%>
    </style>
</head>
<body>
<%@include file="header.jsp" %>
<main>
    <C:set var="author" value="${pageContext.request.getAttribute('requested_author')}"/>
    <C:set var="courses" value="${pageContext.request.getAttribute('author_course_list')}"/>
    <section class="section_1">
        <div class="nickname"> ${author.login}</div>
        <div class="name_surname">${author.name} ${author.surname}</div>
    </section>
    <section class="section_2">
        <img class="author_avatar" src="${pageContext.request.contextPath}/${author.pathToPhoto}"
             alt="avatar"/>
        <div class="author_description_and_stats">
            <div class="author_about">${author.about}</div>
            <div class="author_stats">
                <div class="author_rating">Rating: ${author.avgMark}</div>
                <div class="author_students">Students: 352</div>
                <div class="courses_amount">Courses: ${courses.size()}</div>
            </div>
        </div>
    </section>
    <section class="section_3">
        <div class="course_list_title">Courses from ${author.login}:</div>
        <c:forEach var="course" items="${courses}">
            <div class="about_course">
                <div class="block1">
                    <a href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                        <img class="course_avatar"
                             src="${pageContext.request.contextPath}${course.pathToPicture}"
                             alt="course avatar"/>
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
    </section>
</main>
</body>
</html>
