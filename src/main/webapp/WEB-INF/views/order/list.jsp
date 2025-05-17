<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Order" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주문 내역</title>
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

        .orders-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .orders-table th, .orders-table td {
            padding: 15px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        .orders-table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }

        .orders-table tr:hover {
            background-color: #f5f5f5;
        }

        .order-id {
            font-weight: bold;
            color: #2196F3;
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

        .action-btn {
            display: inline-block;
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
            transition: background-color 0.3s;
            margin-right: 5px;
        }

        .detail-btn {
            background-color: #2196F3;
            color: white;
        }

        .detail-btn:hover {
            background-color: #1976D2;
        }

        .cancel-btn {
            background-color: #F44336;
            color: white;
        }

        .cancel-btn:hover {
            background-color: #D32F2F;
        }

        .empty-orders {
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
    </style>
</head>
<body>
    <div class="container">

        <%
            List<Order> orders = (List<Order>) request.getAttribute("orders");
            
            if (orders == null || orders.isEmpty()) {
        %>
            <div class="empty-orders">
                <p>주문 내역이 없습니다.</p>
            </div>
        <%
            } else {
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.KOREA);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        %>
            <table class="orders-table">
                <thead>
                    <tr>
                        <th>주문번호</th>
                        <th>주문일자</th>
                        <th>주문금액</th>
                        <th>배송비</th>
                        <th>수령인</th>
                        <th>주문상태</th>
                        <th>결제상태</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    for (Order order : orders) {
                        String orderStatusClass = "";
                        String orderStatusText = "";
                        
                        if ("WAIT".equals(order.getSt_order())) {
                            orderStatusClass = "status-wait";
                            orderStatusText = "배송 대기";
                        } else if ("PAID".equals(order.getSt_order())) {
                            orderStatusClass = "status-paid";
                            orderStatusText = "결제 완료";
                        } else if ("SHIP".equals(order.getSt_order())) {
                            orderStatusClass = "status-shipping";
                            orderStatusText = "배송 중";
                        } else if ("COMP".equals(order.getSt_order())) {
                            orderStatusClass = "status-completed";
                            orderStatusText = "배송 완료";
                        } else if ("CNCL".equals(order.getSt_order())) {
                            orderStatusClass = "status-canceled";
                            orderStatusText = "주문 취소";
                        } else {
                            orderStatusClass = "status-wait";
                            orderStatusText = order.getSt_order();
                        }
                        
                        String paymentStatusText = "";
                        if ("WAIT".equals(order.getSt_payment())) {
                            paymentStatusText = "결제 대기";
                        } else if ("PAID".equals(order.getSt_payment())) {
                            paymentStatusText = "결제 완료";
                        } else if ("CNCL".equals(order.getSt_payment())) {
                            paymentStatusText = "결제 취소";
                        } else {
                            paymentStatusText = order.getSt_payment();
                        }
                %>
                    <tr>
                        <td class="order-id"><%= order.getId_order() %></td>
                        <td><%= order.getDa_order() != null ? order.getDa_order() : "N/A" %></td>
                        <td><%= currencyFormatter.format(order.getQt_order_amount()) %></td>
                        <td><%= currencyFormatter.format(order.getQt_deli_money()) %></td>
                        <td><%= order.getNm_receiver() %></td>
                        <td><span class="order-status <%= orderStatusClass %>"><%= orderStatusText %></span></td>
                        <td><%= paymentStatusText %></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/order/detail.do?orderId=<%= order.getId_order() %>" class="action-btn detail-btn">상세보기</a>
                            <% if ("WAIT".equals(order.getSt_order()) || "PAID".equals(order.getSt_order())) { %>
                                <a href="${pageContext.request.contextPath}/order/cancel.do?orderId=<%= order.getId_order() %>" class="action-btn cancel-btn" onclick="return confirm('주문을 취소하시겠습니까?');">취소</a>
                            <% } %>
                        </td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        <%
            }
        %>

    </div>
</body>
</html>