<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/18/2019
  Time: 10:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>Easy Learning</title>
    <style>
        <%@include file="/css/author/author_my_courses_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="courses_block">
        <c:set var="courses_available" value="${sessionScope.coursesAvailable}"/>
        <c:choose>
            <c:when test="${courses_available == null || courses_available.size() < 1}">
                <div><fmt:message key='course.you_still_have_no_courses' bundle='${rb}'/></div>
            </c:when>
            <c:otherwise>
                <div class="course_block_header"><fmt:message key='course.your_courses' bundle='${rb}'/>:</div>
                <c:forEach var="course" items="${courses_available}">
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
                                <div class="course_name_and_state">
                                    <a href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                                            ${course.name}
                                    </a>
                                    <div class="course_state"><ctg:course-state-writer state="${course.state}"/></div>
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
</main>
</body>
</html>
