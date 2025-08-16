<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu - Login</title>
    <style>
        body { font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f4f4f4; }
        .login-container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 300px; }
        h2 { text-align: center; color: #333; }
        .form-group { margin-bottom: 1rem; }
        label { display: block; margin-bottom: 0.5rem; color: #555; }
        input[type="text"], input[type="password"] { width: 100%; padding: 0.5rem; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 0.75rem; background-color: #007bff; color: white; border: none; border-radius: 4px; font-size: 1rem; cursor: pointer; }
        button:hover { background-color: #0056b3; }
        .error-message { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 0.75rem; border-radius: 4px; margin-bottom: 1rem; text-align: center; }
    </style>
</head>
<body>

<div class="login-container">
    <h2>System Login</h2>

    <%-- Check for an error message from the servlet and display it if it exists --%>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) {
    %>
    <div class="error-message"><%= errorMessage %></div>
    <%
        }
    %>

    <%-- The action attribute points to our servlet's URL pattern --%>
    <form action="${pageContext.request.contextPath}/auth/login" method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Login</button>
    </form>
    <div class="register-link">
        <p>Don't have an account? <a href="${pageContext.request.contextPath}/auth/register">Register here</a></p>
    </div>
</div>

</body>
</html>