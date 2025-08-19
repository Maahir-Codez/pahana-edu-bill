<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New Customer</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}">
    <c:redirect url="/auth?action=login"/>
</c:if>

<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="form-container">
        <h2>Add New Customer</h2>
        <hr style="margin-bottom: 2rem;">

        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/customers?action=add" method="post">
            <div class="form-group">
                <label for="accountNumber">Account Number</label>
                <input type="text" id="accountNumber" name="accountNumber" value="<c:out value='${customer.accountNumber}'/>" required>
            </div>
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" value="<c:out value='${customer.fullName}'/>" required>
            </div>
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" value="<c:out value='${customer.email}'/>" required>
            </div>
            <div class="form-group">
                <label for="phoneNumber">Phone Number</label>
                <input type="text" id="phoneNumber" name="phoneNumber" value="<c:out value='${customer.phoneNumber}'/>">
            </div>
            <div class="form-group">
                <label for="address">Address</label>
                <input type="text" id="address" name="address" value="<c:out value='${customer.address}'/>">
            </div>
            <div class="form-group">
                <label for="city">City</label>
                <input type="text" id="city" name="city" value="<c:out value='${customer.city}'/>">
            </div>
            <div class="form-group">
                <label for="postalCode">Postal Code</label>
                <input type="text" id="postalCode" name="postalCode" value="<c:out value='${customer.postalCode}'/>">
            </div>

            <div class="button-group">
                <button type="submit" class="btn btn-primary">Save Customer</button>
                <a href="${pageContext.request.contextPath}/customers?action=list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

</body>
</html>