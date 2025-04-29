<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
    <title>회원정보 수정</title>
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
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 24px;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: bold;
            margin-bottom: 5px;
            color: #333;
        }

        input[type="text"],
        input[type="password"],
        input[type="email"],
        select {
            padding: 10px;
            margin-bottom: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        #button-container {
            display: flex;
            justify-content: center;
        }

        button {
            width: 80%;
            padding: 12px;
            background-color: #4285f4;
            color: white;
            border: none;
            border-radius: 5px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background-color: #3367d6;
        }

        a {
            display: block;
            text-align: end;
            margin-top: 16px;
            color: #555;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
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

<div id="outline">
    <h1>회원정보 수정</h1>

    <form action="modifyOk" method="POST">
        <label for="id">아이디</label>
        <input type="text" id="id" name="id" value="<%= member.getId() %>" readonly
               style="background-color: #f4f4f4; cursor: not-allowed;">

        <label for="paswd">비밀번호</label>
        <input type="password" id="paswd" name="paswd" required>

        <label for="username">이름</label>
        <input type="text" id="username" name="username" value="<%= member.getUsername() %>" required>

        <label for="email">이메일</label>
        <input type="email" id="email" name="email" value="<%= member.getEmail() %>" required>

        <label for="mobile">휴대폰 번호</label>
        <input type="text" id="mobile" name="mobile" value="<%= member.getMobile() %>" required>

        <label for="gender">성별</label>
        <select id="gender" name="gender" required>
            <option value="M" <%= "M".equals(member.getGender()) ? "selected" : "" %>>남성</option>
            <option value="F" <%= "F".equals(member.getGender()) ? "selected" : "" %>>여성</option>
        </select>

        <div id="button-container">
            <button type="submit">수정하기</button>
        </div>
    </form>

    <a href="logout.jsp">로그아웃</a>
</div>

</body>
</html>
