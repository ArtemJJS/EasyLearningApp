<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/17/2019
  Time: 1:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <div class="page_title target_is_acc">Change account avatar:</div>
        <div class="page_title target_is_course">Change course image:</div>
        <div id="result_message"
             class="previous_operation_msg">${pageContext.request.getAttribute("previous_operation_message")}</div>
        <form method="post" enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/upload_img_servlet">
            <div class="param">
                <label for="img_upload">Upload new Image: </label>
                <input type="file" id="img_upload" name="img_to_upload" placeholder="choose file">
            </div>
            <input class="target_is_acc" type="hidden" name="command_name" value="change_acc_img">
            <input class="target_is_course" type="hidden" name="command_name" value="change_course_img">
            <input class="target_is_course" type="hidden" name="course_id" value="${pageContext.request.getParameter('course-id')}">
            <input class="submit_btn" type="submit" value="Submit">
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
