<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
    <title>회원정보 수정</title>
</head>
<body>
<%
    HttpSession loggedInSession = request.getSession(false);

    if (loggedInSession == null || loggedInSession.getAttribute("member") == null) {
        response.sendRedirect("login.html");
        return;
    }

    Member member = (Member) loggedInSession.getAttribute("member");
%>

<h1>회원정보 수정</h1>

<form action="modifyOk" method="POST">
    <label for="id">아이디:</label><br>
    <input type="text" id="id" name="id" value="<%= member.getId() %>" readonly><br><br>

    <label for="paswd">비밀번호:</label><br>
    <input type="password" id="paswd" name="paswd" required><br><br>

    <label for="username">이름:</label><br>
    <input type="text" id="username" name="username" value="<%= member.getUsername() %>" required><br><br>

    <label for="email">이메일:</label><br>
    <input type="email" id="email" name="email" value="<%= member.getEmail() %>" required><br><br>

    <label for="mobile">휴대폰 번호:</label><br>
    <input type="text" id="mobile" name="mobile" value="<%= member.getMobile() %>" required><br><br>

    <label for="gender">성별:</label><br>
    <select id="gender" name="gender" required>
        <option value="M" <%= "M".equals(member.getGender()) ? "selected" : "" %>>남성</option>
        <option value="F" <%= "F".equals(member.getGender()) ? "selected" : "" %>>여성</option>
    </select><br><br>

    <button type="submit">수정하기</button>
</form>

<br>
<a href="logout.jsp">로그아웃</a>

</body>
</html>
