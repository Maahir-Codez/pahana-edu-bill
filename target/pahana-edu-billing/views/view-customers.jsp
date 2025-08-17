<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.pahanaedu.utils.DateTimeUtil" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Customers</title>
    <%-- Reusing dashboard styles for consistency --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"> <%-- We should create a shared CSS file --%>
    <style>
        /* (You can copy styles from dashboard.jsp here or link a shared stylesheet) */
        body { font-family: sans-serif; background-color: #f8f9fa; margin: 0; }
        .navbar { background-color: #343a40; padding: 1rem 2rem; color: white; display: flex; justify-content: space-between; align-items: center; }
        .navbar a { color: white; text-decoration: none; font-size: 1.1rem; }
        .navbar .logout-btn { background-color: #dc3545; padding: 0.5rem 1rem; border-radius: 5px; }
        .container { padding: 2rem; max-width: 1200px; margin: auto;}
        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; }
        .btn-primary { background-color: #007bff; color: white; padding: 0.6rem 1.2rem; border-radius: 5px; text-decoration: none; }
        table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        th, td { padding: 1rem; text-align: left; border-bottom: 1px solid #dee2e6; }
        th { background-color: #f2f2f2; }
        .actions a { margin-right: 0.5rem; text-decoration: none; }
        .actions .edit-link { color: #ffc107; }
        .actions .delete-link { color: #dc3545; }
    </style>
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}">
    <c:redirect url="/auth/login"/>
</c:if>

<div class="navbar">
    <a href="${pageContext.request.contextPath}/dashboard">Pahana Edu Billing System</a>
    <div>
        <span>Welcome, <c:out value="${sessionScope.loggedInUser.fullName}"/>!</span>
        <a href="${pageContext.request.contextPath}/auth/logout" class="logout-btn">Logout</a>
    </div>
</div>

<div class="container">
    <div class="page-header">
        <h1>Customer List</h1>
        <a href="${pageContext.request.contextPath}/customers/add" class="btn-primary">Add New Customer</a>
    </div>

    <table>
        <thead>
        <tr>
            <th>Account No.</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Phone Number</th>
            <th>Registered Date</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="customer" items="${customerList}">
            <c:if test="${customer.active}">
                <tr>
                    <td><c:out value="${customer.accountNumber}"/></td>
                    <td><c:out value="${customer.fullName}"/></td>
                    <td><c:out value="${customer.email}"/></td>
                    <td><c:out value="${customer.phoneNumber}"/></td>
                    <td>
                        <%= DateTimeUtil.formatLocalDateTime(((com.pahanaedu.models.Customer) pageContext.findAttribute("customer")).getDateRegistered()) %>
                    </td>
                    <td>
                        <c:if test="${customer.active}">
                            <span style="color: green;">Active</span>
                        </c:if>
                        <c:if test="${not customer.active}">
                            <span style="color: red;">Inactive</span>
                        </c:if>
                    </td>
                    <td class="actions">
                        <a href="${pageContext.request.contextPath}/customers/edit?id=${customer.id}" class="edit-link">Edit</a>
                        <form action="${pageContext.request.contextPath}/customers/delete" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to deactivate this customer?');">
                            <input type="hidden" name="id" value="${customer.id}">
                            <button type="submit" class="delete-link" style="background:none; border:none; padding:0; color:#dc3545; cursor:pointer; font-size: 1em;">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        <c:if test="${empty customerList}">
            <tr>
                <td colspan="7" style="text-align: center;">No customers found.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

</body>
</html>