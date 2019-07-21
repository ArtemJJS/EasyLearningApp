<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/21/2019
  Time: 3:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyLearning</title>
    <style>
        <%@include file="/css/global/search_result_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="courses_block">
        <c:set var="courses_available" value="${pageContext.request.getAttribute('courses_list')}"/>
        <c:choose>
            <c:when test="${courses_available.size()<1}">
                <c:out value="Nothing found on your request ${pageContext.request.getParameter('search_key')}... ((("/>
            </c:when>
            <c:otherwise>
                <div class="course_block_header">${courses_available.size()}
                    courses has been found on your request
                        ${pageContext.request.getParameter('search_key')}</div>
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
                            <a href="${pageContext.request.contextPath}/course/learn?course-id=${course.id}">Continue
                                learning</a>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="pagination">
        <form class="pages_form" method="get" action="${pageContext.request.contextPath}/search">
            <input type="hidden" name="command_name" value="search_course">
            <input type="hidden" name="search_key" value="${pageContext.request.getParameter('search_key')}">
            <input class="page_number" type="hidden" name="page" value="1">
            <input class="submit_btn" type="submit" value="<">
        </form>
        <form class="pages_form" method="get" action="${pageContext.request.contextPath}/search">
            <input type="hidden" name="command_name" value="search_course">
            <input type="hidden" name="search_key" value="${pageContext.request.getParameter('search_key')}">
            <input class="page_number" type="hidden" name="page" value="1">
            <input class="submit_btn" type="submit" value=">">
        </form>
    </div>
</main>
<script>let currPageNumber = ${pageContext.request.getParameter('page')};
</script>
<script>
    let hasMorePages =  ${pageContext.request.getAttribute('has_more_pages')};
</script>
<script>
    <%@include file="/js/search_page.js"%>
</script>
</body>
</html>
