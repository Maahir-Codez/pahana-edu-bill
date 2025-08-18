<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Pahana Edu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .welcome-header { margin-bottom: 2rem; }
        .welcome-header h1 { color: #333; }
        .welcome-header p { color: #666; font-size: 1.1rem; }
        .card-deck { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1.5rem; }
        .card { background: white; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); overflow: hidden; transition: transform 0.2s; }
        .card:hover { transform: translateY(-5px); }
        .card-body { padding: 1.5rem; }
        .card-title { font-size: 1.25rem; margin-top: 0; margin-bottom: 0.5rem; }
        .card-text { color: #666; margin-bottom: 1rem; }
        .card-link { display: inline-block; background-color: #007bff; color: white; padding: 0.6rem 1.2rem; border-radius: 5px; text-decoration: none; }
        .card-link:hover { background-color: #0056b3; }
    </style>
</head>
<body>

<c:if test="${empty sessionScope.loggedInUser}">
    <c:redirect url="/auth?action=login"/>
</c:if>

<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="welcome-header">
        <h1>Dashboard</h1>
        <p>Select an option below to manage the system.</p>
    </div>

    <div class="card-deck">
        <!-- Customer Management Card -->
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">Customer Management</h3>
                <p class="card-text">View, add, or edit customer accounts and their details.</p>
                <a href="${pageContext.request.contextPath}/customers?action=list" class="card-link">View Customers</a>
            </div>
        </div>

        <!-- Item Management Card -->
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">Item Management</h3>
                <p class="card-text">Manage the bookshop's inventory of items and their prices.</p>
                <a href="${pageContext.request.contextPath}/items?action=list" class="card-link">Manage Items</a>
            </div>
        </div>

        <!-- Billing Card -->
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">Billing & Invoicing</h3>
                <p class="card-text">Generate and print new bills for customers based on consumption.</p>
                <a href="${pageContext.request.contextPath}/orders?action=create" class="card-link">Create New Order</a>
            </div>
        </div>

        <!-- Help Card -->
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">Help Section</h3>
                <p class="card-text">Get information and guidelines on how to use the system.</p>
                <a href="#" class="card-link">View Help</a> <%-- Link to be implemented later --%>
            </div>
        </div>
    </div>
</div>

</body>
</html>