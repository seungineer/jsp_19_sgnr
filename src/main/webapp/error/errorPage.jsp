<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>오류 발생</title>
    <style>
        body {
            background-color: #f5f5f5;
            font-family: 'Segoe UI', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .error-box {
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            padding: 40px 50px;
            text-align: center;
            max-width: 600px;
        }

        .error-box h1 {
            color: #d9534f;
            font-size: 28px;
            margin-bottom: 16px;
        }

        .error-box p {
            color: #555;
            margin-bottom: 20px;
        }

        .error-box pre {
            background-color: #f8f8f8;
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
            overflow-x: auto;
            color: #a00;
        }

        .error-box a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #4285f4;
            color: white;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
        }

        .error-box a:hover {
            background-color: #3367d6;
        }
    </style>
</head>
<body>
<div class="error-box">
    <h1>시스템 오류가 발생했습니다</h1>
    <p>요청을 처리하는 중 문제가 발생했습니다.<br>잠시 후 다시 시도해 주세요.</p>

    <% if (exception != null) { %>
    <pre><%= exception.toString() %></pre>
    <pre><%= exception.getStackTrace()[0] %></pre>
    <% } %>

    <a href="<%= request.getContextPath() %>/main.jsp">메인 페이지로 이동</a>
</div>
</body>
</html>
