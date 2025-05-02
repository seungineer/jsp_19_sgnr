<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.MemberDao" %>
<%
    MemberDao memberDao = new MemberDao();
    List<Member> members = memberDao.findOnlyAdmin();
%>
<h3>회원 관리</h3>
<p>회원 정보를 아래 테이블에서 수정할 수 있습니다.</p>

<form action="<%= request.getContextPath() %>/admin/updateMember" method="post">
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
            <td><%= m.getName() %></td>
            <td><%= m.getEmail() %></td>
            <td>
                <input type="text" name="mobile_<%= m.getEmail() %>" value="<%= m.getPhone() %>" />
            </td>
            <td>
                <select name="status_<%= m.getEmail() %>">
                    <option value="ST00" <%= "ST00".equals(m.getStatus()) ? "selected" : "" %>>요청</option>
                    <option value="ST01" <%= "ST01".equals(m.getStatus()) ? "selected" : "" %>>정상</option>
                    <option value="ST02" <%= "ST02".equals(m.getStatus()) ? "selected" : "" %>>해지</option>
                    <option value="ST03" <%= "ST03".equals(m.getStatus()) ? "selected" : "" %>>일시정지</option>
                </select>
            </td>
            <td>
                <select name="type_<%= m.getEmail() %>">
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
</form>

<script>
    const urlParams = new URLSearchParams(window.location.search);
    const status = urlParams.get('status');
    if (status === 'success') {
        alert("회원 정보가 성공적으로 수정되었습니다.");
        history.replaceState({}, "", location.pathname + location.search.replace(/&?status=success/, ""));
    }
</script>