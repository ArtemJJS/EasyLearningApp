<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/16/2019
  Time: 1:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="text_resources" var="rb"/>
<html>
<head>
    <title>Title</title>
    <style>
        <%@include file="/css/global/account.css"%>
    </style>
</head>
<body>
<%@include file="/jsp/header.jsp" %>
<main>
    <c:set var="user" value="${sessionScope.user}"/>
    <c:set var="curr_role" value="${sessionScope.role}"/>
    <div class="img_and_links">
        <img src="${pageContext.request.contextPath}/img/${user.pathToPhoto}"
             alt='<fmt:message key='global.avatar' bundle='${rb}'/>'>
        <div class="role">${sessionScope.role}</div>
        <div class="balance"><fmt:message key='global.balance' bundle='${rb}'/>: ${user.balance}$</div>
        <div class="links">
            <div class="link"><a href="${pageContext.request.contextPath}/account/change-picture"><fmt:message
                    key='acc.change_photo' bundle='${rb}'/></a></div>
            <div class="link"><a href="${pageContext.request.contextPath}/account/change-password"><fmt:message
                    key='acc.change_password' bundle='${rb}'/></a></div>
            <div class="link"><a href="${pageContext.request.contextPath}/account/payments?page=0"><fmt:message
                    key='acc.payments' bundle='${rb}'/></a></div>
            <c:choose>
                <c:when test="${curr_role.toString() == 'USER'}">
                    <div class="link"><a href="${pageContext.request.contextPath}/user/purchased-courses"><fmt:message
                            key='acc.purchased_courses' bundle='${rb}'/></a></div>
                    <div class="link"><a href="${pageContext.request.contextPath}/user/deposit-by-card"><fmt:message
                            key='acc.deposit' bundle='${rb}'/></a></div>
                </c:when>
                <c:when test="${curr_role.toString() == 'AUTHOR'}">
                    <div class="link"><a href="${pageContext.request.contextPath}/author/add-new-course"><fmt:message
                            key='acc.add_new_course' bundle='${rb}'/></a></div>
                    <div class="link"><a href="${pageContext.request.contextPath}/author/my-courses"><fmt:message
                            key='acc.my_courses' bundle='${rb}'/></a></div>
                    <div class="link"><a href="${pageContext.request.contextPath}/author/cash-out-on-card"><fmt:message
                            key='acc.cashout' bundle='${rb}'/></a></div>
                </c:when>
                <c:when test="${curr_role.toString() == 'ADMIN'}">
                    <div class="link"><a href="${pageContext.request.contextPath}/admin/course-approve"><fmt:message
                            key='acc.courses_on_review' bundle='${rb}'/></a></div>
                    <div class="link"><a
                            href="${pageContext.request.contextPath}/admin/approve-course-image"><fmt:message
                            key='acc.course_images_on_review' bundle='${rb}'/></a></div>
                    <div class="link"><a
                            href="${pageContext.request.contextPath}/admin/approve-account-avatar"><fmt:message
                            key='acc.account_images_on_review' bundle='${rb}'/></a></div>
                </c:when>
            </c:choose>
        </div>
    </div>
    <div class="update_section">
        <div class="header1"><fmt:message key='acc.profile' bundle='${rb}'/></div>
        <div class="previous_operation_message">
           <c:choose>
               <c:when test="${pageContext.request.getParameter('command-result').equals('true')}">
                   <div class="operation_successful"><fmt:message key="msg.personal_info_updated" bundle="${rb}"/></div>
               </c:when>
               <c:when test="${pageContext.request.getParameter('command-result').equals('false')}">
                   <div class="operation_fail"><fmt:message key="msg.incorrect_data" bundle="${rb}"/></div>
               </c:when>
           </c:choose>
        </div>
        <form id="reg_form" method="post" action="${pageContext.request.contextPath}/basic_servlet">
            <div class="param2 param">
                <label for="name-field"><fmt:message key='global.name' bundle='${rb}'/>: </label>
                <div class="input_and_pattern">
                    <input type="text" id="name-field" name="name" value="${user.name}" required
                           pattern="[A-Za-zА-я]{2,30}">
                    <p class="field_desc"><fmt:message key='registr.name_pattern' bundle='${rb}'/></p>
                </div>
            </div>
            <div class="param3 param">
                <label for="surname-field"><fmt:message key='global.surname' bundle='${rb}'/>: </label>
                <div class="input_and_pattern">
                    <input type="text" id="surname-field" name="surname" value="${user.surname}" required
                           pattern="[A-Za-zА-я]{2,30}">
                    <p class="field_desc"><fmt:message key='registr.surname_pattern' bundle='${rb}'/></p>
                </div>
            </div>
            <div class="param4 param">
                <label for="email-field"><fmt:message key='global.email' bundle='${rb}'/>: </label>
                <input type="email" id="email-field" name="email" value="${user.email}" required>
            </div>
            <div class="param5  param">
                <label for="phonenumber-field"><fmt:message key='global.phone' bundle='${rb}'/>: </label>
                <div class="input_and_pattern">
                    <input type="text" id="phonenumber-field" name="phonenumber" value="${user.phoneNumber}"
                           pattern="\+[0-9]{2,19}">
                    <p class="field_desc"><fmt:message key='registr.phone_pattern' bundle='${rb}'/></p>
                </div>
            </div>
            <div class="param6 param">
                <p><fmt:message key='global.about' bundle='${rb}'/>:</p>
                <textarea class="replace_br"  id="about-field" name="about" cols="38" rows="7"
                          maxlength="500">${user.about}</textarea>
            </div>
            <div>
                <input type="hidden" name="command_name" value="edit_user_info">
                <input class="submit_btn" type="submit" value=<fmt:message key='btn.save_changes' bundle='${rb}'/>>
            </div>
        </form>
    </div>
</main>
<script>
    <%@include file="/js/replace_br.js"%>
</script>
</body>
</html>
