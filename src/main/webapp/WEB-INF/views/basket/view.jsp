<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Basket" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.BasketItem" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.BasketDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.ProductDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>장바구니</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
        }

        .basket-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .basket-table th, .basket-table td {
            padding: 15px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        .basket-table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }

        .basket-table tr:hover {
            background-color: #f5f5f5;
        }

        .product-image {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 4px;
        }

        .product-name {
            font-weight: bold;
            color: #333;
        }

        .product-price {
            color: #e53935;
            font-weight: bold;
        }

        .quantity-control {
            display: flex;
            align-items: center;
        }

        .quantity-input {
            width: 50px;
            padding: 5px;
            text-align: center;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin: 0 5px;
        }

        .quantity-btn {
            background-color: #f0f0f0;
            border: none;
            width: 30px;
            height: 30px;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
        }

        .quantity-btn:hover {
            background-color: #e0e0e0;
        }

        .action-btn {
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: background-color 0.3s;
        }

        .remove-btn {
            background-color: #f44336;
            color: white;
        }

        .remove-btn:hover {
            background-color: #d32f2f;
        }

        .update-btn {
            background-color: #2196F3;
            color: white;
            margin-right: 5px;
        }

        .update-btn:hover {
            background-color: #1976D2;
        }

        .empty-basket {
            margin-top: 20px;
            padding: 30px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            text-align: center;
            color: #666;
        }

        .button-container {
            display: flex;
            justify-content: end;
            margin-top: 20px;
        }

        .button {
            display: inline-block;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            transition: background-color 0.3s;
        }

        .continue-shopping {
            background-color: #4CAF50;
            color: white;
        }

        .continue-shopping:hover {
            background-color: #45a049;
        }

        .clear-basket {
            background-color: #f44336;
            color: white;
        }

        .clear-basket:hover {
            background-color: #d32f2f;
        }

        .checkout {
            background-color: #2196F3;
            color: white;
            margin-right: 20px;
        }

        .checkout:hover {
            background-color: #1976D2;
        }

        .basket-summary {
            margin-top: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }

        .summary-row:last-child {
            border-bottom: none;
            font-weight: bold;
            font-size: 18px;
            color: #e53935;
        }

        .checkbox-column {
            width: 30px;
        }

        .select-all-container {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        .select-all-container input {
            margin-right: 10px;
        }
    </style>
    <script>
        function updateQuantity(itemId, increment) {
            var inputElement = document.getElementById('quantity-' + itemId);
            var currentValue = parseInt(inputElement.value);
            var newValue = currentValue + increment;

            if (newValue > 0) {
                inputElement.value = newValue;
                updateItemPrice(itemId);
            }
        }

        function updateItemPrice(itemId) {
            var quantityInput = document.getElementById('quantity-' + itemId);
            var quantity = parseInt(quantityInput.value);
            var priceElement = document.getElementById('price-' + itemId);
            var unitPrice = parseInt(priceElement.getAttribute('data-price'));
            var totalElement = document.getElementById('total-' + itemId);

            var newTotal = quantity * unitPrice;
            totalElement.textContent = formatCurrency(newTotal);
            totalElement.setAttribute('data-total', newTotal);

            var hiddenQuantity = document.getElementById('hidden-quantity-' + itemId);
            if (hiddenQuantity) {
                hiddenQuantity.value = quantity;
            }

            updateTotalPrice();
        }

        function updateTotalPrice() {
            var checkboxes = document.getElementsByName('selectedItems');
            var totalProductPrice = 0;
            var totalDeliveryFee = 0;

            for (var i = 0; i < checkboxes.length; i++) {
                var itemId = checkboxes[i].value;
                var totalElement = document.getElementById('total-' + itemId);
                var deliveryElement = document.getElementById('delivery-' + itemId);

                if (checkboxes[i].checked) {
                    totalProductPrice += parseInt(totalElement.getAttribute('data-total'));
                    totalDeliveryFee += parseInt(deliveryElement.getAttribute('data-fee'));
                }
            }

            document.getElementById('summary-product-price').textContent = formatCurrency(totalProductPrice);
            document.getElementById('summary-delivery-fee').textContent = formatCurrency(totalDeliveryFee);
            document.getElementById('summary-total-price').textContent = formatCurrency(totalProductPrice + totalDeliveryFee);
        }

        function formatCurrency(amount) {
            return new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW' }).format(amount);
        }

        function toggleSelectAll(source) {
            var checkboxes = document.getElementsByName('selectedItems');
            for (var i = 0; i < checkboxes.length; i++) {
                checkboxes[i].checked = source.checked;
            }
            updateTotalPrice();
        }

        function submitSelectedItems(action) {
            var form = document.getElementById('basketForm');
            var checkboxes = document.getElementsByName('selectedItems');
            var hasSelected = false;

            var oldHiddenFields = document.querySelectorAll('.dynamic-hidden-field');
            for (var i = 0; i < oldHiddenFields.length; i++) {
                oldHiddenFields[i].remove();
            }

            for (var i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].checked) {
                    hasSelected = true;
                    var itemId = checkboxes[i].value;
                    var quantity = document.getElementById('quantity-' + itemId).value;

                    var hiddenItem = document.createElement('input');
                    hiddenItem.type = 'hidden';
                    hiddenItem.name = 'selectedItemId';
                    hiddenItem.value = itemId;
                    hiddenItem.className = 'dynamic-hidden-field';
                    form.appendChild(hiddenItem);

                    var hiddenQuantity = document.createElement('input');
                    hiddenQuantity.type = 'hidden';
                    hiddenQuantity.name = 'selectedQuantity-' + itemId;
                    hiddenQuantity.value = quantity;
                    hiddenQuantity.className = 'dynamic-hidden-field';
                    form.appendChild(hiddenQuantity);
                }
            }

            if (!hasSelected) {
                alert('선택된 상품이 없습니다.');
                return false;
            }

            form.action = action;
            form.submit();
            return true;
        }

        window.onload = function() {
            var checkboxes = document.getElementsByName('selectedItems');
            for (var i = 0; i < checkboxes.length; i++) {
                checkboxes[i].checked = true;
            }

            updateTotalPrice();

            var quantityInputs = document.getElementsByClassName('quantity-input');
            for (var i = 0; i < quantityInputs.length; i++) {
                quantityInputs[i].addEventListener('change', function() {
                    var itemId = this.id.split('-')[1];
                    updateItemPrice(itemId);
                });
            }
        };
    </script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/include/header.jsp" />

    <div class="container">
        <h1>장바구니</h1>
        <%
            List<BasketItem> basketItems = (List<BasketItem>) request.getAttribute("basketItems");
            Basket userBasket = (Basket) request.getAttribute("userBasket");

            if (userBasket == null) {
        %>
            <div class="empty-basket">
                <p>장바구니를 불러올 수 없습니다.</p>
            </div>
        <%
            } else {
                if (basketItems == null || basketItems.isEmpty()) {
        %>
            <div class="empty-basket">
                <p>장바구니가 비어 있습니다.</p>
            </div>
        <%
                } else {
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.KOREA);
                    int totalPrice = 0;
        %>
            <div style="display: flex; justify-content: end">
                <a href="${pageContext.request.contextPath}/basket/clear.do?basketId=<%= userBasket.getBasketId() %>" class="button clear-basket">장바구니 비우기</a>
            </div>
            <form action="${pageContext.request.contextPath}/basket/update.do" method="post" id="basketForm">
                <input type="hidden" name="basketId" value="<%= userBasket.getBasketId() %>">

                <div class="select-all-container">
                    <input type="checkbox" id="selectAll" onclick="toggleSelectAll(this)">
                    <label for="selectAll">전체 선택</label>
                </div>

                <table class="basket-table">
                    <thead>
                        <tr>
                            <th class="checkbox-column">선택</th>
                            <th>상품 이미지</th>
                            <th>상품명</th>
                            <th>가격</th>
                            <th>배송비</th>
                            <th>수량</th>
                            <th>합계</th>
                            <th>관리</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        int totalDeliveryFee = 0;
                        ProductDao productDao = new ProductDao();

                        for (BasketItem item : basketItems) {
                            String productName = item.getProductName();
                            Product product = null;

                            if (productName == null || productName.isEmpty()) {
                                product = productDao.getProductById(item.getProductId());
                                if (product != null) {
                                    productName = product.getNm_product();
                                } else {
                                    productName = "Unknown Product";
                                }
                            }

                            if (product == null) {
                                product = productDao.getProductById(item.getProductId());
                            }

                            if (product != null) {
                                totalDeliveryFee += product.getQt_delivery_fee();
                            }

                            String imagePath = "";
                            if (item.getProductImage() != null && !item.getProductImage().isEmpty()) {
                                imagePath = request.getContextPath() + "/content/image.jsp?fileId=" + item.getProductImage();
                            }

                            int itemTotal = item.getPrice() * item.getQuantity();
                            totalPrice += itemTotal;
                    %>
                        <tr>
                            <td class="checkbox-column">
                                <input type="checkbox" name="selectedItems" value="<%= item.getItemId() %>" onchange="updateTotalPrice()">
                            </td>
                            <td>
                                <% if (imagePath != null && !imagePath.isEmpty()) { %>
                                    <img src="<%= imagePath %>" alt="<%= productName %>" class="product-image">
                                <% } else { %>
                                    <div class="product-image" style="background-color: #eee; display: flex; align-items: center; justify-content: center; font-size: 12px;">
                                        No Image
                                    </div>
                                <% } %>
                            </td>
                            <td class="product-name"><%= productName %></td>
                            <td class="product-price">
                                <span id="price-<%= item.getItemId() %>" data-price="<%= item.getPrice() %>"><%= currencyFormatter.format(item.getPrice()) %></span>
                            </td>
                            <td class="product-price">
                                <span id="delivery-<%= item.getItemId() %>" data-fee="<%= product.getQt_delivery_fee() %>">
                                    <%= product.getQt_delivery_fee() != 0 ? currencyFormatter.format(product.getQt_delivery_fee()) : "무료" %>
                                </span>
                            </td>
                            <td>
                                <div class="quantity-control">
                                    <button type="button" class="quantity-btn" onclick="updateQuantity(<%= item.getItemId() %>, -1)">-</button>
                                    <input type="number" id="quantity-<%= item.getItemId() %>" name="quantity-<%= item.getItemId() %>" value="<%= item.getQuantity() %>" min="1" class="quantity-input">
                                    <button type="button" class="quantity-btn" onclick="updateQuantity(<%= item.getItemId() %>, 1)">+</button>
                                    <input type="hidden" id="hidden-quantity-<%= item.getItemId() %>" name="quantity-<%= item.getItemId() %>" value="<%= item.getQuantity() %>">
                                </div>
                            </td>
                            <td class="product-price">
                                <span id="total-<%= item.getItemId() %>" data-total="<%= itemTotal %>"><%= currencyFormatter.format(itemTotal) %></span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/basket/remove.do?itemId=<%= item.getItemId() %>" class="action-btn remove-btn">삭제</a>
                            </td>
                        </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>

                <div class="basket-summary">
                    <div class="summary-row">
                        <span>상품 금액</span>
                        <span id="summary-product-price"><%= currencyFormatter.format(totalPrice) %></span>
                    </div>
                    <div class="summary-row">
                        <span>배송비</span>
                        <span id="summary-delivery-fee"><%= currencyFormatter.format(totalDeliveryFee) %></span>
                    </div>
                    <div class="summary-row">
                        <span>결제 예정 금액</span>
                        <span id="summary-total-price"><%= currencyFormatter.format(totalPrice + totalDeliveryFee) %></span>
                    </div>
                </div>

                <div class="button-container">
                    <a href="${pageContext.request.contextPath}/order/checkout.do" class="button checkout">전체 주문하기</a>
                    <a href="javascript:void(0);" class="button checkout" onclick="submitSelectedItems('${pageContext.request.contextPath}/order/checkout.do')">선택 상품 주문하기</a>
                </div>
            </form>
        <%
                }
            }
        %>
    </div>
</body>
</html>
