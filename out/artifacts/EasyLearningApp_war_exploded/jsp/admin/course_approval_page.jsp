<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/18/2019
  Time: 2:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyLearning</title>
    <style>
        <%@include file="/css/admin/course_approval_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="courses_block">
        <c:set var="courses" value="${requestScope.courses_need_approval}"/>
        <c:choose>
            <c:when test="${courses == null}">
                <c:out value="Nothing to approve..."/>
            </c:when>
            <c:otherwise>
                <div class="course_block_header">Courses, that are waiting of approval</div>
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
                            <a href="${pageContext.request.contextPath}/course/learn?course-id=${course.id}">Check
                                course</a>
                        </div>
                    </div>
                    <div class="forms">
                        <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
                            <input type="hidden" name="course_id" value="${course.id}">
                            <input type="hidden" name="command_name" value="approve_course">
                            <input type="submit" value="Approve!">
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
                            <input type="hidden" name="course_id" value="${course.id}">
                            <input type="hidden" name="command_name" value="decline_course_approval">
                            <input type="submit" value="Decline">
                        </form>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>
