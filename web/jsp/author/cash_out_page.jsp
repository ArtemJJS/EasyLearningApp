<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/17/2019
  Time: 6:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <div class="page_title">CASH OUT TO CARD</div>
    <div class="curr_balance"><span>Current balance:</span><span
            class="balance_value">${sessionScope.user.balance}$</span></div>
    <div class="privious_operation_message">${requestScope.previous_operation_message}</div>
    <form method="post" action="${pageContext.request.contextPath}/basic_servlet">
        <div class="param0 param">
            <label for="currency">Currensy: </label>
            <select id="currency" name="currency" required>
                <option>USD</option>
                <option>FIX ME</option>
            </select>
        </div>
        <div class="param1">
            <div class="param">
                <label for="amount-field">Amount: </label>
                <input type="text" id="amount-field" name="amount" placeholder="0.00" required
                       pattern="[0-9]{1,8}(\.[0-9]{1,2})?">
            </div>
            <div class="pattern-info">Format: 12345678.12</div>
        </div>
        <div class="param2">
            <div class="param">
                <label for="card-field">Card number: </label>
                <input type="text" id="card-field" name="card" placeholder="xxxx xxxx xxxx xxxx" required
                       pattern="[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}">
            </div>
            <div class="pattern-info">Card number: XXXX XXXX XXXX XXXX</div>
        </div>
        <div class="submitting">
            <input type="hidden" name="command_name" value="cash_out">
            <input class="submit_btn" type="submit" value="Process">
        </div>
    </form>
</main>
</body>
</html>
