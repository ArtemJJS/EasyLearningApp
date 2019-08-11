<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/22/2019
  Time: 2:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>Easy Learning</title>
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
            <div class="title"><fmt:message key='cbuy.you_are_purchasing_next_course' bundle='${rb}'/></div>
            <div class="course_price"><fmt:message key='global.price' bundle='${rb}'/>: ${course.price}$</div>
        </div>
        <div class="course_name">${course.name}</div>
        <img class="course_img" src="${pageContext.request.contextPath}/img/${course.pathToPicture}" alt='<fmt:message key='global.course_image' bundle='${rb}'/>'>
    </div>
    <c:if test="${pageContext.request.getParameter('result') != null}">
        <div class="previous_operation_message"><fmt:message key='msg.problems_purchasing_course' bundle='${rb}'/></div>
    </c:if>
    <div class="buy_block">
        <div class="pay-card">
            <form class="card_form payment_form" method="post"
                  action="${pageContext.request.contextPath}/basic_servlet">
                <div class="form_title"><fmt:message key='cbuy.pay_by_card' bundle='${rb}'/>:</div>
                <div class="form_subtitle">
                    <div class="amount_title"><fmt:message key='cbuy.payment_amount' bundle='${rb}'/>:</div>
                    <div class="form_price">${course.price}$</div>
                </div>
                <div class="param">
                    <label for="card-field"><fmt:message key='cbuy.card_number' bundle='${rb}'/>: </label>
                    <input type="text" id="card-field" name="card" placeholder="xxxx xxxx xxxx xxxx" required
                           pattern="[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}">
                </div>
                <input type="hidden" name="command_name" value="buy_with_card">
                <input type="hidden" name="course_id" value="${course.id}">
                <input class="submit_btn" type="submit" value='<fmt:message key='btn.submit' bundle='${rb}'/>'>
            </form>
        </div>
        <div class="pay_balance">
            <form class="balance_form payment_form" method="post"
                  action="${pageContext.request.contextPath}/basic_servlet">
                <div class="form_title"><fmt:message key='cbuy.pay_from_balance' bundle='${rb}'/>:</div>
                <div class="form_subtitle">
                    <div class="amount_title"><fmt:message key='cbuy.payment_amount' bundle='${rb}'/>:</div>
                    <div class="form_price">${course.price}$</div>
                </div>
                <div class="form_subtitle">
                    <div class="amount_title"><fmt:message key='global.balance' bundle='${rb}'/>:</div>
                    <div class="form_price acc_balance">${account.balance}$</div>
                </div>
                <div class="insufficient_funds" style="display: none;"><fmt:message key='cbuy.insufficient_funds' bundle='${rb}'/></div>
                <input type="hidden" name="command_name" value="buy_from_balance">
                <input type="hidden" name="course_id" value="${course.id}">
                <input class="submit_btn btn_balance_purchase" type="submit" value='<fmt:message key='btn.submit' bundle='${rb}'/>'>
            </form>
        </div>
    </div>
</main>
<script>let balance = ${account.balance};</script>
<script>let price = ${course.price};</script>
<script>
    <%@include file="/js/buy_course_page.js"%>
</script>
<script>
    <%@include file="/js/localization_hidden.js"%>
</script>
</body>
</html>
