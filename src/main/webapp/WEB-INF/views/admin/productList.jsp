<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Category" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<style>
    .admin-product-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
        background-color: white;
    }

    .admin-product-table th, 
    .admin-product-table td {
        padding: 12px 15px;
        text-align: left;
        border-bottom: 1px solid #ddd;
    }

    .admin-product-table th {
        background-color: #f5f5f5;
        font-weight: bold;
        color: #333;
    }

    .admin-product-table tr:hover {
        background-color: #f9f9f9;
    }

    .admin-search-form {
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        background-color: white;
        padding: 15px;
        border-radius: 5px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }

    .admin-search-form select,
    .admin-search-form input[type="text"] {
        padding: 8px 12px;
        margin-right: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .admin-search-form button {
        padding: 8px 16px;
        background-color: #4285f4;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .admin-search-form button:hover {
        background-color: #3367d6;
    }

    .admin-product-count {
        margin-bottom: 10px;
        font-size: 14px;
        color: #666;
    }

    .no-products {
        text-align: center;
        padding: 30px;
        background-color: white;
        border-radius: 5px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        color: #666;
        margin-top: 20px;
    }
</style>

<h3>상품 목록 관리</h3>

<form class="admin-search-form" method="get" action="">
    <select name="searchType">
        <option value="product" <%= "product".equals(request.getParameter("searchType")) ? "selected" : "" %>>상품명</option>
        <option value="category" <%= "category".equals(request.getParameter("searchType")) ? "selected" : "" %>>카테고리</option>
    </select>
    <input type="text" name="keyword" placeholder="검색어를 입력하세요" value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>">
    <button type="submit">검색</button>
</form>

<%
    // Get search parameters
    String searchType = request.getParameter("searchType");
    String keyword = request.getParameter("keyword");

    // Get products from request attribute
    List<Product> products = (List<Product>) request.getAttribute("products");

    // Filter products based on search criteria if needed
    List<Product> filteredProducts = products;
    if (keyword != null && !keyword.isEmpty()) {
        filteredProducts = new java.util.ArrayList<>();
        for (Product product : products) {
            if ("product".equals(searchType) && product.getNm_product().toLowerCase().contains(keyword.toLowerCase())) {
                filteredProducts.add(product);
            } else if ("category".equals(searchType)) {
                // This would need to be enhanced with actual category name lookup
                // For now, we'll just add all products when searching by category
                filteredProducts.add(product);
            }
        }
    }
%>

<div class="admin-product-count">
    총 <strong><%= filteredProducts != null ? filteredProducts.size() : 0 %></strong>개의 상품이 있습니다.
</div>

<% if (filteredProducts == null || filteredProducts.isEmpty()) { %>
    <div class="no-products">
        <p>상품이 없습니다.</p>
    </div>
<% } else { %>
    <table class="admin-product-table">
        <thead>
            <tr>
                <th>상품 ID</th>
                <th>상품명</th>
                <th>카테고리</th>
                <th>판매가</th>
                <th>재고</th>
                <th>상태</th>
            </tr>
        </thead>
        <tbody>
            <% for (Product product : filteredProducts) { %>
                <tr>
                    <td><%= product.getNo_product() %></td>
                    <td><%= product.getNm_product() %></td>
                    <td>
                        <%
                            // Get categories for this product from the pre-loaded map
                            Map<String, List<Category>> productCategoryMap = (Map<String, List<Category>>) request.getAttribute("productCategoryMap");
                            List<Category> productCategories = productCategoryMap != null ? productCategoryMap.get(product.getNo_product()) : null;

                            StringBuilder categoryDisplay = new StringBuilder();

                            if (productCategories != null && !productCategories.isEmpty()) {
                                for (int i = 0; i < productCategories.size(); i++) {
                                    Category cat = productCategories.get(i);
                                    // Display the fullname if available, otherwise display the name
                                    String displayName = cat.getNmFullCategory() != null ? cat.getNmFullCategory() : cat.getNmCategory();
                                    categoryDisplay.append(displayName);

                                    // Add comma if not the last category
                                    if (i < productCategories.size() - 1) {
                                        categoryDisplay.append(", ");
                                    }
                                }
                            } else {
                                categoryDisplay.append("카테고리 없음");
                            }
                        %>
                        <%= categoryDisplay.toString() %>
                    </td>
                    <td><%= product.getQt_sale_price() %>원</td>
                    <td><%= product.getQt_stock() %></td>
                    <td><%= product.getQt_stock() > 0 ? "판매중" : "품절" %></td>
                </tr>
            <% } %>
        </tbody>
    </table>
<% } %>

<script>
    // Add any JavaScript functionality here if needed
    document.addEventListener('DOMContentLoaded', function() {
        // Example: Add event listeners or other initialization code
    });
</script>
