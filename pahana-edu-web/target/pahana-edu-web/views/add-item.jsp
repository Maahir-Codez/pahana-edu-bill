<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New Item</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}"><c:redirect url="/app/auth/login"/></c:if>
<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="form-container">
        <h2>Add New Item</h2>
        <hr>
        <c:if test="${not empty errorMessage}"><div class="error-message">${errorMessage}</div></c:if>

        <form action="${pageContext.request.contextPath}/app/items/add" method="post">
            <div class="form-group">
                <label for="sku">SKU (Stock Keeping Unit)</label>
                <input type="text" id="sku" name="sku" value="<c:out value='${item.sku}'/>" required>
            </div>
            <div class="form-group">
                <label for="name">Item Name</label>
                <input type="text" id="name" name="name" value="<c:out value='${item.name}'/>" required>
            </div>
            <div class="form-group">
                <label for="price">Price</label>
                <input type="number" id="price" name="price" step="0.01" min="0" value="<c:out value='${item.price}'/>" required>
            </div>
            <div class="form-group">
                <label for="stockQuantity">Stock Quantity</label>
                <input type="number" id="stockQuantity" name="stockQuantity" min="0" value="<c:out value='${item.stockQuantity}'/>" required>
            </div>
            <div class="form-group">
                <label for="category">Category</label>
                <input type="text" id="category" name="category" value="<c:out value='${item.category}'/>">
            </div>
            <div class="form-group">
                <label for="author">Author (if applicable)</label>
                <input type="text" id="author" name="author" value="<c:out value='${item.author}'/>">
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="4"><c:out value='${item.description}'/></textarea>
            </div>
            <div class="button-group">
                <button type="submit" class="btn btn-primary">Save Item</button>
                <a href="${pageContext.request.contextPath}/app/items/list" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>