<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.MemberDao" %>
<%
    MemberDao memberDao = new MemberDao();
    List<Member> pausedMembers = memberDao.findByStatusAndUserType("ST03", "10");
%>
<h3>탈퇴 승인 요청 목록</h3>
<p>일시정지(ST03) 상태인 일반 사용자만 표시됩니다.</p>

<table border="1" cellspacing="0" cellpadding="8" style="width: 100%; margin-top: 20px; border-collapse: collapse;">
    <thead>
    <tr style="background-color: #f0f0f0;">
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
        <td style="display: flex; gap: 6px;">
            <form action="<%= request.getContextPath() %>/admin/processWithdrawal" method="post">
                <input type="hidden" name="id" value="<%= m.getEmail() %>"/>
                <input type="hidden" name="action" value="approve"/>
                <button type="submit">승인</button>
            </form>
            <form action="<%= request.getContextPath() %>/admin/processWithdrawal" method="post">
                <input type="hidden" name="id" value="<%= m.getEmail() %>"/>
                <input type="hidden" name="action" value="reject"/>
                <button type="submit">반려</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
