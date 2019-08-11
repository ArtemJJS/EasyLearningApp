<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/18/2019
  Time: 2:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>Easy Learning</title>
    <style>
        <%@include file="/css/admin/course_approval_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="message">${pageContext.request.getAttribute("message")}</div>
    <div class="courses_block">
        <c:set var="courses" value="${requestScope.courses_list}"/>
        <c:choose>
            <c:when test="${courses == null}">
                <div><fmt:message key='course.nothing_to_approve' bundle='${rb}'/></div>
            </c:when>
            <c:otherwise>
                <div class="course_block_header"><fmt:message key='course.waiting_of_approval' bundle='${rb}'/></div>
                <c:forEach var="course" items="${courses}">
                    <div class="about_course">
                        <div class="block1">
                            <a href="${pageContext.request.contextPath}/course?course-id=${course.id}">
                                <img class="course_avatar"
                                     src="${pageContext.request.contextPath}/img${course.pathToPicture}"
                                     alt='<fmt:message key='global.course_image' bundle='${rb}'/>'/>
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
                            <a href="${pageContext.request.contextPath}/course/learn?course-id=${course.id}">Check
                                course</a>
                        </div>
                    </div>
                    <div class="forms">
                        <form method="post" action="${pageContext.request.contextPath}/admin/course-approval">
                            <input type="hidden" name="course_id" value="${course.id}">
                            <input type="hidden" name="command_name" value="approve_course">
                            <input type="submit" value='<fmt:message key='btn.approve' bundle='${rb}'/>'>
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/admin/course-approval">
                            <input type="hidden" name="course_id" value="${course.id}">
                            <input type="hidden" name="command_name" value="decline_course_approval">
                            <input type="submit" value='<fmt:message key='btn.decline' bundle='${rb}'/>'>
                        </form>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</main>
<script>
    <%@include file="/js/localization_hidden.js"%>
</script>
</body>
</html>
