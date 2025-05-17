<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주문 확인</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
        }

        .confirmation-box {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 30px;
            margin-bottom: 20px;
        }

        .confirmation-message {
            font-size: 18px;
            color: #4CAF50;
            margin-bottom: 20px;
        }

        .order-details {
            margin-top: 20px;
        }

        .order-id {
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }

        .button-container {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
        }

        .button {
            display: inline-block;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            transition: background-color 0.3s;
        }

        .view-orders {
            background-color: #2196F3;
            color: white;
        }

        .view-orders:hover {
            background-color: #1976D2;
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
    <!-- Include header -->
    <jsp:include page="/WEB-INF/views/include/header.jsp" />

    <div class="container">
        <h1>주문 확인</h1>
        
        <div class="confirmation-box">
            <div class="confirmation-message">
                주문이 성공적으로 완료되었습니다.
            </div>
            
            <div class="order-details">
                <div class="order-id">
                    주문번호: <%= request.getAttribute("orderId") %>
                </div>
                <p>주문 내역은 '주문 내역 조회'에서 확인하실 수 있습니다.</p>
            </div>
            
            <div class="button-container">
                <a href="${pageContext.request.contextPath}/order/list.do" class="button view-orders">주문 내역 조회</a>
                <a href="${pageContext.request.contextPath}/product/list.do" class="button continue-shopping">쇼핑 계속하기</a>
            </div>
        </div>
    </div>
</body>
</html>