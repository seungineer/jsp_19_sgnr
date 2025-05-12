<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.CategoryDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Category" %>
<%@ page import="java.util.List" %>
<%
    CategoryDao categoryDao = new CategoryDao();
    List<Category> categories = categoryDao.findLeafCategories();
    String status = request.getParameter("status");
%>

<h3>상품 등록</h3>
<p>아래 양식을 통해 새로운 상품을 등록할 수 있습니다.</p>

<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%
    Member member = (Member) session.getAttribute("member");
%>
<form action="<%= request.getContextPath() %>/admin/product/register.do" method="post">
    <label>상품명</label><br>
    <input type="text" name="nm_product" required><br><br>

    <label>상세 설명</label><br>
    <textarea name="nm_detail_explain" rows="4" cols="50"></textarea><br><br>

    <label>판매 시작일</label><br>
    <input type="date" id="startDate" required>
    <input type="hidden" name="dt_start_date" id="formattedStartDate"><br><br>

    <label>판매 종료일</label><br>
    <input type="date" id="endDate" required>
    <input type="hidden" name="dt_end_date" id="formattedEndDate"><br><br>

    <label>소비자 가격</label><br>
    <input type="number" name="qt_customer" required> 원<br><br>

    <label>판매 가격</label><br>
    <input type="number" name="qt_sale_price" required> 원<br><br>

    <label>재고 수량</label><br>
    <input type="number" name="qt_stock" required> 개<br><br>

    <label>배송비</label><br>
    <input type="number" name="qt_delivery_fee" required> 원<br><br>

    <label>즉시 판매 시작</label><br>
    <input type="radio" name="sale_status" value="1" checked> 예
    <input type="radio" name="sale_status" value="0"> 아니오<br><br>

    <input type="hidden" name="no_register" value="<%= member.getName() %>">

    <br><br>
    <button type="submit">상품 등록</button>
</form>

<script>
    const startDateInput = document.getElementById("startDate");
    const formattedStart = document.getElementById("formattedStartDate");
    const endDateInput = document.getElementById("endDate");
    const formattedEnd = document.getElementById("formattedEndDate");

    function formatDate(input) {
        return input.value.replaceAll("-", "");  // "2024-01-01" → "20240101"
    }

    startDateInput.addEventListener("change", () => {
        formattedStart.value = formatDate(startDateInput);
        console.log("formatted start: ", startDateInput, formattedStart.value)
    });

    endDateInput.addEventListener("change", () => {
        formattedEnd.value = formatDate(endDateInput);
        console.log("formattedend: ", endDateInput, formattedEnd.value)
    });

    const status = "<%= status %>";
    if (status === "success") {
        alert("✅ 상품이 성공적으로 등록되었습니다.");
    } else if (status === "fail") {
        alert("❌ 상품 등록에 실패했습니다. 다시 시도해주세요.");
    }
</script>
