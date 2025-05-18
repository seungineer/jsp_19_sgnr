<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.CategoryDao" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Category" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="java.util.*" %>

<style>
.switch {
  position: relative;
  display: inline-block;
  width: 52px;
  height: 28px;
}
.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}
.slider {
  position: absolute;
  cursor: pointer;
  top: 0; left: 0; right: 0; bottom: 0;
  background-color: #ccc;
  transition: .4s;
  border-radius: 28px;
  font-size: 10px;
  color: white;
  text-align: center;
  line-height: 28px;
}
.slider:before {
  position: absolute;
  content: "";
  height: 20px; width: 20px;
  left: 4px; bottom: 4px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
  z-index: 2;
}
.slider:after {
  content: "비활성";
  position: absolute;
  left: 23px;
  right: 0;
  z-index: 1;
}
input:checked + .slider {
  background-color: #4285f4;
}
input:checked + .slider:before {
  transform: translateX(24px);
}
input:checked + .slider:after {
  content: "활성";
  left: 0;
  right: 19px;
}
</style>
<%
    Member member = (Member) session.getAttribute("member");
    if (member == null || !"20".equals(member.getUserType())) {
        response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
        return;
    }

    CategoryDao categoryDao = new CategoryDao();
    List<Category> categories = categoryDao.findAll();

    Map<Integer, List<Category>> levelMap = new HashMap<>();
    Map<Integer, List<Category>> parentChildMap = new HashMap<>();

    for (Category category : categories) {
        int level = category.getLevel();
        Integer parentId = category.getUpperId();

        if (!levelMap.containsKey(level)) {
            levelMap.put(level, new ArrayList<>());
        }
        levelMap.get(level).add(category);

        if (parentId != null) {
            if (!parentChildMap.containsKey(parentId)) {
                parentChildMap.put(parentId, new ArrayList<>());
            }
            parentChildMap.get(parentId).add(category);
        }
    }

    for (List<Category> categoryList : levelMap.values()) {
        categoryList.sort(Comparator.comparing(Category::getOrder));
    }

    for (List<Category> children : parentChildMap.values()) {
        children.sort(Comparator.comparing(Category::getOrder));
    }

    String status = request.getParameter("status");
    String message = request.getParameter("message");
%>

<h3>카테고리 관리</h3>
<p>카테고리 이름을 수정하거나 삭제, 활성화/비활성화할 수 있습니다.</p>

<div style="margin-top: 20px;">
    <!-- 카테고리 계층 구조 표시 -->
    <div style="border: 1px solid #ddd; padding: 15px; border-radius: 5px;">
        <h4>카테고리 목록</h4>
        <div style="max-height: 600px; overflow-y: auto;">
            <table style="width: 100%; border-collapse: collapse;">
                <thead>
                    <tr style="background-color: #f0f0f0;">
                        <th style="padding: 8px; text-align: left; border-bottom: 1px solid #ddd;">카테고리 이름</th>
                        <th style="padding: 8px; text-align: center; border-bottom: 1px solid #ddd; width: 180px;">관리</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    List<Category> topCategories = levelMap.getOrDefault(1, new ArrayList<>());

                    for (Category topCategory : topCategories) {
                    %>
                        <tr>
                            <td style="padding: 8px; border-bottom: 1px solid #eee;">
                                <form id="form-<%= topCategory.getId() %>" action="<%= request.getContextPath() %>/admin/category/manage.do" method="post" style="margin: 0;">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="categoryId" value="<%= topCategory.getId() %>">
                                    <input type="text" name="categoryName" value="<%= topCategory.getName() %>" 
                                           style="width: 90%; padding: 5px; border: 1px solid #ddd; border-radius: 3px;">
                                </form>
                            </td>
                            <td style="padding: 8px; border-bottom: 1px solid #eee; text-align: center;">
                                <button onclick="submitForm(<%= topCategory.getId() %>)" 
                                        style="padding: 3px 8px; background-color: #4285f4; color: white; border: none; border-radius: 3px; cursor: pointer; margin-right: 5px;">
                                    수정
                                </button>
                                <button onclick="deleteCategory(<%= topCategory.getId() %>)" 
                                        style="padding: 3px 8px; background-color: #ff6b6b; color: white; border: none; border-radius: 3px; cursor: pointer; margin-right: 5px;">
                                    삭제
                                </button>
                                <label class="switch">
                                    <input type="checkbox" onchange="toggleCategoryStatus(<%= topCategory.getId() %>)"
                                           <%= "Y".equals(topCategory.getYnUse()) ? "checked" : "" %>>
                                    <span class="slider"></span>
                                </label>
                            </td>
                        </tr>
                    <%
                        List<Category> midCategories = parentChildMap.getOrDefault(topCategory.getId(), new ArrayList<>());
                        for (Category midCategory : midCategories) {
                    %>
                        <tr>
                            <td style="padding: 8px; border-bottom: 1px solid #eee;">
                                <form id="form-<%= midCategory.getId() %>" action="<%= request.getContextPath() %>/admin/category/manage.do" method="post" style="margin: 0;">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="categoryId" value="<%= midCategory.getId() %>">
                                    <span style="margin-left: 20px;">└</span>
                                    <input type="text" name="categoryName" value="<%= midCategory.getName() %>" 
                                           style="width: 85%; padding: 5px; border: 1px solid #ddd; border-radius: 3px;">
                                </form>
                            </td>
                            <td style="padding: 8px; border-bottom: 1px solid #eee; text-align: center;">
                                <button onclick="submitForm(<%= midCategory.getId() %>)" 
                                        style="padding: 3px 8px; background-color: #4285f4; color: white; border: none; border-radius: 3px; cursor: pointer; margin-right: 5px;">
                                    수정
                                </button>
                                <button onclick="deleteCategory(<%= midCategory.getId() %>)" 
                                        style="padding: 3px 8px; background-color: #ff6b6b; color: white; border: none; border-radius: 3px; cursor: pointer; margin-right: 5px;">
                                    삭제
                                </button>
                                <label class="switch">
                                    <input type="checkbox" onchange="toggleCategoryStatus(<%= midCategory.getId() %>)"
                                           <%= "Y".equals(midCategory.getYnUse()) ? "checked" : "" %>>
                                    <span class="slider"></span>
                                </label>
                            </td>
                        </tr>
                    <%
                            List<Category> subCategories = parentChildMap.getOrDefault(midCategory.getId(), new ArrayList<>());
                            for (Category subCategory : subCategories) {
                    %>
                        <tr>
                            <td style="padding: 8px; border-bottom: 1px solid #eee;">
                                <form id="form-<%= subCategory.getId() %>" action="<%= request.getContextPath() %>/admin/category/manage.do" method="post" style="margin: 0;">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="categoryId" value="<%= subCategory.getId() %>">
                                    <span style="margin-left: 40px;">└</span>
                                    <input type="text" name="categoryName" value="<%= subCategory.getName() %>" 
                                           style="width: 80%; padding: 5px; border: 1px solid #ddd; border-radius: 3px;">
                                </form>
                            </td>
                            <td style="padding: 8px; border-bottom: 1px solid #eee; text-align: center;">
                                <button onclick="submitForm(<%= subCategory.getId() %>)" 
                                        style="padding: 3px 8px; background-color: #4285f4; color: white; border: none; border-radius: 3px; cursor: pointer; margin-right: 5px;">
                                    수정
                                </button>
                                <button onclick="deleteCategory(<%= subCategory.getId() %>)" 
                                        style="padding: 3px 8px; background-color: #ff6b6b; color: white; border: none; border-radius: 3px; cursor: pointer; margin-right: 5px;">
                                    삭제
                                </button>
                                <label class="switch">
                                    <input type="checkbox" onchange="toggleCategoryStatus(<%= subCategory.getId() %>)"
                                           <%= "Y".equals(subCategory.getYnUse()) ? "checked" : "" %>>
                                    <span class="slider"></span>
                                </label>
                            </td>
                        </tr>
                    <%
                            }
                        }
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<form id="deleteForm" action="<%= request.getContextPath() %>/admin/category/manage.do" method="post" style="display: none;">
    <input type="hidden" name="action" value="delete">
    <input type="hidden" name="categoryId" id="deleteCategoryId">
</form>

<form id="toggleForm" action="<%= request.getContextPath() %>/admin/category/manage.do" method="post" style="display: none;">
    <input type="hidden" name="action" value="toggle">
    <input type="hidden" name="categoryId" id="toggleCategoryId">
</form>

<script>
    function submitForm(categoryId) {
        document.getElementById('form-' + categoryId).submit();
    }

    function deleteCategory(categoryId) {
        if (confirm('정말로 이 카테고리를 삭제하시겠습니까?')) {
            document.getElementById('deleteCategoryId').value = categoryId;
            document.getElementById('deleteForm').submit();
        }
    }

    function toggleCategoryStatus(categoryId) {
        document.getElementById('toggleCategoryId').value = categoryId;
        document.getElementById('toggleForm').submit();
    }

    const status = "<%= status %>";
    const message = "<%= message %>";

    if (status === "success") {
        alert("✅ 카테고리가 성공적으로 처리되었습니다.");
    } else if (status === "fail") {
        if (message) {
            alert("❌ " + message);
        } else {
            alert("❌ 카테고리 처리에 실패했습니다. 다시 시도해주세요.");
        }
    }
</script>
