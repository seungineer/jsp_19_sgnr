<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  String result = request.getParameter("result");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원 탈퇴 처리 결과</title>
</head>
<body>
<h2>회원 탈퇴 결과</h2>

<%
  if ("success".equals(result)) {
%>
<p>회원 탈퇴(일시정지) 처리가 정상적으로 완료되었습니다.</p>
<a href="login.html">로그인 화면으로 이동</a>
<%
} else {
%>
<p style="color: red;">회원 탈퇴 처리 중 문제가 발생했습니다.</p>
<a href="main.jsp">메인으로 돌아가기</a>
<%
  }
%>
</body>
</html>
