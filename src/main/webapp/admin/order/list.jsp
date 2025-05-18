<%@ page import="org.jsp.jsp_19_sgnr.dao.OrderDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.OrderWithUser" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    String orderId = request.getParameter("orderId");
    String userName = request.getParameter("userName");

    OrderDao orderDao = new OrderDao();

    List<OrderWithUser> orderList = orderDao.getAllOrders(orderId, userName);
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.KOREA);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주문 조회</title>
    <style>
        .search-container {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .search-form {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        .search-form input[type="text"] {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            flex-grow: 1;
        }
        .search-form button {
            padding: 8px 15px;
            background-color: #4285f4;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .search-form button:hover {
            background-color: #3367d6;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .empty-list {
            text-align: center;
            padding: 30px;
            color: #666;
        }
        .detail-btn {
            padding: 6px 12px;
            background-color: #4285f4;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.9em;
        }
        .detail-btn:hover {
            background-color: #3367d6;
        }

        /* Modal Styles */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }
        .modal-content {
            background-color: #fefefe;
            margin: 5% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            max-width: 800px;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }
        .close:hover {
            color: black;
        }
        .modal-header {
            padding-bottom: 10px;
            margin-bottom: 20px;
            border-bottom: 1px solid #eee;
        }
        .modal-body {
            margin-bottom: 20px;
        }
        .order-info {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .order-info-row {
            display: flex;
            margin-bottom: 10px;
        }
        .order-info-label {
            width: 120px;
            font-weight: bold;
        }
        .order-items-table {
            width: 100%;
            border-collapse: collapse;
        }
        .order-items-table th, .order-items-table td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .order-items-table th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <h3>주문 조회</h3>

    <div class="search-container">
        <form class="search-form" method="GET" action="admin.jsp">
            <input type="hidden" name="menu" value="orderList">
            <div>
                <label for="orderId">주문번호:</label>
                <input type="text" id="orderId" name="orderId" value="<%= orderId != null ? orderId : "" %>">
            </div>
            <div>
                <label for="userName">사용자명:</label>
                <input type="text" id="userName" name="userName" value="<%= userName != null ? userName : "" %>">
            </div>
            <button type="submit">검색</button>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>주문번호</th>
                <th>사용자명</th>
                <th>주문 금액</th>
                <th>주문 일시</th>
                <th>주문 상태</th>
                <th>결제 상태</th>
                <th>관리</th>
            </tr>
        </thead>
        <tbody>
            <% if (orderList.isEmpty()) { %>
                <tr>
                    <td colspan="7" class="empty-list">주문 내역이 없습니다.</td>
                </tr>
            <% } else { %>
                <% for (OrderWithUser order : orderList) { %>
                    <tr>
                        <td><%= order.getId_order() %></td>
                        <td><%= order.getNm_user() != null ? order.getNm_user() : "알 수 없음" %></td>
                        <td><%= currencyFormatter.format(order.getQt_order_amount()) %></td>
                        <td><%= order.getDa_order() %></td>
                        <td><%= order.getSt_order().equals("30") ? "주문 취소" : "주문 완료" %></td>
                        <td><%= order.getSt_payment().equals("20") ? "결제 완료" : "결제 대기" %></td>
                        <td>
                            <button class="detail-btn" onclick="showOrderDetail('<%= order.getId_order() %>')">상세 보기</button>
                        </td>
                    </tr>
                <% } %>
            <% } %>
        </tbody>
    </table>

    <div id="orderDetailModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">&times;</span>
            <div class="modal-header">
                <h3>주문 상세 정보</h3>
            </div>
            <div class="modal-body">
                <div class="order-info">
                    <div class="order-info-row">
                        <div class="order-info-label">주문번호:</div>
                        <div id="modal-order-id"></div>
                    </div>
                    <div class="order-info-row">
                        <div class="order-info-label">주문자명:</div>
                        <div id="modal-user-name"></div>
                    </div>
                    <div class="order-info-row">
                        <div class="order-info-label">수령자명:</div>
                        <div id="modal-receiver-name"></div>
                    </div>
                    <div class="order-info-row">
                        <div class="order-info-label">주문 금액:</div>
                        <div id="modal-order-amount"></div>
                    </div>
                    <div class="order-info-row">
                        <div class="order-info-label">결제 상태:</div>
                        <div id="modal-payment-status"></div>
                    </div>
                    <div class="order-info-row">
                        <div class="order-info-label">주문 일시:</div>
                        <div id="modal-order-date"></div>
                    </div>
                </div>

                <h4>주문 상품 목록</h4>
                <table class="order-items-table">
                    <thead>
                        <tr>
                            <th>상품명</th>
                            <th>수량</th>
                            <th>단가</th>
                            <th>총 금액</th>
                            <th>결제 상태</th>
                        </tr>
                    </thead>
                    <tbody id="modal-order-items">
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        var modal = document.getElementById("orderDetailModal");

        function showOrderDetail(orderId) {
            fetch('${pageContext.request.contextPath}/order/detail-json.do?orderId=' + orderId)
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        alert(data.error);
                        return;
                    }

                    document.getElementById('modal-order-id').textContent = data.order.id_order;
                    document.getElementById('modal-user-name').textContent = data.order.nm_user;
                    document.getElementById('modal-receiver-name').textContent = data.order.nm_receiver;
                    document.getElementById('modal-order-amount').textContent = data.order.qt_order_amount + '원';
                    document.getElementById('modal-payment-status').textContent = data.order.st_payment;
                    document.getElementById('modal-order-date').textContent = data.order.da_order;

                    var orderItemsHtml = '';
                    data.orderItems.forEach(item => {
                        orderItemsHtml += '<tr>';
                        orderItemsHtml += '<td>' + (item.nm_product || '알 수 없음') + '</td>';
                        orderItemsHtml += '<td>' + item.qt_order_item + '</td>';
                        orderItemsHtml += '<td>' + item.qt_unit_price + '원</td>';
                        orderItemsHtml += '<td>' + item.qt_order_item_amount + '원</td>';
                        orderItemsHtml += '<td>' + item.st_payment + '</td>';
                        orderItemsHtml += '</tr>';
                    });

                    document.getElementById('modal-order-items').innerHTML = orderItemsHtml;

                    modal.style.display = "block";
                })
                .catch(error => {
                    console.error('Error fetching order details:', error);
                    alert('주문 상세 정보를 가져오는 중 오류가 발생했습니다.');
                });
        }

        function closeModal() {
            modal.style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target == modal) {
                closeModal();
            }
        }
    </script>
</body>
</html>
