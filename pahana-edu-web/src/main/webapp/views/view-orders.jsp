<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}"><c:redirect url="/app/auth/login"/></c:if>
<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>Order History</h1>
        <a href="${pageContext.request.contextPath}/app/orders/create" class="btn-primary" style="padding: 10px; border-radius: 5px;">Create New Order</a>
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
                <td><c:out value="${order.customerName}"/></td>
                <td><c:out value="${order.orderDate}"/></td>
                <td>$<c:out value="${order.totalAmount}"/></td>
                <td><span class="status-${order.status.toLowerCase()}"><c:out value="${order.status}"/></span></td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/app/orders/view?id=${order.id}" class="view-link">View Bill</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>