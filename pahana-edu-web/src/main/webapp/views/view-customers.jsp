<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.pahanaedu.utils.DateTimeUtil" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Customers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}">
    <c:redirect url="/auth?action=login"/>
</c:if>

<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>Customer List</h1>
        <a href="${pageContext.request.contextPath}/customers?action=add" class="btn-primary" style="padding: 10px; border-radius: 5px;">Add New Customer</a>
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
                        <a href="${pageContext.request.contextPath}/customers?action=edit&id=${customer.id}" class="edit-link">Edit</a>
                        <form action="${pageContext.request.contextPath}/customers?action=delete" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to deactivate this customer?');">
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