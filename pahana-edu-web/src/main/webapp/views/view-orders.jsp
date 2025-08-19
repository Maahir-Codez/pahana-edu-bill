<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.pahanaedu.utils.DateTimeUtil" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}"><c:redirect url="/auth?action=login"/></c:if>
<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>Order History</h1>
        <a href="${pageContext.request.contextPath}/orders?action=create" class="btn-primary" style="padding: 10px; border-radius: 5px;">Create New Order</a>
    </div>

    <table>
        <thead>
        <tr>
            <th>Order ID</th>
            <th>Customer Name</th>
            <th>Order Date</th>
            <th>Total Amount</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orderList}">
            <tr>
                <td>ORD-<c:out value="${order.id}"/></td>
                <td><c:out value="${customerMap[order.customerId]}"/></td>
                <td>
                    <% com.pahanaedu.models.Order currentOrder = (com.pahanaedu.models.Order) pageContext.findAttribute("order"); %>
                    <%= DateTimeUtil.formatLocalDateTime(currentOrder.getOrderDate()) %>
                </td>
                <td>$<c:out value="${order.totalAmount}"/></td>
                <td><span class="status-${order.status.name().toLowerCase()}"><c:out value="${order.status}"/></span></td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/orders?action=view&id=${order.id}" class="view-link">View Bill</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>