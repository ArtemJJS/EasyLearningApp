<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 8/11/2019
  Time: 3:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Easy Learning</title>
    <style>
        <%@include file="/css/user/edit_author_comment_page.css"%> >
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="page_title"><fmt:message key='global.mark_author' bundle='${rb}'/>:</div>
    <form class="mark_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="params">
            <div class="param">
                <div class="label_and_pattern">
                    <label for="mark_comment"><fmt:message key='mark.comment' bundle='${rb}'/>:</label>
                    <div class="pattern-info"><fmt:message key='mark.max_700_symbols' bundle='${rb}'/></div>
                </div>
                <textarea id="mark_comment" name="mark_comment" rows="5" cols="45"
                          maxlength="700">${pageContext.request.getAttribute("mark_comment")}</textarea>
            </div>
        </div>
        <input type="hidden" name="command_name" value="edit_author_comment"/>
        <input type="hidden" name="mark_id" value="${pageContext.request.getParameter("mark_id")}">
        <input type="hidden" name="requested_author_login" value="${pageContext.request.getParameter("author_login")}"/>
        <input class="submit_btn" type="submit" value='<fmt:message key='btn.submit' bundle='${rb}'/>'>
    </form>
</main>
</body>
</html>
