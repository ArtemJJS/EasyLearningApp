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
    <title>Easy Learning</title>
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
                <c:if test="${pageContext.request.getAttribute('is_author_marked_already') == null
                        && sessionScope.role.toString().equalsIgnoreCase('user')}">
                    <div class="links">
                        <a href="${pageContext.request.contextPath}/user/mark-author?author-id=${author.id}"><fmt:message
                                key='global.mark_author' bundle='${rb}'/></a>
                    </div>
                </c:if>
                <div class="stat_unit author_rating"><fmt:message key='author.rating' bundle='${rb}'/> <ctg:write-rating
                        rating="${author.avgMark}"/></div>
                <div class="stat_unit courses_amount"><fmt:message key='author.courses'
                                                                   bundle='${rb}'/> ${courses.size()}</div>
    </section>
    <section class="section_3">
        <div class="course_list_title"><fmt:message key='author.more_courses_from' bundle='${rb}'/> ${author.login}:
        </div>
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

    <section class="section_5">
        <div class="comments_title"><fmt:message key='author.feedback_from_users_on_author' bundle='${rb}'/>:</div>
        <c:set var="marks" value="${pageContext.request.getAttribute('author_marks')}"/>
        <c:forEach var="mark" items="${marks}">
            <div class="single_mark">
                <div class="mark_content">
                    <div class="writer_login">${mark.accLogin}</div>
                    <div class="value"><fmt:message key="mark.mark" bundle="${rb}"/>: ${mark.markValue}</div>
                    <div class="comment">${mark.comment}</div>
                    <div class="date"><ctg:millisec-to-time millisecAmount="${mark.markDate}"/></div>
                    <c:choose>
                        <c:when test="${sessionScope.role.toString().equalsIgnoreCase('user')
                        && sessionScope.coursesAvailable.contains(curr_course)}">
                            <div class="user_comment_actions">
                                <form method="post" action="${pageContext.request.contextPath}/edit_author_comment">
                                    <input type="hidden" name="mark_id" value="${mark.id}">
                                    <input type="hidden" name="command_name" value="go_edit_author_comment">
                                    <input class="mark_action_submit_btn" type="submit"
                                           value='<fmt:message key="btn.delete_comment" bundle="${rb}"/>'>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
                                    <input type="hidden" name="mark_id" value="${mark.id}">
                                    <input type="hidden" name="course-id" value="${curr_course.id}">
                                    <input type="hidden" name="command_name" value="go_edit_course_comment">
                                    <input class="mark_action_submit_btn" type="submit"
                                           value='<fmt:message key="btn.edit_comment" bundle="${rb}"/>'>
                                </form>
                            </div>
                        </c:when>
                        <c:when test="${sessionScope.role.toString().equalsIgnoreCase('admin')}">
                            <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
                                <input type="hidden" name="mark_id" value="${mark.id}">
                                <input type="hidden" name="command_name" value="delete_author_comment">
                                <input class="mark_action_submit_btn" type="submit"
                                       value='<fmt:message key="btn.delete_comment" bundle="${rb}"/>'>
                            </form>
                        </c:when>
                    </c:choose>
                    <c:if test="${sessionScope.role.toString().equalsIgnoreCase('admin')}">
                        <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
                            <input type="hidden" name="mark_id" value="${mark.id}">
                            <input type="hidden" name="command_name" value="delete_author_comment">
                            <input class="mark_action_submit_btn" type="submit"
                                   value='<fmt:message key="btn.delete_comment" bundle="${rb}"/>'>
                        </form>
                    </c:if>
                </div>
                <img class="writer_avatar" src="${pageContext.request.contextPath}/img/${mark.accPathToPhoto}"
                     alt="avatar"/>
            </div>
        </c:forEach>
    </section>
</main>
</body>
</html>
