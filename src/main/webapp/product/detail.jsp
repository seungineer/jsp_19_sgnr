<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Product" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Category" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상품 상세 정보</title>
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

        .product-detail {
            display: flex;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            margin-top: 20px;
        }

        .product-image {
            flex: 0 0 50%;
            padding: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .product-image img {
            max-width: 100%;
            max-height: 500px;
            object-fit: contain;
        }

        .product-info {
            flex: 0 0 50%;
            padding: 30px;
            border-left: 1px solid #eee;
        }

        .product-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
            color: #333;
        }

        .product-categories {
            margin-bottom: 15px;
            color: #666;
        }

        .product-categories span {
            display: inline-block;
            background-color: #f0f0f0;
            padding: 4px 8px;
            border-radius: 4px;
            margin-right: 5px;
            margin-bottom: 5px;
            font-size: 14px;
        }

        .product-price {
            font-size: 28px;
            font-weight: bold;
            color: #e53935;
            margin-bottom: 15px;
        }

        .product-original-price {
            text-decoration: line-through;
            color: #999;
            font-size: 18px;
            margin-bottom: 5px;
        }

        .product-discount {
            color: #e53935;
            font-size: 16px;
            margin-bottom: 20px;
        }

        .product-stock {
            margin-bottom: 20px;
            color: #333;
        }

        .product-description {
            margin-bottom: 30px;
            line-height: 1.6;
            color: #555;
        }

        .add-to-cart-form {
            margin-top: 20px;
        }

        .quantity-input {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }

        .quantity-input label {
            margin-right: 10px;
            font-weight: bold;
        }

        .quantity-input input {
            width: 60px;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            text-align: center;
        }

        .add-to-cart-button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 12px 24px;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
            transition: background-color 0.3s;
        }

        .add-to-cart-button:hover {
            background-color: #45a049;
        }

        .back-button {
            display: inline-block;
            margin-top: 20px;
            color: #666;
            text-decoration: none;
        }

        .back-button:hover {
            text-decoration: underline;
        }

        .delivery-info {
            margin-top: 15px;
            padding: 15px;
            background-color: #f9f9f9;
            border-radius: 4px;
        }

        .delivery-info p {
            margin: 5px 0;
            color: #666;
        }

        .login-message {
            margin-top: 20px;
            padding: 15px;
            background-color: #fff3cd;
            border: 1px solid #ffeeba;
            border-radius: 4px;
            color: #856404;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>상품 상세 정보</h1>

        <%
            Product product = (Product) request.getAttribute("product");
            String imagePath = (String) request.getAttribute("imagePath");
            List<Category> categories = (List<Category>) request.getAttribute("categories");

            if (product != null) {
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.KOREA);
                int discountAmount = product.getQt_customer() - product.getQt_sale_price();
                double discountRate = (double) discountAmount / product.getQt_customer() * 100;
        %>

        <div class="product-detail">
            <div class="product-image">
                <% if (imagePath != null && !imagePath.isEmpty()) { %>
                    <img src="<%= imagePath %>" alt="<%= product.getNm_product() %>">
                <% } else { %>
                    <div style="width: 400px; height: 400px; background-color: #eee; display: flex; align-items: center; justify-content: center;">
                        상품 이미지가 없습니다
                    </div>
                <% } %>
            </div>

            <div class="product-info">
                <div class="product-title"><%= product.getNm_product() %></div>

                <div class="product-categories">
                    <% if (categories != null && !categories.isEmpty()) { 
                        for (Category category : categories) { %>
                            <span><%= category.getName() %></span>
                        <% } 
                    } %>
                </div>

                <div class="product-original-price"><%= currencyFormatter.format(product.getQt_customer()) %></div>
                <div class="product-price"><%= currencyFormatter.format(product.getQt_sale_price()) %></div>
                <div class="product-discount">(<%= String.format("%.1f", discountRate) %>% 할인)</div>

                <div class="product-stock">
                    재고: <%= product.getQt_stock() %> 개
                </div>

                <div class="product-description">
                    <%= product.getNm_detail_explain() %>
                </div>

                <div class="delivery-info">
                    <p>배송비: <%= product.getQt_delivery_fee() > 0 ? currencyFormatter.format(product.getQt_delivery_fee()) : "무료" %></p>
                    <p>판매 기간: <%= product.getDt_start_date() %> ~ <%= product.getDt_end_date() %></p>
                </div>

                <%
                    // Check if user is logged in
                    Member member = (Member) session.getAttribute("member");
                    if (member != null) {
                %>
                <form class="add-to-cart-form" action="<%= request.getContextPath() %>/basket/add.do" method="post">
                    <input type="hidden" name="productId" value="<%= product.getNo_product() %>">
                    <input type="hidden" name="price" value="<%= product.getQt_sale_price() %>">

                    <div class="quantity-input">
                        <label for="quantity">수량:</label>
                        <input type="number" id="quantity" name="quantity" value="1" min="1" max="<%= product.getQt_stock() %>">
                    </div>

                    <button type="submit" class="add-to-cart-button">장바구니에 담기</button>
                </form>
                <% } else { %>
                <div class="login-message">
                    장바구니에 담으려면 <a href="<%= request.getContextPath() %>/member/loginForm.do">로그인</a>이 필요합니다.
                </div>
                <% } %>
            </div>
        </div>

        <a href="<%= request.getContextPath() %>/product/list.do" class="back-button">← 상품 목록으로 돌아가기</a>

        <% } else { %>
        <div>
            <p>상품 정보를 찾을 수 없습니다.</p>
            <a href="<%= request.getContextPath() %>/product/list.do" class="back-button">← 상품 목록으로 돌아가기</a>
        </div>
        <% } %>
    </div>
</body>
</html>
