<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Order" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.OrderItem" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Product" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주문 상세 정보</title>
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

        h1, h2 {
            color: #333;
            margin-bottom: 20px;
        }

        .order-info {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin-bottom: 20px;
        }

        .order-info-row {
            display: flex;
            margin-bottom: 10px;
        }

        .order-info-label {
            width: 150px;
            font-weight: bold;
            color: #555;
        }

        .order-info-value {
            flex: 1;
        }

        .order-items-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .order-items-table th, .order-items-table td {
            padding: 15px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        .order-items-table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }

        .order-items-table tr:hover {
            background-color: #f5f5f5;
        }

        .product-name {
            font-weight: bold;
            color: #333;
        }

        .product-price {
            color: #e53935;
            font-weight: bold;
        }

        .order-status {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }

        .status-wait {
            background-color: #FFC107;
            color: #333;
        }

        .status-paid {
            background-color: #4CAF50;
            color: white;
        }

        .status-shipping {
            background-color: #2196F3;
            color: white;
        }

        .status-completed {
            background-color: #9E9E9E;
            color: white;
        }

        .status-canceled {
            background-color: #F44336;
            color: white;
        }

        .button-container {
            display: flex;
            justify-content: space-between;
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

        .back-btn {
            background-color: #2196F3;
            color: white;
        }

        .back-btn:hover {
            background-color: #1976D2;
        }

        .cancel-btn {
            background-color: #F44336;
            color: white;
        }

        .cancel-btn:hover {
            background-color: #D32F2F;
        }
    </style>
</head>
<body>

    <div class="container">

        <%
            List<OrderItem> orderItems = (List<OrderItem>) request.getAttribute("orderItems");
            Map<String, Product> productMap = (Map<String, Product>) request.getAttribute("productMap");

            if (orderItems == null || orderItems.isEmpty()) {
        %>
            <div class="order-info">
                <p>주문 항목을 찾을 수 없습니다.</p>
            </div>
        <%
            } else {
                OrderItem firstItem = orderItems.get(0);
                String orderId = firstItem.getId_order();
                String userId = firstItem.getNo_user();

                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.KOREA);

                // Calculate totals
                int totalAmount = 0;
                int totalDeliveryFee = 0;

                for (OrderItem item : orderItems) {
                    totalAmount += item.getQt_order_item_amount();
                    totalDeliveryFee += item.getQt_order_item_delivery_fee();
                }
        %>
            <div class="order-info">
                <div class="order-info-row">
                    <div class="order-info-label">주문번호</div>
                    <div class="order-info-value"><%= orderId %></div>
                </div>
                <div class="order-info-row">
                    <div class="order-info-label">주문자</div>
                    <div class="order-info-value"><%= userId %></div>
                </div>
                <div class="order-info-row">
                    <div class="order-info-label">주문 상태</div>
                    <div class="order-info-value">
                        <%
                            Order order = (Order) request.getAttribute("order");
                            String orderStatus = order.getSt_order();
                            String orderStatusClass = "";
                            String orderStatusText = "";

                            if (orderStatus == null) orderStatus = "10";

                            if ("10".equals(orderStatus)) {
                                orderStatusClass = "status-wait";
                                orderStatusText = "배송 대기";
                            } else if ("20".equals(orderStatus)) {
                                orderStatusClass = "status-paid";
                                orderStatusText = "결제 완료";
                            } else if ("30".equals(orderStatus)) {
                                orderStatusClass = "status-canceled";
                                orderStatusText = "주문 취소";
                            } else if ("40".equals(orderStatus)) {
                                orderStatusClass = "status-shipping";
                                orderStatusText = "배송 중";
                            } else if ("50".equals(orderStatus)) {
                                orderStatusClass = "status-completed";
                                orderStatusText = "배송 완료";
                            } else {
                                orderStatusClass = "status-wait";
                                orderStatusText = orderStatus;
                            }
                        %>
                        <span class="order-status <%= orderStatusClass %>"><%= orderStatusText %></span>
                    </div>
                </div>
                <div class="order-info-row">
                    <div class="order-info-label">결제 상태</div>
                    <div class="order-info-value">
                        <%
                            String paymentStatus = firstItem.getSt_payment();
                            String paymentStatusText = "";

                            if ("10".equals(paymentStatus)) {
                                paymentStatusText = "결제 대기";
                            } else if ("20".equals(paymentStatus)) {
                                paymentStatusText = "결제 완료";
                            } else if ("30".equals(paymentStatus)) {
                                paymentStatusText = "결제 취소";
                            } else {
                                paymentStatusText = paymentStatus;
                            }
                        %>
                        <%= paymentStatusText %>
                    </div>
                </div>
                <div class="order-info-row">
                    <div class="order-info-label">총 주문금액</div>
                    <div class="order-info-value"><%= currencyFormatter.format(totalAmount) %></div>
                </div>
                <div class="order-info-row">
                    <div class="order-info-label">총 배송비</div>
                    <div class="order-info-value"><%= currencyFormatter.format(totalDeliveryFee) %></div>
                </div>
                <div class="order-info-row">
                    <div class="order-info-label">총 결제금액</div>
                    <div class="order-info-value product-price"><%= currencyFormatter.format(totalAmount + totalDeliveryFee) %></div>
                </div>
            </div>

            <h2>주문 상품 목록</h2>

            <table class="order-items-table">
                <thead>
                    <tr>
                        <th>상품명</th>
                        <th>단가</th>
                        <th>수량</th>
                        <th>금액</th>
                        <th>배송비</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    for (OrderItem item : orderItems) {
                        Product product = productMap.get(item.getNo_product());
                        String productName = product != null ? product.getNm_product() : "Unknown Product";
                %>
                    <tr>
                        <td class="product-name"><%= productName %></td>
                        <td><%= currencyFormatter.format(item.getQt_unit_price()) %></td>
                        <td><%= item.getQt_order_item() %></td>
                        <td><%= currencyFormatter.format(item.getQt_order_item_amount()) %></td>
                        <td><%= currencyFormatter.format(item.getQt_order_item_delivery_fee()) %></td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>

            <div class="button-container">
                <% if (!"50".equals(order.getSt_order()) && ("10".equals(order.getSt_order()) || "20".equals(order.getSt_order()))) { %>
                    <a href="${pageContext.request.contextPath}/order/cancel.do?orderId=<%= orderId %>" class="button cancel-btn" onclick="return confirm('주문을 취소하시겠습니까?');">주문 취소</a>
                <% } %>
            </div>
        <%
            }
        %>
    </div>
</body>
</html>
