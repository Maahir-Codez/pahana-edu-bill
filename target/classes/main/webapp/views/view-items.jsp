<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Items</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}"><c:redirect url="/auth?action=login"/></c:if>
<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>Item Inventory</h1>
        <a href="${pageContext.request.contextPath}/items?action=add" class="btn-primary" style="padding: 10px; border-radius: 5px;">Add New Item</a>
    </div>

    <table>
        <thead>
        <tr>
            <th>SKU</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${itemList}">
            <tr>
                <td><c:out value="${item.sku}"/></td>
                <td><c:out value="${item.name}"/></td>
                <td><c:out value="${item.category}"/></td>
                <td><fmt:formatNumber value="${item.price}" type="currency" currencySymbol="$"/></td>
                <td><c:out value="${item.stockQuantity}"/></td>
                <td>
                    <c:if test="${item.active}"><span style="color: green;">Active</span></c:if>
                    <c:if test="${not item.active}"><span style="color: red;">Inactive</span></c:if>
                </td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/items?action=edit&id=${item.id}" class="edit-link">Edit</a>
                    <form action="${pageContext.request.contextPath}/items?action=delete" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this item? This action is permanent.');">
                        <input type="hidden" name="id" value="${item.id}">
                        <button type="submit" class="delete-link">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>