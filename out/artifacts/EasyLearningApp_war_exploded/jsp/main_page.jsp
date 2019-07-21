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
<html>
<head>
    <title>EasyLearning</title>
    <%--    <c:import url="/css/main_page.css" >--%>
    <style>
        <%@include  file="/css/main_page.css" %>
    </style>
</head>
<body>
<%@include file="header.jsp" %>
<main>
    <div class="banner">Learn EASY. <br>EVERYTIME. EVERYWHERE.</div>
    <div class="greeting">Welcome back, ${sessionScope.user.name}! Are you ready to study?</div>
    <div class="courses_block">
        <c:set var="courses_available" value="${sessionScope.coursesAvailable}"/>
        <c:choose>
            <c:when test="${courses_available == null}">
                <c:out value="You do not have available courses... ((("/>
            </c:when>
            <c:otherwise>
                <div class="course_block_header">Courses, that you have purchased already:</div>
                <c:forEach var="course" items="${courses_available}">
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
                            <ctg:course-options role="${sessionScope.role}" course="${course}"
                                                contextPath="${pageContext.request.contextPath}"
                                                coursesAvailable="${sessionScope.coursesAvailable}"/>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="courses_block">
        <c:set var="courses_recommended"
               value="${sessionScope.get('coursesRecommended')}"/>
        <div class="course_block_header"> Courses, That are recommended for you:</div>
        <c:forEach var="course" items="${courses_available}">
            <div class="about_course">
                <div class="block1">
                    <a href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                        <img class="course_avatar"
                             src="${pageContext.request.contextPath}${course.pathToPicture}"
                             alt="course avatar"/>
                    </a>
                </div>
                <div class="block2">
                    <div class="course_name">
                        <a href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                                ${course.name}
                        </a>
                    </div>
                    <div class="course_desc">${course.description}</div>
                </div>
            </div>
        </c:forEach>
    </div>
</main>

</body>

</html>
