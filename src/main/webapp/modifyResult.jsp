<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원정보 수정 결과</title>
</head>
<body>
<%
    String status = request.getParameter("status");

    if ("success".equals(status)) {
%>
<h1>회원정보 수정 성공!</h1>
<p>정보가 정상적으로 수정되었습니다.</p>
<a href="modify.jsp">다시 수정하기</a> |
<a href="logout.jsp">로그아웃</a>
<%
} else {
%>
<h1>회원정보 수정 실패...</h1>
<p>문제가 발생했습니다. 다시 시도해주세요.</p>
<a href="modify.jsp">회원정보 수정 다시 시도</a> |
<a href="logout.jsp">로그아웃</a>
<%
    }
%>
</body>
</html>
