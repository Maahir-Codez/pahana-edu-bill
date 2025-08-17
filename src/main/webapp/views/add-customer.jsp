<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New Customer</title>
    <%-- Reusing dashboard styles for consistency --%>
    <style>
        body { font-family: sans-serif; background-color: #f8f9fa; margin: 0; }
        .navbar { background-color: #343a40; padding: 1rem 2rem; color: white; display: flex; justify-content: space-between; align-items: center; }
        .navbar a { color: white; text-decoration: none; }
        .navbar .logout-btn { background-color: #dc3545; padding: 0.5rem 1rem; border-radius: 5px; }
        .container { padding: 2rem; max-width: 800px; margin: auto; }
        .form-container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 1.5rem; }
        label { display: block; margin-bottom: 0.5rem; font-weight: bold; }
        input[type="text"], input[type="email"] { width: 100%; padding: 0.75rem; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .button-group { display: flex; gap: 1rem; margin-top: 2rem; }
        .btn { padding: 0.75rem 1.5rem; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; text-align: center; }
        .btn-primary { background-color: #007bff; color: white; }
        .btn-secondary { background-color: #6c757d; color: white; }
        .error-message { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 1rem; border-radius: 4px; margin-bottom: 1rem; }
    </style>
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}">
    <c:redirect url="/auth/login"/>
</c:if>

<div class="navbar">
    <a href="${pageContext.request.contextPath}/dashboard">Pahana Edu Billing System</a>
    <a href="${pageContext.request.contextPath}/auth/logout" class="logout-btn">Logout</a>
</div>

<div class="container">
    <div class="form-container">
        <h2>Add New Customer</h2>
        <hr style="margin-bottom: 2rem;">

        <%-- Display any error messages from the controller --%>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/customers/add" method="post">
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
                <a href="${pageContext.request.contextPath}/customers/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

</body>
</html>