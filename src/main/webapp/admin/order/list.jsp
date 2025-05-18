<%@ page import="org.jsp.jsp_19_sgnr.dao.OrderDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.OrderWithUser" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // 검색 파라미터 가져오기
    String orderId = request.getParameter("orderId");
    String userName = request.getParameter("userName");

    // OrderDao 인스턴스 생성
    OrderDao orderDao = new OrderDao();

    // 주문 목록 가져오기
    List<OrderWithUser> orderList = orderDao.getAllOrders(orderId, userName);
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
            </tr>
        </thead>
        <tbody>
            <% if (orderList.isEmpty()) { %>
                <tr>
                    <td colspan="6" class="empty-list">주문 내역이 없습니다.</td>
                </tr>
            <% } else { %>
                <% for (OrderWithUser order : orderList) { %>
                    <tr>
                        <td><%= order.getId_order() %></td>
                        <td><%= order.getNm_user() != null ? order.getNm_user() : "알 수 없음" %></td>
                        <td><%= order.getQt_order_amount() %>원</td>
                        <td><%= order.getDa_order() %></td>
                        <td><%= order.getSt_order() %></td>
                        <td><%= order.getSt_payment() %></td>
                    </tr>
                <% } %>
            <% } %>
        </tbody>
    </table>
</body>
</html>
