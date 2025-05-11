<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  String result = request.getParameter("result");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원 탈퇴 처리 결과</title>
  <style>
    body {
      margin: 0;
      font-family: Arial, sans-serif;
      background-color: #f5f5f5;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }

    .result-box {
      background-color: white;
      padding: 40px 60px;
      border-radius: 15px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      text-align: center;
    }

    .result-box h2 {
      color: #333;
      margin-bottom: 24px;
    }

    .result-message.success {
      color: #2b73ec;
      font-weight: bold;
    }

    .result-message.fail {
      color: red;
      font-weight: bold;
    }

    .result-box a {
      display: inline-block;
      margin-top: 20px;
      padding: 12px 20px;
      background-color: #4285f4;
      color: white;
      text-decoration: none;
      border-radius: 5px;
      font-weight: bold;
    }

    .result-box a:hover {
      background-color: #3367d6;
    }
  </style>
</head>
<body>
<div class="result-box">
  <h2>회원 탈퇴 결과</h2>
  <%
    if ("success".equals(result)) {
  %>
  <p class="result-message success">회원 탈퇴(일시정지) 처리가 정상적으로 완료되었습니다.</p>
  <a href="login.html">로그인 화면으로 이동</a>
  <%
  } else {
  %>
  <p class="result-message fail">회원 탈퇴 처리 중 문제가 발생했습니다.</p>
  <a href="main.jsp">메인으로 돌아가기</a>
  <%
    }
  %>
</div>
</body>
</html>
