<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
    <title>로그인 결과</title>
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
            HttpSession loggedInSession = request.getSession(false);

            if (loggedInSession != null) {
                Member member = (Member) loggedInSession.getAttribute("member");
                String username = (member != null) ? member.getName() : "사용자";
    %>
    <h1>로그인 성공!</h1>
    <p><%= username %>님, 환영합니다.</p>
    <a href="modify.jsp">회원정보 수정하러 가기</a>
    <a href="logout.jsp">로그아웃</a>
    <%
    } else {
    %>
    <h1>세션이 만료되었습니다.</h1>
    <p>다시 로그인해주세요.</p>
    <a href="login.html">로그인 하러 가기</a>
    <%
        }
    } else {
    %>
    <h1>로그인 실패</h1>
    <p>아이디 또는 비밀번호를 다시 확인하세요.</p>
    <a href="login.html">로그인 다시 시도</a>
    <%
        }
    %>
</div>
</body>
</html>
