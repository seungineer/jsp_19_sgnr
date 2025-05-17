<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.CategoryDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.ProductDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Category" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Product" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="java.util.*" %>
<%
    // Check if user is logged in
    Member member = (Member) session.getAttribute("member");
    if (member == null) {
        response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
        return;
    }

    // Get all categories
    CategoryDao categoryDao = new CategoryDao();
    List<Category> categories = categoryDao.findAll();

    // Organize categories by level and parent for hierarchical display
    Map<Integer, List<Category>> levelMap = new HashMap<>();
    Map<Integer, List<Category>> parentChildMap = new HashMap<>();

    // Initialize maps
    for (Category category : categories) {
        int level = category.getLevel();
        Integer parentId = category.getUpperId();

        // Group by level
        if (!levelMap.containsKey(level)) {
            levelMap.put(level, new ArrayList<>());
        }
        levelMap.get(level).add(category);

        // Group by parent
        if (parentId != null) {
            if (!parentChildMap.containsKey(parentId)) {
                parentChildMap.put(parentId, new ArrayList<>());
            }
            parentChildMap.get(parentId).add(category);
        }
    }

    // Sort each level by order

    for (List<Category> categoryList : levelMap.values()) {
        categoryList.sort(Comparator.comparing(Category::getOrder));
    }

    // Sort each parent's children by order
    for (List<Category> children : parentChildMap.values()) {
        children.sort(Comparator.comparing(Category::getOrder));
    }

    // SQL equivalent of what we're doing:
    // SELECT * FROM TB_CATEGORY 
    // WHERE YN_USE = 'Y' AND YN_DELETE = 'N' 
    // ORDER BY cn_level ASC, cn_order ASC

    // Get selected category and its products
    String selectedCategoryIdStr = request.getParameter("categoryId");
    int selectedCategoryId = 0;
    List<Product> mappedProducts = new ArrayList<>();
    List<Product> unmappedProducts = new ArrayList<>();
    Category selectedCategory = null;

    if (selectedCategoryIdStr != null && !selectedCategoryIdStr.isEmpty()) {
        try {
            selectedCategoryId = Integer.parseInt(selectedCategoryIdStr);

            // Find the selected category first
            for (Category c : categories) {
                if (c.getId() == selectedCategoryId) {
                    selectedCategory = c;
                    break;
                }
            }

            // Only fetch products if it's a Level 3 category
            if (selectedCategory != null && selectedCategory.getLevel() == 3) {
                // Get products mapped to this category
                ProductDao productDao = new ProductDao();
                mappedProducts = productDao.findProductsByCategory(selectedCategoryId);

                // Get products not mapped to this category
                unmappedProducts = productDao.findUnmappedProducts(selectedCategoryId);
            }
        } catch (NumberFormatException e) {
            // Invalid category ID, ignore
        }
    }

    // Get status and message for alerts
    String status = request.getParameter("status");
    String message = request.getParameter("message");
%>

<h3>카테고리 매핑 관리</h3>
<p>카테고리를 선택하고 상품을 매핑하거나 해제할 수 있습니다.</p>

<div style="display: flex; gap: 20px; margin-top: 20px;">
    <!-- 왼쪽 컬럼: 카테고리 목록 (계층적 표시) -->
    <div style="flex: 1; border: 1px solid #ddd; padding: 15px; border-radius: 5px;">
        <h4>카테고리 목록</h4>
        <div style="max-height: 500px; overflow-y: scroll;">
            <%
            // Get level 1 categories (대분류)
            List<Category> topCategories = levelMap.getOrDefault(1, new ArrayList<>());

            // Display categories hierarchically
            for (Category topCategory : topCategories) {
                // Display top level category (대분류)
            %>
                <div style="margin-bottom: 8px; padding: 5px;">
                    <div style="margin-left:0px; color: #666;">
                        <%= topCategory.getName() %>
                    </div>
                </div>
            <%
                // Get and display children (중분류)
                List<Category> midCategories = parentChildMap.getOrDefault(topCategory.getId(), new ArrayList<>());
                for (Category midCategory : midCategories) {
            %>
                <div style="margin-bottom: 8px; padding: 5px;">
                    <div style="margin-left:20px; color: #666;">
                        - <%= midCategory.getName() %>
                    </div>
                </div>
            <%
                    // Get and display grandchildren (소분류)
                    List<Category> subCategories = parentChildMap.getOrDefault(midCategory.getId(), new ArrayList<>());
                    for (Category subCategory : subCategories) {
                        boolean isSelected = selectedCategoryId == subCategory.getId();
            %>
                <div style="margin-bottom: 8px; padding: 5px; <%= isSelected ? "background-color: #f0f0f0; font-weight: bold;" : "" %>">
                    <a href="../admin/admin.jsp?menu=categoryMapping&categoryId=<%= subCategory.getId() %>" 
                       style="text-decoration: none; margin-left:40px; color: <%= isSelected ? "#000" : "#0066cc" %>;">
                        &nbsp;&nbsp;- <%= subCategory.getName() %>
                    </a>
                </div>
            <%
                    }
                }
            }
            %>
        </div>
    </div>

    <!-- 오른쪽 컬럼: 선택된 카테고리의 상품 목록 및 추가 기능 -->
    <div style="flex: 2; border: 1px solid #ddd; padding: 15px; border-radius: 5px;">
        <% if (selectedCategoryId > 0 && selectedCategory != null) { %>
            <h4>카테고리: <%= selectedCategory.getName() %></h4>

            <% if (selectedCategory.getLevel() == 3) { %>
                <!-- 상품 추가 폼 -->
                <div style="margin-bottom: 20px; padding: 10px; background-color: #f9f9f9; border-radius: 5px;">
                    <h5>상품 추가하기</h5>
                    <% if (unmappedProducts.isEmpty()) { %>
                        <p>추가할 수 있는 상품이 없습니다.</p>
                    <% } else { %>
                        <form action="<%= request.getContextPath() %>/admin/product/category-mapping.do" method="post">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="categoryId" value="<%= selectedCategoryId %>">

                            <div style="display: flex; gap: 10px; align-items: center;">
                                <select name="productId" style="flex-grow: 1; padding: 5px;">
                                    <option value="">-- 상품을 선택하세요 --</option>
                                    <% for (Product product : unmappedProducts) { %>
                                        <option value="<%= product.getNo_product() %>">
                                            <%= product.getNm_product() %>
                                        </option>
                                    <% } %>
                                </select>
                                <button type="submit" style="padding: 5px 10px;">추가</button>
                            </div>
                        </form>
                    <% } %>
                </div>

                <!-- 매핑된 상품 목록 -->
                <h5>매핑된 상품 목록</h5>
                <% if (mappedProducts.isEmpty()) { %>
                    <p>현재 매핑된 상품이 없습니다.</p>
                <% } else { %>
                    <div style="max-height: 400px; overflow-y: auto;">
                        <table style="width: 100%; border-collapse: collapse;">
                            <thead>
                                <tr style="background-color: #f0f0f0;">
                                    <th style="padding: 8px; text-align: left; border-bottom: 1px solid #ddd;">상품 ID</th>
                                    <th style="padding: 8px; text-align: left; border-bottom: 1px solid #ddd;">상품명</th>
                                    <th style="padding: 8px; text-align: right; border-bottom: 1px solid #ddd;">판매가</th>
                                    <th style="padding: 8px; text-align: center; border-bottom: 1px solid #ddd;">작업</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Product product : mappedProducts) { %>
                                    <tr>
                                        <td style="padding: 8px; border-bottom: 1px solid #eee;"><%= product.getNo_product() %></td>
                                        <td style="padding: 8px; border-bottom: 1px solid #eee;"><%= product.getNm_product() %></td>
                                        <td style="padding: 8px; border-bottom: 1px solid #eee; text-align: right;"><%= product.getQt_sale_price() %>원</td>
                                        <td style="padding: 8px; border-bottom: 1px solid #eee; text-align: center;">
                                            <form action="<%= request.getContextPath() %>/admin/product/category-mapping.do" method="post" style="margin: 0;">
                                                <input type="hidden" name="action" value="remove">
                                                <input type="hidden" name="categoryId" value="<%= selectedCategoryId %>">
                                                <input type="hidden" name="productId" value="<%= product.getNo_product() %>">
                                                <button type="submit" style="padding: 3px 8px; background-color: #ff6b6b; color: white; border: none; border-radius: 3px; cursor: pointer;">삭제</button>
                                            </form>
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } %>
            <% } else { %>
                <div style="margin: 30px 0; padding: 20px; background-color: #fff3cd; border-radius: 5px; text-align: center;">
                    <p style="color: #856404; font-weight: bold;">Level 3 카테고리만 선택 가능합니다.</p>
                    <p>상품 매핑은 Level 3 카테고리(소분류)에서만 가능합니다.</p>
                </div>
            <% } %>
        <% } else { %>
            <div style="text-align: center; padding: 50px 0;">
                <p>왼쪽에서 카테고리를 선택하세요.</p>
            </div>
        <% } %>
    </div>
</div>

<script>
    const status = "<%= status %>";
    const message = "<%= message %>";

    if (status === "success") {
        alert("✅ 카테고리 매핑이 성공적으로 처리되었습니다.");
    } else if (status === "fail") {
        if (message) {
            alert("❌ " + message);
        } else {
            alert("❌ 카테고리 매핑 처리에 실패했습니다. 다시 시도해주세요.");
        }
    }
</script>
