<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">

<div class="navbar">
    <a href="${pageContext.request.contextPath}/dashboard" class="navbar-brand">Pahana Edu</a>

    <nav class="navbar-nav">
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-link">
            Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/app/customers/list" class="nav-link">
            Customers
        </a>
        <a href="${pageContext.request.contextPath}/app/items/list" class="nav-link">
            Items
        </a>
        <div class="nav-dropdown">
            <a href="#" class="nav-link dropdown-toggle">
                Orders
            </a>
            <div class="dropdown-menu">
                <a href="${pageContext.request.contextPath}/app/orders/create" class="dropdown-item">Create Order</a>
                <a href="${pageContext.request.contextPath}/app/orders/list" class="dropdown-item">Order History</a>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/app/help" class="nav-link">
            Help
        </a>
    </nav>

    <div class="user-info">
        <span class="user-greeting">Welcome, <c:out value="${sessionScope.loggedInUser.fullName}"/>!</span>
        <a href="${pageContext.request.contextPath}/app/auth/logout" class="logout-btn">Logout</a>
    </div>
</div>