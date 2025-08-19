<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Pahana Edu - Register</title>

    <style>
        body { font-family: sans-serif; display: flex; justify-content: center; align-items: center; min-height: 100vh; background-color: #f4f4f4; padding: 20px 0; }
        .register-container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 350px; }
        h2 { text-align: center; color: #333; }
        .form-group { margin-bottom: 1rem; }
        label { display: block; margin-bottom: 0.5rem; color: #555; }
        input[type="text"], input[type="password"], input[type="email"] { width: 100%; padding: 0.5rem; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 0.75rem; background-color: #28a745; color: white; border: none; border-radius: 4px; font-size: 1rem; cursor: pointer; }
        button:hover { background-color: #218838; }
        .error-message { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 0.75rem; border-radius: 4px; margin-bottom: 1rem; text-align: center; }
        .login-link { text-align: center; margin-top: 1rem; }
        .login-link a { color: #007bff; text-decoration: none; }
    </style>
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

    <form action="${pageContext.request.contextPath}/auth?action=register" method="post">
        <div class="form-group">
            <label for="fullName">Full Name:</label>
            <input type="text" id="fullName" name="fullName" required>
        </div>
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Register</button>
    </form>
    <div class="login-link">
        <p>Already have an account? <a href="${pageContext.request.contextPath}/auth?action=login">Login here</a></p>
    </div>
</div>

</body>
</html>