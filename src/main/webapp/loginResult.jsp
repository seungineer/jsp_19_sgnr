<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>로그인 결과</title>
</head>
<body>
<%
    String status = request.getParameter("status");

    if ("success".equals(status)) {
%>
<h1>로그인 성공!</h1>
<p>환영합니다.</p>
<a href="modify.jsp">회원정보 수정하러 가기</a> |
<a href="logout.jsp">로그아웃</a>
<%
} else {
%>
<h1>로그인 실패</h1>
<p>아이디 또는 비밀번호를 다시 확인하세요.</p>
<a href="login.html">로그인 다시 시도</a>
<%
    }
%>
</body>
</html>
