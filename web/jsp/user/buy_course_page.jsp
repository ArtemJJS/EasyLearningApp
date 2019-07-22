<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/22/2019
  Time: 2:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyLearning</title>
    <style>
        <%@include file="/css/user/buy_course_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<c:set var="course" value="${requestScope.requestedCourse}"/>
<c:set var="account" value="${sessionScope.user}"/>
<main>
    <div class="page_title">
        <div class="title_and_price">
            <div class="title">You are buying next course:</div>
            <div class="course_price">Price: ${course.price}$</div>
        </div>
        <div class="course_name">${course.name}</div>
        <img class="course_img" src="${pageContext.request.contextPath}/${course.pathToPicture}" alt="Course image">
    </div>
    <c:if test="${pageContext.request.getParameter('result') != null}">
        <div class="previous_operation_message">Not enough funds!</div>
    </c:if>
    <div class="buy_block">
        <div class="pay-card">
            <form class="card_form payment_form" method="post"
                  action="${pageContext.request.contextPath}/basic_servlet">
                <div class="form_title">Pay by card:</div>
                <div class="form_subtitle">
                    <div class="amount_title">Payment amount:</div>
                    <div class="form_price">${course.price}$</div>
                </div>
                <div class="param">
                    <label for="card-field">Card number: </label>
                    <input type="text" id="card-field" name="card" placeholder="xxxx xxxx xxxx xxxx" required
                           pattern="[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}">
                </div>
                <input type="hidden" name="command_name" value="buy_with_card">
                <input type="hidden" name="course_id" value="${course.id}">
                <input class="submit_btn" type="submit" value="Submit">
            </form>
        </div>
        <div class="pay_balance">
            <form class="balance_form payment_form" method="post"
                  action="${pageContext.request.contextPath}/basic_servlet">
                <div class="form_title">Pay from balance:</div>
                <div class="form_subtitle">
                    <div class="amount_title">Payment amount:</div>
                    <div class="form_price">${course.price}$</div>
                </div>
                <div class="form_subtitle">
                    <div class="amount_title">Balance:</div>
                    <div class="form_price acc_balance">${account.balance}$</div>
                </div>
                <input type="hidden" name="command_name" value="buy_from_balance">
                <input type="hidden" name="course_id" value="${course.id}">
                <input class="submit_btn" type="submit" value="Submit">
            </form>
        </div>
    </div>
</main>
</body>
</html>
