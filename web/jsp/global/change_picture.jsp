<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/17/2019
  Time: 1:57 PM
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
        <%@include file="/css/global/change_picture.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="page_content_wrapper">
        <div class="page_title target_is_acc"><fmt:message key='change.change_account_avatar' bundle='${rb}'/>:</div>
        <div class="page_title target_is_course"><fmt:message key='change.change_course_image' bundle='${rb}'/>:</div>
        <c:if test="${pageContext.request.getParameter('operation-result').equals('true')}">
            <div class="change_successful"><fmt:message key="msg.change_img_request_was_sent" bundle="${rb}"/></div>
        </c:if>
        <div id="result_message"
             class="previous_operation_msg">${pageContext.request.getAttribute("previous_operation_message")}</div>
        <form method="post" enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/account/change-image">
            <div class="param">
                <label for="img_upload"><fmt:message key='change.upload_new_image' bundle='${rb}'/>: </label>
                <input type="file" id="img_upload" name="img_to_upload" placeholder='<fmt:message key='change.choose_file' bundle='${rb}'/>'>
            </div>
            <input class="target_is_acc" type="hidden" name="command_name" value="change_acc_img">
            <input class="target_is_course" type="hidden" name="command_name" value="change_course_img">
            <input class="target_is_course" type="hidden" name="course_id" value="${pageContext.request.getParameter('course-id')}">
            <input class="submit_btn" type="submit" value='<fmt:message key='btn.submit' bundle='${rb}'/>'>
        </form>
    </div>

</main>
<script>
    let courseId = ${pageContext.request.getParameter('course-id')};
</script>
<script>
    <%@include file="/js/change_picture.js" %>
</script>
</body>
</html>
