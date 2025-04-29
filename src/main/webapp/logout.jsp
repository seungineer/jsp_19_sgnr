<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<html>
<head>
    <title>로그아웃</title>
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
        HttpSession loggedInSession = request.getSession(false);

        if (loggedInSession == null || loggedInSession.getAttribute("member") == null) {
    %>
    <h1>이미 로그아웃된 상태입니다.</h1>
    <a href="login.html">로그인 하러 가기</a>
    <%
    } else {
        loggedInSession.invalidate();
    %>
    <h1>로그아웃 되었습니다.</h1>
    <a href="login.html">다시 로그인하기</a>
    <a href="join.html">회원가입 하러 가기</a>
    <%
        }
    %>
</div>
</body>
</html>
