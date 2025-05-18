<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.MemberDao" %>
<%
    MemberDao memberDao = new MemberDao();

    int currentPage = 1;
    int pageSize = 10;

    String pageParam = request.getParameter("page");
    if (pageParam != null && !pageParam.isEmpty()) {
        try {
            currentPage = Integer.parseInt(pageParam);
            if (currentPage < 1) currentPage = 1;
        } catch (NumberFormatException e) {
            currentPage = 1;
        }
    }

    int totalUsers = memberDao.countByStatusAndUserType("ST00", "10");
    int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

    if (totalPages > 0 && currentPage > totalPages) {
        currentPage = totalPages;
    }

    List<Member> pendingMembers = memberDao.getPaginatedMembersByStatusAndUserType("ST00", "10", currentPage, pageSize);
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
</style>

<h3>가입 승인 요청 목록</h3>
<p>가입 요청 상태(ST00)의 일반 사용자만 표시됩니다.</p>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>이름</th>
        <th>이메일</th>
        <th>휴대폰번호</th>
        <th>현재 상태</th>
        <th>승인 처리</th>
    </tr>
    </thead>
    <tbody>
    <% for (Member m : pendingMembers) { %>
    <tr>
        <td><%= m.getEmail() %></td>
        <td><%= m.getName() %></td>
        <td><%= m.getEmail() %></td>
        <td><%= m.getPhone() %></td>
        <td><%= m.getStatus() %></td>
        <td>
            <form action="<%= request.getContextPath() %>/admin/member/approve.do" method="post" style="margin: 0;">
                <input type="hidden" name="id" value="<%= m.getEmail() %>"/>
                <input type="hidden" name="page" value="<%= currentPage %>"/>
                <button type="submit">승인</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

<div style="display: flex; justify-content: center; margin-top: 20px;">
    <div style="display: flex; align-items: center;">
        <% if (currentPage > 1) { %>
            <a href="?menu=approval&page=<%= currentPage - 1 %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;">이전</a>
        <% } else { %>
            <span style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; color: #ccc; border-radius: 3px;">이전</span>
        <% } %>

        <% 
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);

        for (int i = startPage; i <= endPage; i++) { 
        %>
            <% if (i == currentPage) { %>
                <span style="margin: 0 5px; padding: 5px 10px; background-color: #4285f4; color: white; border-radius: 3px;"><%= i %></span>
            <% } else { %>
                <a href="?menu=approval&page=<%= i %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;"><%= i %></a>
            <% } %>
        <% } %>

        <% if (currentPage < totalPages) { %>
            <a href="?menu=approval&page=<%= currentPage + 1 %>" style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; text-decoration: none; border-radius: 3px;">다음</a>
        <% } else { %>
            <span style="margin: 0 5px; padding: 5px 10px; border: 1px solid #ccc; color: #ccc; border-radius: 3px;">다음</span>
        <% } %>
    </div>
</div>

<div style="text-align: center; margin-top: 10px; color: #666;">
    총 <%= totalUsers %>명의 가입 승인 대기 회원 (페이지 <%= currentPage %> / <%= totalPages %>)
</div>
