<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원정보 수정 결과</title>
    <style>
        body {
            background-color: #f5f5f5;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        #outline {
            padding: 30px 40px;
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            width: 360px;
            text-align: center;
        }

        h1 {
            color: #333;
            margin-bottom: 24px;
        }

        p {
            color: #555;
            margin-bottom: 24px;
        }

        a {
            display: inline-block;
            margin: 8px;
            color: #4285f4;
            text-decoration: none;
            font-weight: bold;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div id="outline">
    <%
        String status = request.getParameter("status");

        if ("success".equals(status)) {
    %>
    <h1>회원정보 수정 성공!</h1>
    <p>정보가 정상적으로 수정되었습니다.</p>
    <a href="modify.jsp">다시 수정하기</a>
    <a href="logout.jsp">로그아웃</a>
    <%
    } else {
    %>
    <h1>회원정보 수정 실패...</h1>
    <p>문제가 발생했습니다. 다시 시도해주세요.</p>
    <a href="modify.jsp">회원정보 수정 다시 시도</a>
    <a href="logout.jsp">로그아웃</a>
    <%
        }
    %>
</div>
</body>
</html>
