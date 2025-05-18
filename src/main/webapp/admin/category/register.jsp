<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.CategoryDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%
  CategoryDao categoryDao = new CategoryDao();
  List<Category> allCategories = categoryDao.findAll();
  Member loggedInMember = (Member) session.getAttribute("member");
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
    max-width: 700px;
  }

  label {
    display: block;
    margin-bottom: 6px;
    font-weight: bold;
  }

  input[type="text"],
  select {
    width: 100%;
    padding: 10px;
    margin-bottom: 20px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
    box-sizing: border-box;
  }

  button[type="submit"] {
    padding: 10px 20px;
    background-color: #4285f4;
    color: #fff;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
  }

  button[type="submit"]:hover:enabled {
    background-color: #3367d6;
  }

  button[disabled] {
    background-color: #ccc;
    cursor: not-allowed;
  }

  #upper1-container,
  #upper2-container {
    margin-bottom: 20px;
  }

  small {
    color: #888;
  }
</style>

<h3>카테고리 등록</h3>
<p>카테고리 레벨 2단계 이상부터는 상위 카테고리를 선택해야 합니다.</p>

<form action="<%= request.getContextPath() %>/admin/category/register.do" method="post" onsubmit="return validateForm()">
  <label for="level">카테고리 레벨</label><br>
  <select name="level" id="level" required>
    <option value="">-- 선택 --</option>
    <option value="1">1단계</option>
    <option value="2">2단계</option>
    <option value="3">3단계</option>
  </select><br><br>

  <div id="upper1-container" style="display:none;">
    <label for="upper1">1단계 카테고리</label><br>
    <select id="upper1">
      <option value="">-- 선택 --</option>
      <% for (Category c : allCategories) {
        if (c.getLevel() == 1) { %>
      <option value="<%= c.getId() %>"><%= c.getName() %></option>
      <% } } %>
    </select><br><br>
  </div>

  <div id="upper2-container" style="display:none;">
    <label for="upper2">2단계 카테고리</label><br>
    <select id="upper2">
      <option value="">-- 선택 --</option>
    </select><br><br>
  </div>

  <label for="name">카테고리 이름</label><br>
  <input type="text" name="name" id="name" required><br><br>

  <input type="hidden" name="upperId" id="upperId">
  <input type="hidden" name="fullName" id="fullName">
  <input type="hidden" name="regName" value="<%= loggedInMember.getName() %>" />

  <button id="submitBtn" type="submit" disabled>등록하기</button>
</form>

<%
  String status = request.getParameter("status");
%>

<script>
  <% if ("success".equals(status)) { %>
  alert("카테고리가 성공적으로 등록되었습니다.");
  history.replaceState({}, "", location.pathname); // URL 파라미터 제거
  <% } else if ("fail".equals(status)) { %>
  alert("카테고리 등록에 실패했습니다. 다시 시도해주세요.");
  history.replaceState({}, "", location.pathname); // URL 파라미터 제거
  <% } %>
</script>


<script>
  const allCategories = [
    <% for (int i = 0; i < allCategories.size(); i++) {
         Category c = allCategories.get(i);
         String comma = (i < allCategories.size() - 1) ? "," : "";
    %>
    {
      id: <%= c.getId() %>,
      name: "<%= c.getName() %>",
      level: <%= c.getLevel() %>,
      upperId: <%= c.getUpperId() == null ? "null" : c.getUpperId() %>
    }<%= comma %>
    <% } %>
  ];
  const levelSelect = document.getElementById("level");
  const upper1 = document.getElementById("upper1");
  const upper2 = document.getElementById("upper2");
  const nameInput = document.getElementById("name");
  const submitBtn = document.getElementById("submitBtn");
  const upperIdHidden = document.getElementById("upperId");
  const fullNameHidden = document.getElementById("fullName");

  levelSelect.addEventListener("change", handleLevelChange);
  upper1.addEventListener("change", handleUpper1Change);
  nameInput.addEventListener("input", checkValid);
  upper2.addEventListener("change", checkValid);

  function handleLevelChange() {
    const level = levelSelect.value;
    document.getElementById("upper1-container").style.display = "none";
    document.getElementById("upper2-container").style.display = "none";

    if (level === "2") {
      document.getElementById("upper1-container").style.display = "block";
    } else if (level === "3") {
      document.getElementById("upper1-container").style.display = "block";
      document.getElementById("upper2-container").style.display = "block";
    }

    checkValid();
  }

  function handleUpper1Change() {
    upper2.innerHTML = '<option value="">-- 선택 --</option>';
    const selectedId = upper1.value;

    allCategories.forEach(cat => {
      if (cat.level === 2 && cat.upperId === parseInt(selectedId)) {
        const opt = document.createElement("option");
        opt.value = cat.id;
        opt.textContent = cat.name;
        upper2.appendChild(opt);
      }
    });

    checkValid();
  }

  function checkValid() {
    const level = parseInt(levelSelect.value);
    const name = nameInput.value.trim();

    let isValid = false;
    let full = "", upperId = "";

    if (level === 1 && name) {
      full = name;
      isValid = true;
    } else if (level === 2 && upper1.value && name) {
      full = upper1.options[upper1.selectedIndex].text + " > " + name;
      upperId = upper1.value;
      isValid = true;
    } else if (level === 3 && upper1.value && upper2.value && name) {
      full = upper1.options[upper1.selectedIndex].text + " > " + upper2.options[upper2.selectedIndex].text + " > " + name;
      upperId = upper2.value;
      isValid = true;
    }

    submitBtn.disabled = !isValid;
    if (isValid) {
      fullNameHidden.value = full;
      upperIdHidden.value = upperId;
    }
  }

  function validateForm() {
    return !submitBtn.disabled;
  }
</script>
