<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/22/2019
  Time: 11:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyLearning</title>
    <style>
        <%@include file="/css/user/mark_course.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<c:set var="course" value="${requestScope.requestedCourse}"/>
<c:set var="account" value="${sessionScope.user}"/>
<main>
    <div class="page_title">
        <div class="title_and_price">
            <div class="title">Mark next course:</div>
            <div class="course_price">Price: ${course.price}$</div>
        </div>
        <div class="course_name">${course.name}</div>
        <img class="course_img" src="${pageContext.request.contextPath}/${course.pathToPicture}" alt="Course image">
    </div>
    <form class="mark_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="params">
            <div class="param">
                <label for="mark_value_field_1">Mark: </label>
                <div class="mark_selectors">
                    <label for="mark_value_field_1">1</label>
                    <input id="mark_value_field_1" type="radio" name="mark_value" value="1">
                    <label for="mark_value_field_2">2</label>
                    <input id="mark_value_field_2" type="radio" name="mark_value" value="2">
                    <label for="mark_value_field_3">3</label>
                    <input id="mark_value_field_3" type="radio" name="mark_value" value="3">
                    <label for="mark_value_field_4">4</label>
                    <input id="mark_value_field_4" type="radio" name="mark_value" value="4">
                    <label for="mark_value_field_5">5</label>
                    <input id="mark_value_field_5" type="radio" name="mark_value" value="5">
                </div>
            </div>
            <div class="param">
                <div class="label_and_pattern">
                    <label for="mark_comment">Comment:</label>
                    <div class="pattern-info">max 1000 symbols</div>
                </div>
                <textarea id="mark_comment" name="comment" placeholder="Write your comment" rows="5"
                          cols="40"></textarea>
            </div>
        </div>
        <input type="hidden" name="command_name" value="mark_course"/>
        <input type="hidden" name="course_id" value="${pageContext.request.getParameter("course-id")}"/>
        <input class="submit_btn" type="submit" value="Submit">
    </form>
</main>
</body>
</html>
