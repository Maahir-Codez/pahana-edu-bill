<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu - Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">

</head>
<body>

<div class="register-container">
    <h2>Create an Account</h2>

    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) {
    %>
    <div class="error-message"><%= errorMessage %></div>
    <%
        }
    %>

    <form action="${pageContext.request.contextPath}/app/auth/register" method="post">
        <div class="form-group">
            <label for="fullName">Full Name</label>
            <input type="text" id="fullName" name="fullName" required>
        </div>
        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Create Account</button>
    </form>
    <div class="login-link">
        <p>Already have an account? <a href="${pageContext.request.contextPath}/app/auth/login">Login here</a></p>
    </div>
</div>

</body>
</html>