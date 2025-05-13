<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.ProductDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Product" %>
<%@ page import="java.util.List" %>
<%
    ProductDao productDao = new ProductDao();
    List<Product> products = productDao.getAllProducts();
    String status = request.getParameter("status");
%>

<h3>상품 수정</h3>
<p>아래 표에서 상품 정보를 수정할 수 있습니다. 변경된 항목은 연두색으로 표시됩니다.</p>

<form action="<%= request.getContextPath() %>/admin/product/modify.do" method="post" enctype="multipart/form-data" id="modifyForm">
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

            console.log("Field name:", fieldName);
            console.log("Extracted product code:", productCode);
            console.log("Looking for:", "input[name=\"modified_" + productCode + "\"]");

            // Set the modified flag for this product
            const modifiedFlag = document.querySelector("input[name=\"modified_" + productCode + "\"]");
            if (modifiedFlag) {
                modifiedFlag.value = "true";
                console.log("Modified flag found and set to true");
            } else {
                console.error(`Could not find modified flag for product ${productCode}`);
                // List all modified flags for debugging
                const allFlags = document.querySelectorAll('.modified-flag');
                console.log("All modified flags:", Array.from(allFlags).map(f => f.getAttribute('name')));
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

                console.log("Image field name:", fieldName);
                console.log("Extracted product code:", productCode);
                console.log("Looking for:", "input[name=\"modified_" + productCode + "\"]");

                // Set the modified flag for this product
                const modifiedFlag = document.querySelector("input[name=\"modified_" + productCode + "\"]");
                if (modifiedFlag) {
                    modifiedFlag.value = "true";
                    console.log("Modified flag found and set to true");
                } else {
                    console.error(`Could not find modified flag for product ${productCode}`);
                    // List all modified flags for debugging
                    const allFlags = document.querySelectorAll('.modified-flag');
                    console.log("All modified flags:", Array.from(allFlags).map(f => f.getAttribute('name')));
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

        // Check if any products were modified (either by flag or by modified class)
        const modifiedFlags = document.querySelectorAll('.modified-flag[value="true"]');
        const modifiedFields = document.querySelectorAll('.editable-field.modified');

        if (modifiedFlags.length === 0 && modifiedFields.length === 0) {
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

                console.log("Form field name:", fieldName);
                console.log("Extracted product code:", productCode);
                console.log("Looking for:", "input[name=\"modified_" + productCode + "\"]");

                const modifiedFlag = document.querySelector("input[name=\"modified_" + productCode + "\"]");
                if (modifiedFlag) {
                    modifiedFlag.value = "true";
                    console.log("Modified flag found and set to true");
                } else {
                    console.error(`Could not find modified flag for product ${productCode}`);
                    // List all modified flags for debugging
                    const allFlags = document.querySelectorAll('.modified-flag');
                    console.log("All modified flags:", Array.from(allFlags).map(f => f.getAttribute('name')));
                }
            });
        }

        // Submit the form
        this.submit();
    });

    // Show status messages
    const status = "<%= status %>";
    if (status === "success") {
        alert("✅ 상품이 성공적으로 수정되었습니다.");
    } else if (status === "fail") {
        alert("❌ 상품 수정에 실패했습니다. 다시 시도해주세요.");
    }
</script>
