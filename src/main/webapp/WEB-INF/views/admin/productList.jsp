<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Category" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
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
        background-color: #4285f4;
        color: white;
        border-color: #4285f4;
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

<h3>상품 목록 관리</h3>

<form class="admin-search-form" method="get" action="<%= request.getContextPath() %>/admin/product/list.do">
    <select name="searchType">
        <option value="product" <%= "product".equals(request.getParameter("searchType")) ? "selected" : "" %>>상품명</option>
        <option value="category" <%= "category".equals(request.getParameter("searchType")) ? "selected" : "" %>>카테고리</option>
    </select>
    <input type="text" name="keyword" placeholder="검색어를 입력하세요" value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>">
    <% if (request.getParameter("categoryId") != null) { %>
        <input type="hidden" name="categoryId" value="<%= request.getParameter("categoryId") %>">
    <% } %>
    <% if (request.getParameter("sortBy") != null) { %>
        <input type="hidden" name="sortBy" value="<%= request.getParameter("sortBy") %>">
    <% } %>
    <% if (request.getParameter("sortOrder") != null) { %>
        <input type="hidden" name="sortOrder" value="<%= request.getParameter("sortOrder") %>">
    <% } %>
    <button type="submit">검색</button>
</form>

<%
    String searchType = request.getParameter("searchType");
    String keyword = request.getParameter("keyword");

    List<Product> products = (List<Product>) request.getAttribute("products");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
    Integer totalProducts = (Integer) request.getAttribute("totalProducts");
    Integer pageSize = (Integer) request.getAttribute("pageSize");

    if (currentPage == null) currentPage = 1;
    if (totalPages == null) totalPages = 1;
    if (totalProducts == null) totalProducts = 0;
    if (pageSize == null) pageSize = 10;

    List<Product> filteredProducts = products;
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.KOREA);
%>

<div class="admin-product-count">
    총 <strong><%= totalProducts %></strong>개의 상품이 있습니다.
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
                            Map<String, List<Category>> productCategoryMap = (Map<String, List<Category>>) request.getAttribute("productCategoryMap");
                            List<Category> productCategories = productCategoryMap != null ? productCategoryMap.get(product.getNo_product()) : null;

                            StringBuilder categoryDisplay = new StringBuilder();

                            if (productCategories != null && !productCategories.isEmpty()) {
                                for (int i = 0; i < productCategories.size(); i++) {
                                    Category cat = productCategories.get(i);
                                    String displayName = cat.getNmFullCategory() != null ? cat.getNmFullCategory() : cat.getNmCategory();
                                    categoryDisplay.append(displayName);

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
                    <td><%= currencyFormatter.format(product.getQt_sale_price()) %></td>
                    <td><%= product.getQt_stock() %></td>
                    <td><%= product.getQt_stock() > 0 ? "판매중" : "품절" %></td>
                </tr>
            <% } %>
        </tbody>
    </table>
<% } %>

<div class="pagination-container">
    <div class="pagination-controls">
        <% if (currentPage > 1) { %>
            <a href="<%= request.getContextPath() %>/admin/product/list.do?page=<%= currentPage - 1 %><%= request.getParameter("categoryId") != null ? "&categoryId=" + request.getParameter("categoryId") : "" %><%= request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "" %><%= request.getParameter("sortOrder") != null ? "&sortOrder=" + request.getParameter("sortOrder") : "" %><%= request.getParameter("keyword") != null ? "&keyword=" + request.getParameter("keyword") : "" %><%= request.getParameter("searchType") != null ? "&searchType=" + request.getParameter("searchType") : "" %>">이전</a>
        <% } else { %>
            <span class="disabled">이전</span>
        <% } %>

        <% 
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        for (int i = startPage; i <= endPage; i++) { 
        %>
            <% if (i == currentPage) { %>
                <span class="current-page"><%= i %></span>
            <% } else { %>
                <a href="<%= request.getContextPath() %>/admin/product/list.do?page=<%= i %><%= request.getParameter("categoryId") != null ? "&categoryId=" + request.getParameter("categoryId") : "" %><%= request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "" %><%= request.getParameter("sortOrder") != null ? "&sortOrder=" + request.getParameter("sortOrder") : "" %><%= request.getParameter("keyword") != null ? "&keyword=" + request.getParameter("keyword") : "" %><%= request.getParameter("searchType") != null ? "&searchType=" + request.getParameter("searchType") : "" %>"><%= i %></a>
            <% } %>
        <% } %>

        <% if (currentPage < totalPages) { %>
            <a href="<%= request.getContextPath() %>/admin/product/list.do?page=<%= currentPage + 1 %><%= request.getParameter("categoryId") != null ? "&categoryId=" + request.getParameter("categoryId") : "" %><%= request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "" %><%= request.getParameter("sortOrder") != null ? "&sortOrder=" + request.getParameter("sortOrder") : "" %><%= request.getParameter("keyword") != null ? "&keyword=" + request.getParameter("keyword") : "" %><%= request.getParameter("searchType") != null ? "&searchType=" + request.getParameter("searchType") : "" %>">다음</a>
        <% } else { %>
            <span class="disabled">다음</span>
        <% } %>
    </div>
</div>

<div class="pagination-info">
    페이지 <%= currentPage %> / <%= totalPages %>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
    });
</script>
