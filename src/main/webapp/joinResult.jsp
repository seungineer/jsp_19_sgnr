<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입 결과</title>
</head>
<body>
<%
    String status = request.getParameter("status");

    if ("success".equals(status)) {
%>
<h1>회원가입 성공!</h1>
<p>축하합니다. 회원가입이 완료되었습니다.</p>
<a href="login.html">로그인 하러 가기</a>
<%
} else {
%>
<h1>회원가입 실패...</h1>
<p>죄송합니다. 문제가 발생했습니다. 다시 시도해주세요.</p>
<a href="join.html">회원가입 다시 시도</a>
<%
    }
%>
</body>
</html>
