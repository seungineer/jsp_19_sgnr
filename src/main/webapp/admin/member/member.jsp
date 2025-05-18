<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.MemberDao" %>
<%
    MemberDao memberDao = new MemberDao();

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
    int totalUsers = memberDao.countAllExceptAdmin();
    int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

    // Ensure current page is not greater than total pages
    if (totalPages > 0 && currentPage > totalPages) {
        currentPage = totalPages;
    }

    // Get paginated list of members
    List<Member> members = memberDao.getPaginatedMembersExceptAdmin(currentPage, pageSize);
%>
<style>
    table {
        width: 100%;
        border-collapse: collapse;
        font-family: 'Segoe UI', sans-serif;
    }

    thead {
        background-color: #e3f0fb;
    }

    th, td {
        padding: 12px;
        border: 1px solid #c0d7ec;
        text-align: center;
    }

    tr:hover {
        background-color: #f0f8ff;
    }

    input[type="text"], select {
        padding: 6px;
        width: 95%;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    .modified {
        background-color: #d1f0d1 !important;
    }

    button[type="submit"] {
        margin-top: 10px;
        padding: 10px 20px;
        background-color: #4285f4;
        border: none;
        color: white;
        border-radius: 6px;
        font-weight: bold;
        cursor: pointer;
    }

    button[type="submit"]:hover {
        background-color: #3367d6;
    }
</style>

<h3>회원 관리</h3>
<p>회원 정보를 아래 테이블에서 수정할 수 있습니다.</p>

<form action="<%= request.getContextPath() %>/admin/member/update.do" method="post">
    <input type="hidden" name="menu" value="member"/>
    <input type="hidden" name="page" value="<%= currentPage %>"/>
    <button style="display: flex; justify-self: end" type="submit">변경사항 저장</button>
    <table border="1" cellspacing="0" cellpadding="8" style="width: 100%; margin-top: 20px; border-collapse: collapse;">
        <thead>
        <tr style="background-color: #f0f0f0;">
            <th>ID</th>
            <th>이름</th>
            <th>이메일</th>
            <th>휴대폰번호</th>
            <th>계정상태</th>
            <th>회원유형</th>
        </tr>
        </thead>
        <tbody>
        <% for (Member m : members) { %>
        <tr>
            <td><%= m.getEmail() %></td>
            <td>
                <input type="text" name="name_<%= m.getEmail() %>" value="<%= m.getName() %>"
                       <%= "ST02".equals(m.getStatus()) ? "disabled" : "" %> />
            </td>
            <td><%= m.getEmail() %></td>
            <td>
                <input type="text" name="mobile_<%= m.getEmail() %>" value="<%= m.getPhone() %>"
                        <%= "ST02".equals(m.getStatus()) ? "disabled" : "" %> />
            </td>
            <td>
                <select name="status_<%= m.getEmail() %>" <%= "ST02".equals(m.getStatus()) ? "disabled" : "" %>>
                    <option value="ST00" <%= "ST00".equals(m.getStatus()) ? "selected" : "" %>>요청</option>
                    <option value="ST01" <%= "ST01".equals(m.getStatus()) ? "selected" : "" %>>정상</option>
                    <option value="ST02" <%= "ST02".equals(m.getStatus()) ? "selected" : "" %>>해지</option>
                    <option value="ST03" <%= "ST03".equals(m.getStatus()) ? "selected" : "" %>>일시정지</option>
                </select>
            </td>
            <td>
                <select name="type_<%= m.getEmail() %>" <%= "ST02".equals(m.getStatus()) ? "disabled" : "" %>>
                    <option value="10" <%= "10".equals(m.getUserType()) ? "selected" : "" %>>일반</option>
                    <option value="20" <%= "20".equals(m.getUserType()) ? "selected" : "" %>>관리자</option>
                </select>
            </td>
            <input type="hidden" name="idList" value="<%= m.getEmail() %>" />
        </tr>
        <% } %>
        </tbody>
    </table>
    <br>

    <!-- Pagination Controls -->
    <div style="display: flex; justify-content: center; margin-top: 20px;">
        <div style="display: flex; align-items: center;">
            <% if (currentPage > 1) { %>
                <a href="?menu=member&page=<%= currentPage - 1 %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;">이전</a>
            <% } else { %>
                <span style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; color: #ccc; border-radius: 3px;">이전</span>
            <% } %>

            <% 
            // Display page numbers
            int startPage = Math.max(1, currentPage - 2);
            int endPage = Math.min(totalPages, currentPage + 2);

            for (int i = startPage; i <= endPage; i++) { 
            %>
                <% if (i == currentPage) { %>
                    <span style="margin: 0 5px; padding: 5px 10px; background-color: #4285f4; color: white; border-radius: 3px;"><%= i %></span>
                <% } else { %>
                    <a href="?menu=member&page=<%= i %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;"><%= i %></a>
                <% } %>
            <% } %>

            <% if (currentPage < totalPages) { %>
                <a href="?menu=member&page=<%= currentPage + 1 %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;">다음</a>
            <% } else { %>
                <span style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; color: #ccc; border-radius: 3px;">다음</span>
            <% } %>
        </div>
    </div>

    <!-- Pagination Info -->
    <div style="text-align: center; margin-top: 10px; color: #666;">
        총 <%= totalUsers %>명의 회원 (페이지 <%= currentPage %> / <%= totalPages %>)
    </div>
</form>

<script>
    const urlParams = new URLSearchParams(window.location.search);
    const status = urlParams.get('status');
    if (status === 'success') {
        alert("회원 정보가 성공적으로 수정되었습니다.");
        // Preserve the page parameter when removing the status parameter
        const page = urlParams.get('page');
        let newUrl = location.pathname + "?menu=member";
        if (page) {
            newUrl += "&page=" + page;
        }
        history.replaceState({}, "", newUrl);
    }

    document.querySelectorAll('input[type="text"], select').forEach((el) => {
        const original = el.value;

        el.addEventListener('change', () => {
            if (el.value !== original) {
                el.classList.add('modified');
            } else {
                el.classList.remove('modified');
            }
        });
    });

</script>
