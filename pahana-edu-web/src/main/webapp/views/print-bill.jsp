<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bill for Order ORD-${order.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .invoice-box { max-width: 800px; margin: auto; padding: 30px; border: 1px solid #eee; box-shadow: 0 0 10px rgba(0, 0, 0, 0.15); font-size: 16px; line-height: 24px; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; color: #555; background: white;}
        .invoice-box table { width: 100%; line-height: inherit; text-align: left; border-collapse: collapse; }
        .invoice-box table td { padding: 5px; vertical-align: top; }
        .invoice-box table tr.top table td { padding-bottom: 20px; }
        .invoice-box table tr.information table td { padding-bottom: 40px; }
        .invoice-box table tr.heading td { background: #eee; border-bottom: 1px solid #ddd; font-weight: bold; }
        .invoice-box table tr.item td { border-bottom: 1px solid #eee; }
        .invoice-box table tr.total td:nth-child(2) { border-top: 2px solid #eee; font-weight: bold; }
        .text-right { text-align: right; }
        .print-btn-container { text-align: center; margin-top: 20px; }
        @media print {
            .navbar, .print-btn-container, .container > h1 { display: none; }
            .invoice-box { box-shadow: none; border: none; margin: 0; padding: 0; }
        }
    </style>
</head>
<body>
<c:if test="${empty sessionScope.loggedInUser}"><c:redirect url="/app/auth/login"/></c:if>
<!--<%@ include file="_navbar.jsp" %>-->

<div class="container">
    <h1>Bill Details</h1>
    <div class="invoice-box">
        <table>
            <tr class="top">
                <td colspan="4">
                    <table>
                        <tr>
                            <td>
                                <h2>Pahana Edu Bookshop</h2>
                                123 Education Lane<br>
                                Colombo, Sri Lanka
                            </td>
                            <td class="text-right">
                                <strong>Invoice #:</strong> ORD-${order.id}<br>
                                <strong>Created:</strong> ${order.getOrderDate()}
                                <br>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="information">
                <td colspan="4">
                    <table>
                        <tr>
                            <td>
                                <strong>Bill To:</strong><br>
                                <c:out value="${customer.fullName}"/><br>
                                <c:out value="${customer.address}"/><br>
                                <c:out value="${customer.city}"/>, <c:out value="${customer.postalCode}"/><br>
                                <c:out value="${customer.email}"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="heading">
                <td>Item</td>
                <td class="text-right">Unit Price</td>
                <td class="text-right">Quantity</td>
                <td class="text-right">Total</td>
            </tr>
            <c:forEach var="orderItem" items="${order.items}">
                <tr class="item">
                    <td><c:out value="${orderItem.itemName}"/></td>
                    <td class="text-right"><fmt:formatNumber value="${orderItem.priceAtPurchase}" type="currency" currencySymbol="$"/></td>
                    <td class="text-right"><c:out value="${orderItem.quantity}"/></td>
                    <td class="text-right"><fmt:formatNumber value="${orderItem.lineTotal}" type="currency" currencySymbol="$"/></td>
                </tr>
            </c:forEach>
            <tr class="total">
                <td colspan="3" class="text-right">Subtotal:</td>
                <td class="text-right"><fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="$"/></td>
            </tr>
            <tr class="total">
                <td colspan="3" class="text-right"><strong>Total:</strong></td>
                <td class="text-right"><strong><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$"/></strong></td>
            </tr>
        </table>
    </div>
    <div class="print-btn-container">
        <button class="btn btn-primary" onclick="window.print();">Print this Bill</button>
    </div>
</div>
</body>
</html>