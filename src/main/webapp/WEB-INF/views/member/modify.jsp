<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<html>
<head>
    <title>회원정보 수정</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
        }

        form {
            display: flex;
            flex-direction: column;
            background-color: white;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        label {
            font-weight: bold;
            margin-bottom: 5px;
            color: #333;
        }

        input[type="text"],
        input[type="password"] {
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .error-message {
            font-size: 0.85em;
            color: #F44336;
            margin-bottom: 10px;
            display: none;
        }

        button {
            padding: 10px 15px;
            background-color: #2196F3;
            color: white;
            border: none;
            border-radius: 4px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #1976D2;
        }

        button:disabled {
            background-color: #9E9E9E;
            cursor: not-allowed;
        }

        a {
            display: inline-block;
            color: #2196F3;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        #button-container {
            display: flex;
            justify-content: flex-start;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<%
    Member member = (Member) session.getAttribute("member");
%>

<div class="container">
    <form id="modifyForm" action="${pageContext.request.contextPath}/member/modify.do" method="POST" onsubmit="return validateForm()">
        <label for="id">아이디</label>
        <input type="text" id="id" name="id" value="<%= member.getEmail() %>" readonly
               style="background-color: #f4f4f4; cursor: not-allowed;">

        <label for="paswd">비밀번호</label>
        <input type="password" id="paswd" name="paswd" required>
        <div id="passwordError" class="error-message"></div>

        <label for="username">이름</label>
        <input type="text" id="username" name="username" value="<%= member.getName() %>" required>

        <label for="mobile">휴대폰 번호</label>
        <input type="text" id="mobile" name="mobile" value="<%= member.getPhone() %>" required>
        <div id="mobileError" class="error-message"></div>

        <label for="type">사용자 구분</label>
        <input type="text" id="type" name="type" value="<%= member.getUserType().equals("10") ? "일반사용자" : "관리자" %>" readonly
               style="background-color: #f4f4f4; cursor: not-allowed;">

        <div id="button-container">
            <button type="submit" disabled>수정하기</button>
        </div>
    </form>
</div>


<script>
    const pwRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{5,15}$/;
    const phoneRegex = /^010-\d{4}-\d{4}$/;

    const pwInput = document.getElementById('paswd');
    const phoneInput = document.getElementById('mobile');
    const submitBtn = document.querySelector("button[type='submit']");

    const pwError = document.getElementById('passwordError');
    const phoneError = document.getElementById('mobileError');

    function validatePassword(show = true) {
        const value = pwInput.value;
        if (!pwRegex.test(value)) {
            if (show) {
                pwError.textContent = "비밀번호는 영문 대소문자+숫자 포함, 5~15자여야 합니다.";
                pwError.style.display = "block";
            }
            return false;
        }
        pwError.style.display = "none";
        return true;
    }

    function validateMobile(show = true) {
        const value = phoneInput.value.trim();
        if (!phoneRegex.test(value)) {
            if (show) {
                phoneError.textContent = "휴대폰 번호는 010-0000-0000 형식이어야 합니다.";
                phoneError.style.display = "block";
            }
            return false;
        }
        phoneError.style.display = "none";
        return true;
    }

    function updateButtonState() {
        const valid = validatePassword(false) && validateMobile(false);
        submitBtn.disabled = !valid;
    }

    pwInput.addEventListener("input", updateButtonState);
    phoneInput.addEventListener("input", updateButtonState);
    pwInput.addEventListener("blur", () => validatePassword(true));
    phoneInput.addEventListener("blur", () => validateMobile(true));

    function validateForm() {
        return validatePassword(true) && validateMobile(true);
    }

    window.onload = updateButtonState;
</script>
</body>
</html>
