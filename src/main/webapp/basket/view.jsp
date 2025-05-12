<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>장바구니</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .empty-basket {
            margin-top: 20px;
            color: #666;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <h1>장바구니</h1>
    
    <%
        Map<String, Integer> basket = (Map<String, Integer>) session.getAttribute("basket");
        if (basket == null || basket.isEmpty()) {
    %>
        <div class="empty-basket">
            <p>장바구니가 비어 있습니다.</p>
        </div>
    <%
        } else {
    %>
        <table>
            <tr>
                <th>상품 ID</th>
                <th>수량</th>
            </tr>
            <%
                for (Map.Entry<String, Integer> entry : basket.entrySet()) {
            %>
            <tr>
                <td><%= entry.getKey() %></td>
                <td><%= entry.getValue() %></td>
            </tr>
            <%
                }
            %>
        </table>
    <%
        }
    %>
    
    <a href="<%= request.getContextPath() %>/main.jsp" class="button">계속 쇼핑하기</a>
</body>
</html>