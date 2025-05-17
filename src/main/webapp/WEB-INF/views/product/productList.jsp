<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Category" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Product" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.ContentDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>쇼핑몰 - 상품 목록</title>
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

        .main-content {
            display: flex;
            margin-top: 20px;
        }

        .sidebar {
            width: 250px;
            margin-right: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }

        .content {
            flex: 1;
        }

        .category-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 15px;
            color: #333;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }

        .category-item {
            margin: 5px 0;
            cursor: pointer;
        }

        .category-item a {
            text-decoration: none;
            color: #333;
            display: block;
            padding: 8px 0;
        }

        .category-item a:hover {
            color: #4285f4;
        }

        .category-item.level-1 { margin-left: 0; }
        .category-item.level-2 { margin-left: 15px; }
        .category-item.level-3 { margin-left: 30px; }

        .category-item.active a { 
            color: #4285f4;
            font-weight: bold;
        }

        .sort-options {
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 15px;
        }

        .sort-options label {
            margin-right: 10px;
            font-weight: bold;
        }

        .sort-options select {
            padding: 8px;
            margin-right: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .product-table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
        }

        .product-table th, .product-table td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        .product-table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }

        .product-table tr:hover {
            background-color: #f5f5f5;
        }

        .product-image {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 4px;
        }

        .detail-button {
            padding: 8px 16px;
            background-color: #4285f4;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .detail-button:hover {
            background-color: #3367d6;
        }

        .no-products {
            text-align: center;
            padding: 30px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            color: #666;
        }

        .search-result {
            margin-bottom: 20px;
            font-size: 16px;
            color: #666;
        }

        .search-keyword {
            font-weight: bold;
            color: #4285f4;
        }
    </style>
</head>
<body>
    <!-- Include header -->
    <jsp:include page="/WEB-INF/views/include/header.jsp" />

    <div class="container">
        <h2>상품 목록</h2>

<%
    // Get search keyword if any
    String keyword = (String) request.getAttribute("keyword");
    Integer selectedCategoryId = (Integer) request.getAttribute("selectedCategoryId");
%>

<div class="main-content">
    <!-- Category sidebar -->
    <div class="sidebar">
        <div class="category-title">카테고리</div>
        <%
            List<Category> categories = (List<Category>) request.getAttribute("categories");
            if (categories != null && !categories.isEmpty()) {
                for (Category category : categories) {
                    if (category.getUpperId() == null) { // Top level category
                        String activeClass = (selectedCategoryId != null && selectedCategoryId == category.getId()) ? "active" : "";
        %>
                        <div class="category-item level-1 <%= activeClass %>">
                            <a href="${pageContext.request.contextPath}/product/list.do?categoryId=<%= category.getId() %>">
                                <%= category.getName() %>
                            </a>
                        </div>
                        <%
                            // Display child categories
                            for (Category childCategory : categories) {
                                if (childCategory.getUpperId() != null && childCategory.getUpperId() == category.getId()) {
                                    activeClass = (selectedCategoryId != null && selectedCategoryId == childCategory.getId()) ? "active" : "";
                        %>
                                    <div class="category-item level-2 <%= activeClass %>">
                                        <a href="${pageContext.request.contextPath}/product/list.do?categoryId=<%= childCategory.getId() %>">
                                            <%= childCategory.getName() %>
                                        </a>
                                    </div>
                        <%
                                }
                            }
                        %>
        <%
                    }
                }
            } else {
        %>
                <div>카테고리가 없습니다.</div>
        <%
            }
        %>
    </div>

    <!-- Main content area -->
    <div class="content">
        <% if (keyword != null && !keyword.isEmpty()) { %>
            <div class="search-result">
                "<span class="search-keyword"><%= keyword %></span>" 검색 결과
            </div>
        <% } %>

        <div class="sort-options">
            <label for="sortBy">정렬:</label>
            <select id="sortBy" onchange="updateSort()">
                <option value="category" <%= "category".equals(request.getAttribute("sortBy")) ? "selected" : "" %>>카테고리명</option>
                <option value="price" <%= "price".equals(request.getAttribute("sortBy")) ? "selected" : "" %>>가격</option>
            </select>

            <select id="sortOrder" onchange="updateSort()">
                <option value="asc" <%= "asc".equals(request.getAttribute("sortOrder")) ? "selected" : "" %>>오름차순</option>
                <option value="desc" <%= "desc".equals(request.getAttribute("sortOrder")) ? "selected" : "" %>>내림차순</option>
            </select>
        </div>

<%
    List<Product> products = (List<Product>) request.getAttribute("products");
    if (products == null || products.isEmpty()) {
%>
    <div class="no-products">
        <p>상품이 없습니다.</p>
    </div>
<%
    } else {
%>
    <table class="product-table">
        <thead>
            <tr>
                <th>이미지</th>
                <th>상품명</th>
                <th>가격</th>
                <th>재고</th>
                <th>상세보기</th>
            </tr>
        </thead>
        <tbody>
        <%
            for (Product product : products) {
                String fileId = product.getId_file();
                String imagePath = "";

                if (fileId != null && !fileId.isEmpty()) {
                    ContentDao contentDao = new ContentDao();
                    Map<String, String> fileInfo = contentDao.getFileInfo(fileId);

                    if (!fileInfo.isEmpty()) {
                        String filePath = fileInfo.get("filePath");
                        String saveName = fileInfo.get("saveName");

                        if (filePath != null && saveName != null) {
                            imagePath = request.getContextPath() + "/content/image.jsp?fileId=" + fileId;
                        }
                    }
                }
        %>
            <tr>
                <td>
                <% if (!imagePath.isEmpty()) { %>
                    <img src="<%= imagePath %>" alt="<%= product.getNm_product() %>" class="product-image">
                <% } else { %>
                    <div class="product-image" style="background-color: #eee; display: flex; align-items: center; justify-content: center;">
                        No Image
                    </div>
                <% } %>
                </td>
                <td><%= product.getNm_product() %></td>
                <td><%= product.getQt_sale_price() %>원</td>
                <td><%= product.getQt_stock() %></td>
                <td>
                    <button class="detail-button" onclick="location.href='${pageContext.request.contextPath}/product/detail.do?productId=<%= product.getNo_product() %>'">
                        상세 보기
                    </button>
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
    </div><!-- End of content -->
</div><!-- End of main-content -->
</div><!-- End of container -->

<script>
    function updateSort() {
        const sortBy = document.getElementById('sortBy').value;
        const sortOrder = document.getElementById('sortOrder').value;
        const currentUrl = new URL(window.location.href);

        // Update or add sort parameters
        currentUrl.searchParams.set('sortBy', sortBy);
        currentUrl.searchParams.set('sortOrder', sortOrder);

        // Preserve category and keyword parameters
        const categoryId = currentUrl.searchParams.get('categoryId');
        const keyword = currentUrl.searchParams.get('keyword');

        if (categoryId) {
            currentUrl.searchParams.set('categoryId', categoryId);
        }

        if (keyword) {
            currentUrl.searchParams.set('keyword', keyword);
        }

        // Navigate to the updated URL
        window.location.href = currentUrl.toString();
    }
</script>
</body>
</html>