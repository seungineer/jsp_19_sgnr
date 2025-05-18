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
    int totalUsers = memberDao.countByStatusAndUserType("ST03", "10");
    int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

    // Ensure current page is not greater than total pages
    if (totalPages > 0 && currentPage > totalPages) {
        currentPage = totalPages;
    }

    // Get paginated list of members
    List<Member> pausedMembers = memberDao.getPaginatedMembersByStatusAndUserType("ST03", "10", currentPage, pageSize);
%>

<style>
    table {
        width: 100%;
        border-collapse: collapse;
        font-family: 'Segoe UI', sans-serif;
    }

    thead {
        background-color: #f0f0f0;
    }

    th, td {
        padding: 12px;
        border: 1px solid #c0d7ec;
        text-align: center;
    }

    tr:hover {
        background-color: #f0f8ff;
    }

    button[type="submit"] {
        padding: 6px 12px;
        background-color: #4285f4;
        color: white;
        border: none;
        border-radius: 5px;
        font-weight: bold;
        cursor: pointer;
    }

    button[type="submit"]:hover {
        background-color: #3367d6;
    }

    .action-cell {
        display: flex;
        gap: 8px;
        justify-content: center;
    }

    form {
        margin: 0;
    }
</style>

<h3>탈퇴 승인 요청 목록</h3>
<p>일시정지(ST03) 상태인 일반 사용자만 표시됩니다.</p>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>이름</th>
        <th>이메일</th>
        <th>휴대폰번호</th>
        <th>현재 상태</th>
        <th>처리</th>
    </tr>
    </thead>
    <tbody>
    <% for (Member m : pausedMembers) { %>
    <tr>
        <td><%= m.getEmail() %></td>
        <td><%= m.getName() %></td>
        <td><%= m.getEmail() %></td>
        <td><%= m.getPhone() %></td>
        <td><%= m.getStatus() %></td>
        <td class="action-cell">
            <form action="<%= request.getContextPath() %>/admin/member/withdraw.do" method="post">
                <input type="hidden" name="id" value="<%= m.getEmail() %>"/>
                <input type="hidden" name="action" value="approve"/>
                <input type="hidden" name="page" value="<%= currentPage %>"/>
                <button type="submit">승인</button>
            </form>
            <form action="<%= request.getContextPath() %>/admin/member/withdraw.do" method="post">
                <input type="hidden" name="id" value="<%= m.getEmail() %>"/>
                <input type="hidden" name="action" value="reject"/>
                <input type="hidden" name="page" value="<%= currentPage %>"/>
                <button type="submit">반려</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

<!-- Pagination Controls -->
<div style="display: flex; justify-content: center; margin-top: 20px;">
    <div style="display: flex; align-items: center;">
        <% if (currentPage > 1) { %>
            <a href="?menu=withdrawal&page=<%= currentPage - 1 %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;">이전</a>
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
                <a href="?menu=withdrawal&page=<%= i %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;"><%= i %></a>
            <% } %>
        <% } %>

        <% if (currentPage < totalPages) { %>
            <a href="?menu=withdrawal&page=<%= currentPage + 1 %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;">다음</a>
        <% } else { %>
            <span style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; color: #ccc; border-radius: 3px;">다음</span>
        <% } %>
    </div>
</div>

<!-- Pagination Info -->
<div style="text-align: center; margin-top: 10px; color: #666;">
    총 <%= totalUsers %>명의 탈퇴 승인 대기 회원 (페이지 <%= currentPage %> / <%= totalPages %>)
</div>
