<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/17/2019
  Time: 6:57 PM
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
        <%@include file="/css/author/cash_out_page.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="page_title"><fmt:message key='payment.cash_out_to_card' bundle='${rb}'/></div>
    <div class="message">${pageContext.request.getAttribute("message")}</div>
    <div class="curr_balance"><span><fmt:message key='payment.current_balance' bundle='${rb}'/>:</span><span
            class="balance_value">${sessionScope.user.balance}$</span></div>
    <form method="post" action="${pageContext.request.contextPath}/author/cash-out">
        <div class="param0 param">
            <label for="currency"><fmt:message key='global.currency' bundle='${rb}'/>: </label>
            <select id="currency" name="currency" required>
                <option>USD</option>
            </select>
        </div>
        <div class="param1">
            <div class="param">
                <label for="amount-field"><fmt:message key='global.amount' bundle='${rb}'/>: </label>
                <input type="text" id="amount-field" name="amount" placeholder="0.00" required
                       pattern="[0-9]{1,8}(\.[0-9]{1,2})?">
            </div>
            <div class="pattern-info"><fmt:message key='course.price_format' bundle='${rb}'/></div>
        </div>
        <div class="param2">
            <div class="param">
                <label for="card-field"><fmt:message key='cbuy.card_number' bundle='${rb}'/>: </label>
                <input type="text" id="card-field" name="card" placeholder="xxxx xxxx xxxx xxxx" required
                       pattern="[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}">
            </div>
            <div class="pattern-info"><fmt:message key='cbuy.card_pattern' bundle='${rb}'/></div>
        </div>
        <div class="submitting">
            <input type="hidden" name="command_name" value="cash_out">
            <input class="submit_btn" type="submit" value='<fmt:message key='btn.process' bundle='${rb}'/>'>
        </div>
    </form>
    <a class="back_to_acc" href="${pageContext.request.contextPath}/account"><fmt:message key="btn.back_to_account" bundle="${rb}"/></a>
</main>
</body>
</html>
