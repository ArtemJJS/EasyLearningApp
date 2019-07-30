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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
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
        <img class="author_avatar" src="${pageContext.request.contextPath}/img/${author.pathToPhoto}"
             alt="avatar"/>
        <div class="author_description_and_stats">
            <div class="author_about">${author.about}</div>
            <div class="author_stats">
                <div class="author_rating"><fmt:message key='author.rating' bundle='${rb}'/> <ctg:write-rating rating="${author.avgMark}"/></div>
                <div class="author_students"><fmt:message key='author.students' bundle='${rb}'/> 352</div>
                <div class="courses_amount"><fmt:message key='author.courses' bundle='${rb}'/> ${courses.size()}</div>
            </div>
            <c:if test="${pageContext.request.getAttribute('is_author_marked_already') == null}">
                <a href="${pageContext.request.contextPath}/user/mark-author?author-id=${author.id}"><fmt:message key='global.mark_author' bundle='${rb}'/></a>
            </c:if>
        </div>
    </section>
    <section class="section_3">
        <div class="course_list_title"><fmt:message key='author.more_courses_from' bundle='${rb}'/> ${author.login}:</div>
        <c:forEach var="course" items="${courses}">
            <div class="about_course">
                <div class="block1">
                    <a href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                        <img class="course_avatar"
                             src="${pageContext.request.contextPath}/img${course.pathToPicture}"
                             alt="course avatar"/>
                    </a>
                </div>
                <div class="block2">
                    <div class="name_and_desk">
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
