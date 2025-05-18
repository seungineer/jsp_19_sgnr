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

        .action-btn {
            display: inline-block;
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
            transition: background-color 0.3s;
            margin-right: 5px;
            cursor: pointer;
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

        /* Modal styles */
        .modal {
            display: none;
            position: fixed;
            z-index: 1050;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.5);
            font-family: 'Noto Sans KR', sans-serif;
        }

        .modal-content {
            position: relative;
            background-color: white;
            margin: 50px auto;
            padding: 0;
            border-radius: 8px;
            width: 80%;
            max-width: 1000px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            animation: modalopen 0.4s;
        }

        @keyframes modalopen {
            from {opacity: 0; transform: translateY(-60px);}
            to {opacity: 1; transform: translateY(0);}
        }

        .close-modal {
            position: absolute;
            right: 20px;
            top: 15px;
            color: #555;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
            transition: color 0.3s;
        }

        .close-modal:hover,
        .close-modal:focus {
            color: #2196F3;
            text-decoration: none;
        }

        .modal-body {
            padding: 20px;
            max-height: 80vh;
            overflow-y: auto;
        }

        .modal-loading {
            text-align: center;
            padding: 40px;
            color: #333;
        }

        /* Override styles for detail.jsp content when in modal */
        .modal-body h1 {
            font-size: 24px;
            margin-top: 0;
            color: #333;
            margin-bottom: 20px;
        }

        .modal-body .container {
            padding: 0;
            margin: 0;
            max-width: none;
        }

        .modal-body .order-info {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin-bottom: 20px;
        }

        .modal-body .order-info-row {
            display: flex;
            margin-bottom: 10px;
        }

        .modal-body .order-info-label {
            width: 150px;
            font-weight: bold;
            color: #555;
        }

        .modal-body .order-info-value {
            flex: 1;
        }

        .modal-body .order-items-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .modal-body .order-items-table th, 
        .modal-body .order-items-table td {
            padding: 15px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        .modal-body .order-items-table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }

        .modal-body .order-items-table tr:hover {
            background-color: #f5f5f5;
        }

        .modal-body .product-name {
            font-weight: bold;
            color: #333;
        }

        .modal-body .product-price {
            color: #e53935;
            font-weight: bold;
        }

        /* Pagination styles */
        .pagination-container {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        .pagination-controls {
            display: flex;
            align-items: center;
        }

        .pagination-controls a, .pagination-controls span {
            margin: 0 5px;
            padding: 5px 10px;
            border: 1px solid #ccc;
            text-decoration: none;
            border-radius: 3px;
        }

        .pagination-controls a:hover {
            background-color: #f0f0f0;
        }

        .pagination-controls span.current-page {
            background-color: #2196F3;
            color: white;
            border-color: #2196F3;
        }

        .pagination-controls span.disabled {
            color: #ccc;
        }

        .pagination-info {
            text-align: center;
            margin-top: 10px;
            color: #666;
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

                        if ("10".equals(order.getSt_order())) {
                            orderStatusClass = "status-wait";
                            orderStatusText = "배송 대기";
                        } else if ("20".equals(order.getSt_order())) {
                            orderStatusClass = "status-paid";
                            orderStatusText = "결제 완료";
                        } else if ("30".equals(order.getSt_order())) {
                            orderStatusClass = "status-shipping";
                            orderStatusText = "주문 취소";
                        } else if ("40".equals(order.getSt_order())) {
                            orderStatusClass = "status-completed";
                            orderStatusText = "배송 중";
                        } else if ("50".equals(order.getSt_order())) {
                            orderStatusClass = "status-canceled";
                            orderStatusText = "배송 완료";
                        } else {
                            orderStatusClass = "status-wait";
                            orderStatusText = order.getSt_order();
                        }

                        String paymentStatusText = "";
                        if ("10".equals(order.getSt_payment())) {
                            paymentStatusText = "결제 대기";
                        } else if ("20".equals(order.getSt_payment())) {
                            paymentStatusText = "결제 완료";
                        } else if ("30".equals(order.getSt_payment())) {
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
                        </td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>

            <!-- Pagination Controls -->
            <%
                Integer currentPage = (Integer) request.getAttribute("currentPage");
                Integer totalPages = (Integer) request.getAttribute("totalPages");
                Integer totalOrders = (Integer) request.getAttribute("totalOrders");

                if (currentPage != null && totalPages != null && totalPages > 0) {
            %>
            <div class="pagination-container">
                <div class="pagination-controls">
                    <% if (currentPage > 1) { %>
                        <a href="${pageContext.request.contextPath}/mypage.do?menu=orderList&page=<%= currentPage - 1 %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;">이전</a>
                    <% } else { %>
                        <span style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; color: #ccc; border-radius: 3px;">이전</span>
                    <% } %>

                    <% 
                    // Display page numbers
                    int startPage = Math.max(1, currentPage - 2);
                    int endPage = Math.min(totalPages, currentPage + 2);

                    for (int i = startPage; i <= endPage; i++) { 
                    %>
                        <% if (i == currentPage) { %>
                            <span style="margin: 0 5px; padding: 5px 10px; background-color: #2196F3; color: white; border-radius: 3px;"><%= i %></span>
                        <% } else { %>
                            <a href="${pageContext.request.contextPath}/mypage.do?menu=orderList&page=<%= i %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;"><%= i %></a>
                        <% } %>
                    <% } %>

                    <% if (currentPage < totalPages) { %>
                        <a href="${pageContext.request.contextPath}/mypage.do?menu=orderList&page=<%= currentPage + 1 %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;">다음</a>
                    <% } else { %>
                        <span style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; color: #ccc; border-radius: 3px;">다음</span>
                    <% } %>
                </div>
            </div>

            <!-- Pagination Info -->
            <div style="text-align: center; margin-top: 10px; color: #666;">
                총 <%= totalOrders %>개의 주문 (페이지 <%= currentPage %> / <%= totalPages %>)
            </div>
            <% } %>
        <%
            }
        %>

    </div>

    <!-- Order Detail Modal -->
    <div id="orderDetailModal" class="modal">
        <div class="modal-content">
            <span class="close-modal">&times;</span>
            <div class="modal-body">
                <div class="modal-loading">
                    <p>주문 정보를 불러오는 중입니다...</p>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Get the modal
        const modal = document.getElementById("orderDetailModal");
        const modalBody = modal.querySelector(".modal-body");
        const closeBtn = modal.querySelector(".close-modal");

        // Close the modal when clicking the close button
        closeBtn.onclick = function() {
            modal.style.display = "none";
        }

        // Close the modal when clicking outside of it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }

        // Function to load order details
        function loadOrderDetails(orderId) {
            // Show loading message
            modalBody.innerHTML = '<div class="modal-loading"><p>주문 정보를 불러오는 중입니다...</p></div>';
            modal.style.display = "block";

            // Fetch order details
            fetch('${pageContext.request.contextPath}/order/detail.do?orderId=' + orderId)
                .then(response => response.text())
                .then(html => {
                    // Extract the content from the response
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, 'text/html');
                    const container = doc.querySelector('.container');

                    if (container) {
                        // Create a title element
                        const title = document.createElement('h1');
                        title.textContent = '주문 상세 정보';

                        // Clear the modal body and add the title
                        modalBody.innerHTML = '';
                        modalBody.appendChild(title);

                        // Add the container content
                        modalBody.appendChild(container);
                    } else {
                        modalBody.innerHTML = '<p>주문 정보를 불러오는데 실패했습니다.</p>';
                    }
                })
                .catch(error => {
                    console.error('Error loading order details:', error);
                    modalBody.innerHTML = '<p>주문 정보를 불러오는데 실패했습니다.</p>';
                });
        }

        // Update all detail buttons to use the modal
        document.querySelectorAll('.detail-btn').forEach(button => {
            button.addEventListener('click', function(e) {
                e.preventDefault();
                const url = this.getAttribute('href');
                const orderId = url.split('orderId=')[1];
                loadOrderDetails(orderId);
            });
        });
    </script>
</body>
</html>
