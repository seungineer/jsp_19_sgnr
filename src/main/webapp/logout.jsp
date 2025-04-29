<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
    <title>로그아웃</title>
</head>
<body>
<%
    HttpSession loggedInSession = request.getSession(false);
    if (loggedInSession != null) {
        loggedInSession.invalidate();
    }
%>

<h1>로그아웃 되었습니다.</h1>
<a href="login.html">다시 로그인하기</a> |
<a href="join.html">회원가입 하러 가기</a>
</body>
</html>
