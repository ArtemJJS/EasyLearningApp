<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/16/2019
  Time: 11:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>Easy Learning</title>
    <style>
        <%@include file="/css/global/payment_history.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <div class="page_title"><fmt:message key='payment.your_payment_history' bundle='${rb}'/>:</div>
    <c:set var="payments" value="${pageContext.request.getAttribute('payments')}"/>
    <div class="payment_list_wrapper">
        <c:forEach var="payment" items="${payments}">
            <div class="payment-view">
                <div class="payment_date"><ctg:millisec-to-time millisecAmount="${payment.paymentDate}"/></div>
                <div class="payment_description">${payment.description}</div>
                <div class="amount_and_currency">
                    <div class="payment_amount">${payment.amount}</div>
                    <div class="payment_currency"><ctg:currency-marker currencyId="${payment.currencyId}"/></div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="pagination">
        <form class="pages_form" method="get" action="${pageContext.request.contextPath}/basic_servlet">
            <input type="hidden" name="command_name" value="next_payment_page">
            <input type="hidden" name="search_key" value="${pageContext.request.getParameter('search_key')}">
            <input class="page_number" type="hidden" name="page" value="1">
            <input class="submit_btn" type="submit" value="<">
        </form>
        <div class="curr_page">${pageContext.request.getParameter("page") + 1}</div>
        <form class="pages_form" method="get" action="${pageContext.request.contextPath}/basic_servlet">
            <input type="hidden" name="command_name" value="next_payment_page">
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
    <%@include file="/js/pagination.js"%>
</script>
</body>
</html>
