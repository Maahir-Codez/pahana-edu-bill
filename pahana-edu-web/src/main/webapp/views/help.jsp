<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help & Documentation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .help-section {
            background: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            margin-bottom: 2rem;
        }
        .help-section h2 {
            margin-top: 0;
            border-bottom: 2px solid #eee;
            padding-bottom: 0.5rem;
            margin-bottom: 1rem;
        }
        .help-section p {
            line-height: 1.6;
        }
        .help-section ol {
            padding-left: 20px;
        }
    </style>
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}"><c:redirect url="/auth?action=login"/></c:if>
<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>Help & System Guidelines</h1>
    </div>

    <div class="help-section">
        <h2>Dashboard</h2>
        <p>The dashboard is the main hub of the application. After logging in, you will land here. It provides quick links to all major sections of the system.</p>
    </div>

    <div class="help-section">
        <h2>Customer Management</h2>
        <p>This module allows you to manage all customer accounts.</p>
        <ol>
            <li><strong>View Customers:</strong> From the dashboard, click "View Customers" to see a list of all registered customers. Inactive customers will not be shown on this list.</li>
            <li><strong>Add a New Customer:</strong> On the customer list page, click the "Add New Customer" button. Fill in all required fields. The Account Number and Email must be unique.</li>
            <li><strong>Edit a Customer:</strong> In the customer list, click the "Edit" link next to any customer to update their details.</li>
            <li><strong>Deactivate a Customer:</strong> Click the "Delete" button to deactivate a customer's account. This is a soft delete; the customer's record is kept for historical purposes but they will be marked as inactive.</li>
        </ol>
    </div>

    <div class="help-section">
        <h2>Item Management</h2>
        <p>This module is for managing your store's inventory.</p>
        <ol>
            <li><strong>View Items:</strong> Click "Manage Items" on the dashboard. This shows a list of all items, their price, and current stock quantity.</li>
            <li><strong>Add a New Item:</strong> Use the "Add New Item" button. The SKU (Stock Keeping Unit) must be unique. Price and stock cannot be negative.</li>
            <li><strong>Edit an Item:</strong> Click "Edit" to update an item's details.</li>
            <li><strong>Delete an Item:</strong> Click "Delete" to permanently remove an item from the inventory. Be careful, as this action cannot be undone.</li>
        </ol>
    </div>

    <div class="help-section">
        <h2>Order & Billing Management</h2>
        <p>This module is used to create new orders for customers, which generates a bill.</p>
        <ol>
            <li><strong>Create an Order:</strong> From the dashboard, click "Create New Order".</li>
            <li>First, select a customer from the dropdown list.</li>
            <li>Next, select an item from the item dropdown, choose a quantity, and click "Add".</li>
            <li>The item will appear in the "Order Items" table. You can add multiple different items to the order.</li>
            <li>The system will automatically check for available stock and calculate the subtotal.</li>
            <li>Once you are finished adding items, click "Create Order". The order will be saved, and the stock levels for the items will be updated automatically.</li>
            <li><strong>View Order History / Print Bills:</strong> On the dashboard, click "View Order History". This page lists all past orders. Click "View Bill" on any order to see a detailed, printable invoice for that transaction.</li>
        </ol>
    </div>
</div>

</body>
</html>