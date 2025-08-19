<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="navbar">
    <a href="${pageContext.request.contextPath}/dashboard">Pahana Edu Billing System</a>
    <div>
        <span>Welcome, <c:out value="${sessionScope.loggedInUser.fullName}"/>!</span>
        <a href="${pageContext.request.contextPath}/auth?action=logout" class="logout-btn">Logout</a>
    </div>
</div>