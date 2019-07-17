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
        <%@include file="/css/global/change_acc_photo_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="page_content_wrapper">
        <div class="page_title">Change account image:</div>
        <div id="result_message"
             class="previous_operation_msg">${pageContext.request.getAttribute("previous_operation_message")}</div>
        <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/upload_img_servlet">
            <div class="param">
                <label for="img_upload">Upload new Image: </label>
                <input type="file" id="img_upload" name="img_to_upload" placeholder="choose file">
                <%--                <p class="field_desc">Min 5, max 50 symbols.</p>--%>
            </div>
            <input type="hidden" name="command_name" value="change_img">
            <input class="submit_btn" type="submit" value="Submit">
        </form>
    </div>
</main>
</body>
</html>
