<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.MemberDao" %>
<%
    MemberDao memberDao = new MemberDao();
    List<Member> pendingMembers = memberDao.findByStatusAndUserType("ST00", "10"); // ST00 상태, 일반 사용자만
%>
<h3>가입 승인 요청 목록</h3>
<p>가입 요청 상태(ST00)의 일반 사용자만 표시됩니다.</p>

<table border="1" cellspacing="0" cellpadding="8" style="width: 100%; margin-top: 20px; border-collapse: collapse;">
    <thead>
    <tr style="background-color: #f0f0f0;">
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
            <form action="<%= request.getContextPath() %>/admin/approveMember" method="post" style="margin: 0;">
                <input type="hidden" name="id" value="<%= m.getEmail() %>"/>
                <button type="submit">승인</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
