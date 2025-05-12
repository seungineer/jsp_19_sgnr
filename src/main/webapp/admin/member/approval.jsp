<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.MemberDao" %>
<%
    MemberDao memberDao = new MemberDao();
    List<Member> pendingMembers = memberDao.findByStatusAndUserType("ST00", "10"); // ST00 상태, 일반 사용자만
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
                <button type="submit">승인</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
