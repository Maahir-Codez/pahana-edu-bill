<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create New Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        fieldset { margin-top: 2rem; border: 1px solid #ccc; padding: 1rem; border-radius: 4px; }
        .item-adder { display: flex; gap: 1rem; align-items: flex-end; }
        .item-adder .form-group { flex-grow: 1; }
        #cartTable tfoot th { text-align: right; font-size: 1.2em; }
        .remove-btn { background-color: #dc3545; color: white; border: none; padding: 0.3rem 0.6rem; border-radius: 4px; cursor: pointer; }
        #item-adder-error { color: #dc3545; font-size: 0.9em; height: 1em; margin-top: 0.5rem; }
    </style>
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}"><c:redirect url="/auth?action=login"/></c:if>
<%@ include file="_navbar.jsp" %>

<div class="container">
    <div class="form-container">
        <h2>Create New Order</h2>
        <hr>
        <c:if test="${not empty errorMessage}"><div class="error-message">${errorMessage}</div></c:if>

        <form id="orderForm" action="${pageContext.request.contextPath}/orders?action=create" method="post">
            <!-- Customer Selection -->
            <div class="form-group">
                <label for="customerId">Select Customer</label>
                <select id="customerId" name="customerId" class="form-control" required>
                    <option value="">-- Please select a customer --</option>
                    <c:forEach var="customer" items="${customers}">
                        <c:if test="${customer.active}">
                            <option value="${customer.id}">${customer.fullName} (${customer.accountNumber})</option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>

            <!-- Item Selection -->
            <fieldset>
                <legend>Add Items to Order</legend>
                <div class="item-adder">
                    <div class="form-group">
                        <label for="itemSelector">Select Item</label>
                        <select id="itemSelector" class="form-control">
                            <option value="">-- Select an item to add --</option>
                            <c:forEach var="item" items="${items}">
                                <option value="${item.id}" data-name="${item.name}" data-price="${item.price}" data-stock="${item.stockQuantity}">${item.name} - (<fmt:formatNumber value="${item.price}" type="currency" currencySymbol="$"/>)</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="itemQuantity">Quantity</label>
                        <input type="number" id="itemQuantity" value="1" min="1" class="form-control" style="width: 80px;">
                    </div>
                    <button type="button" id="addItemBtn" class="btn btn-primary" style="margin-bottom: 1.5rem;">Add</button>
                </div>
                <div id="item-adder-error"></div>
            </fieldset>

            <!-- Cart / Order Items Table -->
            <h3 style="margin-top: 2rem;">Order Items</h3>
            <table id="cartTable">
                <thead>
                <tr>
                    <th>Item Name</th>
                    <th>Quantity</th>
                    <th>Unit Price</th>
                    <th>Line Total</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <tr id="empty-cart-row">
                    <td colspan="5" style="text-align: center;">Your cart is empty.</td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="3" style="text-align: right; font-weight: bold;">Subtotal:</td>
                    <td id="subtotalCell" style="font-weight: bold;">$0.00</td>
                    <td></td>
                </tr>
                </tfoot>
            </table>

            <div class="button-group">
                <button type="submit" class="btn btn-primary">Create Order</button>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Cancel</a>
            </div>
            <div id="cart-items-container"></div>
        </form>
    </div>
</div>

<template id="cartItemTemplate">
    <tr>
        <td class="item-name"></td>
        <td class="item-quantity"></td>
        <td class="item-price"></td>
        <td class="item-line-total"></td>
        <td><button type="button" class="remove-item-btn">Remove</button></td>
    </tr>
</template>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const itemSelector = document.getElementById('itemSelector');
        const itemQuantityInput = document.getElementById('itemQuantity');
        const addItemBtn = document.getElementById('addItemBtn');
        const cartTableBody = document.querySelector('#cartTable tbody');
        const subtotalCell = document.getElementById('subtotalCell');
        const orderForm = document.getElementById('orderForm');
        const errorDisplay = document.getElementById('item-adder-error');

        let cart = new Map();

        const rowTemplate = document.getElementById('cartItemTemplate');

        addItemBtn.addEventListener('click', handleAddItem);
        cartTableBody.addEventListener('click', handleRemoveItem);
        orderForm.addEventListener('submit', validateFormSubmission);

        function renderCart() {
            let currentSubtotal = 0;

            cartTableBody.innerHTML = '';

            if (cart.size === 0) {
                const emptyRow = document.createElement('tr');
                const emptyCell = document.createElement('td');
                emptyCell.setAttribute('colspan', '5');
                emptyCell.style.textAlign = 'center';
                emptyCell.textContent = 'Your cart is empty.';
                emptyRow.appendChild(emptyCell);
                cartTableBody.appendChild(emptyRow);
                subtotalCell.textContent = '$0.00';
                return;
            }

            cart.forEach((itemData, itemId) => {
                const row = rowTemplate.content.cloneNode(true);

                row.querySelector('.item-name').textContent = itemData.name;
                row.querySelector('.item-quantity').textContent = itemData.quantity;
                row.querySelector('.item-price').textContent = itemData.price.toFixed(2);
                row.querySelector('.item-line-total').textContent = itemData.lineTotal.toFixed(2);
                row.querySelector('.remove-item-btn').dataset.id = itemId;

                cartTableBody.appendChild(row);

                currentSubtotal += itemData.lineTotal;
            });

            subtotalCell.textContent = currentSubtotal.toFixed(2);
        }

        function handleAddItem() {
            const selectedOption = itemSelector.options[itemSelector.selectedIndex];
            const itemId = selectedOption.value;
            const quantity = parseInt(itemQuantityInput.value, 10);

            if (!validateItemAddition(itemId, selectedOption, quantity)) {
                return;
            }

            clearError();

            const itemData = {
                id: itemId,
                name: selectedOption.dataset.name,
                price: parseFloat(selectedOption.dataset.price),
                quantity: quantity,
                lineTotal: parseFloat(selectedOption.dataset.price) * quantity
            };

            cart.set(itemId, itemData);
            renderCart();
            resetInputs();
        }

        function handleRemoveItem(event) {
            if (event.target.classList.contains('remove-item-btn')) {
                const itemIdToRemove = event.target.dataset.id;
                cart.delete(itemIdToRemove);
                renderCart();
            }
        }

        function validateFormSubmission(event) {
            if (cart.size === 0) {
                event.preventDefault();
                alert('Cannot create an order with no items.');
                return;
            }

            const container = document.getElementById('cart-items-container');
            container.innerHTML = '';

            cart.forEach((itemData, itemId) => {
                const idInput = document.createElement('input');
                idInput.type = 'hidden';
                idInput.name = 'itemIds[]';
                idInput.value = itemData.id;
                container.appendChild(idInput);

                const qtyInput = document.createElement('input');
                qtyInput.type = 'hidden';
                qtyInput.name = 'quantities[]';
                qtyInput.value = itemData.quantity;
                container.appendChild(qtyInput);
            });
        }

        function validateItemAddition(itemId, selectedOption, quantity) {
            if (!itemId) {
                showError('Please select an item.');
                return false;
            }
            if (cart.has(itemId)) {
                showError('Item is already in the cart. Remove it to add a new quantity.');
                return false;
            }
            const maxStock = parseInt(selectedOption.dataset.stock, 10);
            if (isNaN(quantity) || quantity <= 0) {
                showError('Please enter a valid quantity.');
                return false;
            }
            if (quantity > maxStock) {
                showError(`Only ${maxStock} available in stock.`);
                return false;
            }
            return true;
        }

        function showError(message) {
            errorDisplay.textContent = message;
        }

        function clearError() {
            errorDisplay.textContent = '';
        }

        function resetInputs() {
            itemSelector.selectedIndex = 0;
            itemQuantityInput.value = 1;
            itemQuantityInput.removeAttribute('max');
        }

        function updateQuantityInputMax() {
            const selectedOption = itemSelector.options[itemSelector.selectedIndex];
            if (selectedOption.value) {
                itemQuantityInput.max = selectedOption.dataset.stock;
            } else {
                itemQuantityInput.removeAttribute('max');
            }
        }

        itemSelector.addEventListener('change', updateQuantityInputMax);
    });

</script>

</body>
</html>