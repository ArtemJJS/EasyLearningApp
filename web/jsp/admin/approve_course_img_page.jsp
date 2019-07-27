<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/20/2019
  Time: 7:09 PM
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
        <%@include file="/css/admin/approve_course_img_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp"%>
<main>
    <c:set var="courses" value="${pageContext.request.getAttribute('courses_list')}"/>
    <div class="page_title"><fmt:message key='course.change_course_img_requests' bundle='${rb}'/>:</div>
    <c:forEach var="course" items="${courses}">
        <div class="course_wrapper">
            <div class="info_block">
                <div class="avatar">
                    <div class="img_detail"><fmt:message key='course.new_image' bundle='${rb}'/>:</div>
                    <img src="${pageContext.request.contextPath}/img/${course.updatePhotoPath}" alt="">
                </div>
                <div class="course_info">
                    <div class="course_login">${course.name}</div>
                    <div class="course_desc">${course.description}</div>
                </div>
                <div class="avatar">
                    <div class="img_detail"><fmt:message key='course.current_image' bundle='${rb}'/>:</div>
                    <img src="${pageContext.request.contextPath}/img/${course.pathToPicture}">
                </div>
            </div>
            <div class="forms">
                <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
                    <input type="hidden" name="course_name" value="${course.name}">
                    <input type="hidden" name="command_name" value="approve_course_img_change">
                    <input type="submit" value='<fmt:message key='btn.approve' bundle='${rb}'/>'>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
                    <input type="hidden" name="course_name" value="${course.name}">
                    <input type="hidden" name="command_name" value="decline_course_img_change">
                    <input type="submit" value='<fmt:message key='btn.decline' bundle='${rb}'/>'>
                </form>
            </div>
        </div>
    </c:forEach>
</main>
</body>
</html>
