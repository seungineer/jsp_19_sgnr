<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.ProductDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Product" %>
<%@ page import="java.util.List" %>
<%
    ProductDao productDao = new ProductDao();

    // Pagination parameters
    int currentPage = 1;
    int pageSize = 10;

    // Get page from request parameter
    String pageParam = request.getParameter("page");
    if (pageParam != null && !pageParam.isEmpty()) {
        try {
            currentPage = Integer.parseInt(pageParam);
            if (currentPage < 1) currentPage = 1;
        } catch (NumberFormatException e) {
            // If invalid, default to page 1
            currentPage = 1;
        }
    }

    // Get total count and calculate total pages
    int totalProducts = productDao.getTotalProductCount();
    int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

    // Ensure current page is not greater than total pages
    if (totalPages > 0 && currentPage > totalPages) {
        currentPage = totalPages;
    }

    // Get paginated list of products
    List<Product> products = productDao.getPaginatedProducts(currentPage, pageSize);
    String status = request.getParameter("status");
%>
<style>
    body {
        font-family: 'Noto Sans KR', sans-serif;
        background-color: #f5f5f5;
        margin: 0;
        padding: 20px;
    }

    h3 {
        margin-bottom: 20px;
    }

    p {
        color: #555;
        margin-bottom: 30px;
    }

    form {
        background-color: #fff;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        max-width: 100%;
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
        background-color: #f9f9f9;
    }

    .editable-field {
        width: 95%;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }

    .image-upload {
        padding: 6px;
    }

    .modified {
        background-color: #e6ffe6;
    }

    .save-button {
        padding: 10px 18px;
        background-color: #4285f4;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 14px;
    }

    .save-button:hover {
        background-color: #3367d6;
    }

    .delete-button {
        padding: 6px 12px;
        background-color: #f44336;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 13px;
    }

    .delete-button:hover {
        background-color: #d32f2f;
    }

    .delete-button:disabled {
        background-color: #ffcccc;
        color: #666666;
        cursor: not-allowed;
    }

    .cancel-delete-button {
        padding: 6px 12px;
        background-color: #9e9e9e;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 13px;
    }

    .cancel-delete-button:hover {
        background-color: #757575;
    }

    small {
        color: #888;
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


<h3>상품 수정</h3>
<p>아래 표에서 상품 정보를 수정할 수 있습니다. 변경된 항목은 연두색으로 표시됩니다.</p>

<form action="<%= request.getContextPath() %>/admin/product/modify.do" method="post" enctype="multipart/form-data" id="modifyForm">
    <input type="hidden" name="page" value="<%= currentPage %>">
    <div style="text-align: right; margin-bottom: 10px;">
        <button type="submit" class="save-button">변경사항 저장</button>
    </div>

    <table border="1" style="width: 100%; border-collapse: collapse;">
        <thead>
            <tr>
                <th>이미지</th>
                <th>상품코드</th>
                <th>상품명</th>
                <th>판매가격</th>
                <th>재고</th>
                <th>판매상태</th>
                <th>이미지 변경</th>
                <th>삭제</th>
            </tr>
        </thead>
        <tbody>
            <% for(Product product : products) { %>
            <tr>
                <td style="text-align: center; width: 120px;">
                    <% if (product.getId_file() != null && !product.getId_file().isEmpty()) { %>
                        <img src="<%= request.getContextPath() %>/content/image.jsp?id=<%= product.getId_file() %>" 
                             style="max-width: 100px; max-height: 100px;"><br>
                        <small>기존 이미지</small>
                    <% } else { %>
                        <div style="width: 100px; height: 100px; background-color: #f0f0f0; display: flex; align-items: center; justify-content: center;">
                            <small>이미지 없음</small>
                        </div>
                    <% } %>
                </td>
                <td>
                    <%= product.getNo_product() %>
                    <input type="hidden" name="no_product_<%= product.getNo_product() %>" value="<%= product.getNo_product() %>">
                </td>
                <td>
                    <input type="text" name="nm_product_<%= product.getNo_product() %>" 
                           value="<%= product.getNm_product() %>" class="editable-field">
                </td>
                <td>
                    <input type="number" name="qt_sale_price_<%= product.getNo_product() %>" 
                           value="<%= product.getQt_sale_price() %>" class="editable-field"> 원
                </td>
                <td>
                    <input type="number" name="qt_stock_<%= product.getNo_product() %>" 
                           value="<%= product.getQt_stock() %>" class="editable-field"> 개
                </td>
                <td>
                    <select name="sale_status_<%= product.getNo_product() %>" class="editable-field">
                        <option value="1" <%= product.getSale_status() == 1 ? "selected" : "" %>>판매중</option>
                        <option value="0" <%= product.getSale_status() == 0 ? "selected" : "" %>>판매중지</option>
                    </select>
                </td>
                <td>
                    <input type="file" name="newImage_<%= product.getNo_product() %>" 
                           accept="image/*" class="image-upload">
                    <input type="hidden" name="modified_<%= product.getNo_product() %>" value="false" class="modified-flag">
                    <input type="hidden" name="id_file_<%= product.getNo_product() %>" value="<%= product.getId_file() %>">
                </td>
                <td style="text-align: center;">
                    <button type="button" class="delete-button" data-product-id="<%= product.getNo_product() %>">삭제</button>
                    <input type="hidden" name="delete_<%= product.getNo_product() %>" value="false" class="delete-flag">
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
</form>

<style>
    table {
        margin-top: 20px;
    }
    th, td {
        padding: 8px;
    }
    .editable-field {
        width: 90%;
    }
    .modified {
        background-color: #e6ffe6;
    }
    .save-button {
        padding: 8px 16px;
        background-color: #4285f4;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    .save-button:hover {
        background-color: #3367d6;
    }
    .delete-button {
        padding: 5px 10px;
        background-color: #f44336;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    .delete-button:hover {
        background-color: #d32f2f;
    }
    .delete-button:disabled {
        background-color: #ffcccc;
        color: #666666;
        cursor: not-allowed;
    }
    .cancel-delete-button {
        padding: 5px 10px;
        background-color: #9e9e9e;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    .cancel-delete-button:hover {
        background-color: #757575;
    }
</style>

<script>
    // Track modified fields
    document.querySelectorAll('.editable-field').forEach(field => {
        field.addEventListener('change', function() {
            this.classList.add('modified');

            // Get the product code from the field name
            const fieldName = this.name;
            // Extract product code by removing the known prefixes
            let productCode = fieldName;
            const knownPrefixes = ["nm_product_", "qt_sale_price_", "qt_stock_", "sale_status_"];
            for (const prefix of knownPrefixes) {
                if (fieldName.startsWith(prefix)) {
                    productCode = fieldName.substring(prefix.length);
                    break;
                }
            }


            // Set the modified flag for this product
            const modifiedFlag = document.querySelector("input[name=\"modified_" + productCode + "\"]");
            if (modifiedFlag) {
                modifiedFlag.value = "true";
            } else {
                // List all modified flags for debugging
                const allFlags = document.querySelectorAll('.modified-flag');
            }
        });
    });

    // Handle image uploads
    document.querySelectorAll('.image-upload').forEach(fileInput => {
        fileInput.addEventListener('change', function() {
            if (this.files && this.files[0]) {
                // Get the product code from the field name
                const fieldName = this.name;
                // Extract product code by removing the known prefixes
                let productCode = fieldName;
                const knownPrefixes = ["newImage_"];
                for (const prefix of knownPrefixes) {
                    if (fieldName.startsWith(prefix)) {
                        productCode = fieldName.substring(prefix.length);
                        break;
                    }
                }


                // Set the modified flag for this product
                const modifiedFlag = document.querySelector("input[name=\"modified_" + productCode + "\"]");
                if (modifiedFlag) {
                    modifiedFlag.value = "true";
                } else {
                    // List all modified flags for debugging
                    const allFlags = document.querySelectorAll('.modified-flag');
                }

                // Show image preview
                const reader = new FileReader();
                reader.onload = function(e) {
                    const row = fileInput.closest('tr');
                    const imgCell = row.querySelector('td:first-child');

                    // Check if there's an existing image or placeholder
                    const imgElement = imgCell.querySelector('img');
                    if (imgElement) {
                        // Update existing image
                        imgElement.src = e.target.result;
                    } else {
                        // Replace placeholder with new image
                        imgCell.innerHTML = `
                            <img src="${e.target.result}" style="max-width: 100px; max-height: 100px;"><br>
                            <small>새 이미지</small>
                        `;
                    }
                };
                reader.readAsDataURL(this.files[0]);
            }
        });
    });

    // Form submission - only include modified products
    document.getElementById('modifyForm').addEventListener('submit', function(e) {
        e.preventDefault();

        // Check for delete flags
        const deleteFlags = document.querySelectorAll('input[name^="delete_"][value="true"]');

        // Check if any products were modified (either by flag or by modified class)
        const modifiedFlags = document.querySelectorAll('.modified-flag[value="true"]');
        const modifiedFields = document.querySelectorAll('.editable-field.modified');

        if (modifiedFlags.length === 0 && modifiedFields.length === 0 && deleteFlags.length === 0) {
            alert('변경된 상품이 없습니다.');
            return;
        }

        // If we have modified fields but no flags set, set the flags now
        if (modifiedFlags.length === 0 && modifiedFields.length > 0) {
            modifiedFields.forEach(field => {
                const fieldName = field.name;
                // Extract product code by removing the known prefixes
                let productCode = fieldName;
                const knownPrefixes = ["nm_product_", "qt_sale_price_", "qt_stock_", "sale_status_"];
                for (const prefix of knownPrefixes) {
                    if (fieldName.startsWith(prefix)) {
                        productCode = fieldName.substring(prefix.length);
                        break;
                    }
                }

                const modifiedFlag = document.querySelector("input[name=\"modified_" + productCode + "\"]");
                if (modifiedFlag) {
                    modifiedFlag.value = "true";
                } else {
                    // List all modified flags for debugging
                    const allFlags = document.querySelectorAll('.modified-flag');
                }
            });
        }

        this.submit();
    });

    // Handle delete buttons
    document.querySelectorAll('.delete-button').forEach(button => {
        button.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');

            // Find the product name input within the same row as the delete button
            const row = this.closest('tr');
            const productNameInput = row.querySelector('input[name="nm_product_'+productId+'"]');

            const productName = productNameInput ? productNameInput.value : "선택한 상품";

            if (confirm('정말로 상품 '+productName+'을(를) 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.')) {

                // Set the delete flag for this product (find it within the same row)
                const deleteFlag = row.querySelector(`.delete-flag`);

                if (deleteFlag) {
                    deleteFlag.value = "true";

                    // Visually indicate that the product will be deleted
                    row.style.backgroundColor = '#ffdddd';
                    row.style.textDecoration = 'line-through';

                    // Create a hidden input for the product name to ensure it's submitted
                    // We already have productNameInput from earlier

                    if (productNameInput) {
                        const hiddenNameInput = document.createElement('input');
                        hiddenNameInput.type = 'hidden';
                        hiddenNameInput.name = `nm_product_${productId}`;
                        hiddenNameInput.value = productNameInput.value;
                        row.appendChild(hiddenNameInput);
                    }

                    // Disable inputs for this product
                    row.querySelectorAll('input, select').forEach(input => {
                        if (!input.name.startsWith('delete_')) {
                            input.disabled = true;
                        }
                    });

                    // Change button text and disable it
                    this.textContent = '삭제 예정';
                    this.disabled = true;

                    // Add a cancel button
                    const cancelButton = document.createElement('button');
                    cancelButton.type = 'button';
                    cancelButton.textContent = '취소';
                    cancelButton.className = 'cancel-delete-button';
                    cancelButton.style.marginLeft = '5px';
                    this.parentNode.appendChild(cancelButton);

                    // Handle cancel button click
                    cancelButton.addEventListener('click', function() {
                        deleteFlag.value = "false";
                        row.style.backgroundColor = '';
                        row.style.textDecoration = '';

                        // Re-enable inputs
                        row.querySelectorAll('input, select').forEach(input => {
                            input.disabled = false;
                        });

                        // Restore delete button
                        button.textContent = '삭제';
                        button.disabled = false;

                        // Remove cancel button
                        this.remove();
                    });
                }
            }
        });
    });

    // Show status messages
    const status = "<%= status %>";
    if (status === "success") {
        alert("✅ 상품이 성공적으로 수정/삭제되었습니다.");
    } else if (status === "fail") {
        alert("❌ 상품 수정/삭제에 실패했습니다. 다시 시도해주세요.");
    }
</script>

<!-- Pagination Controls -->
<div class="pagination-container">
    <div class="pagination-controls">
        <% if (currentPage > 1) { %>
            <a href="?menu=productModify&page=<%= currentPage - 1 %>">이전</a>
        <% } else { %>
            <span class="disabled">이전</span>
        <% } %>

        <% 
        // Display page numbers
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        for (int i = startPage; i <= endPage; i++) { 
        %>
            <% if (i == currentPage) { %>
                <span class="current-page"><%= i %></span>
            <% } else { %>
                <a href="?menu=productModify&page=<%= i %>"><%= i %></a>
            <% } %>
        <% } %>

        <% if (currentPage < totalPages) { %>
            <a href="?menu=productModify&page=<%= currentPage + 1 %>">다음</a>
        <% } else { %>
            <span class="disabled">다음</span>
        <% } %>
    </div>
</div>

<!-- Pagination Info -->
<div class="pagination-info">
    총 <%= totalProducts %>개의 상품 (페이지 <%= currentPage %> / <%= totalPages %>)
</div>
