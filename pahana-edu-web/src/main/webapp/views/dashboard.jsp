<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Pahana Edu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>

<c:if test="${empty sessionScope.loggedInUser}">
    <c:redirect url="/app/auth/login"/>
</c:if>

<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="welcome-header">
        <h1>Dashboard</h1>
        <p>Welcome to Pahana Edu Management System. Select an option below to manage the system efficiently.</p>
    </div>

    <div class="card-deck">
        <!-- Customer Management Card -->
        <div class="card">
            <div class="card-body">
                <div class="card-icon">👥</div>
                <h3 class="card-title">Customer Management</h3>
                <p class="card-text">View, add, or edit customer accounts and their details. Manage customer information and track their order history.</p>
                <a href="${pageContext.request.contextPath}/app/customers/list" class="card-link" style="padding: 10px; border-radius: 5px;">View Customers</a>
            </div>
        </div>

        <!-- Item Management Card -->
        <div class="card">
            <div class="card-body">
                <div class="card-icon">📚</div>
                <h3 class="card-title">Item Management</h3>
                <p class="card-text">Manage the bookshop's inventory of items and their prices. Add new items, update existing ones, and track stock levels.</p>
                <a href="${pageContext.request.contextPath}/app/items/list" class="card-link" style="padding: 10px; border-radius: 5px;">Manage Items</a>
            </div>
        </div>

        <!-- Billing Card -->
        <div class="card">
            <div class="card-body">
                <div class="card-icon">🧾</div>
                <h3 class="card-title">Orders & Billing</h3>
                <p class="card-text">Create new orders for customers or view the history of past orders and bills. Generate invoices and track payments.</p>
                <div class="card-link-group">
                    <a href="${pageContext.request.contextPath}/app/orders/create" class="card-link" style="padding: 10px; border-radius: 5px;">Create New Order</a>
                    <a href="${pageContext.request.contextPath}/app/orders/list" class="card-link secondary" style="padding: 10px; border-radius: 5px;">View Order History</a>
                </div>
            </div>
        </div>

        <!-- Help Card -->
        <div class="card">
            <div class="card-body">
                <div class="card-icon">❓</div>
                <h3 class="card-title">Help & Support</h3>
                <p class="card-text">Get information and guidelines on how to use the system. Find answers to common questions and troubleshooting tips.</p>
                <a href="${pageContext.request.contextPath}/app/help" class="card-link" style="padding: 10px; border-radius: 5px;">View Help</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>