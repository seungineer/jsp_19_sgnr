<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
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
            width: 40%;
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

        #message {
            color: red;
            margin-bottom: 16px;
        }

        #join-container {
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

        button:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }

        .error-message {
            color: red;
            font-size: 0.9em;
            margin-top: -10px;
            margin-bottom: 10px;
            display: none;
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
<div id="outline">
    <h1>회원가입</h1>

    <form action="${pageContext.request.contextPath}/member/join.do" method="POST">
        <label for="id">아이디</label>
        <input type="text" id="id" name="id" required>
        <div id="idError" class="error-message"></div>

        <label for="paswd">비밀번호</label>
        <input type="password" id="paswd" name="paswd" required>
        <div id="passwordError" class="error-message"></div>

        <label for="username">이름</label>
        <input type="text" id="username" name="username" required>

        <label for="mobile">휴대폰 번호</label>
        <input type="text" id="mobile" name="mobile" required>
        <div id="mobileError" class="error-message"></div>

        <div id="join-container">
            <button type="submit" disabled>회원가입</button>
        </div>
    </form>

    <a href="${pageContext.request.contextPath}/member/loginForm.do">로그인 하러 가기</a>
</div>

<script>
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const pwRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{5,15}$/;
    const mobileRegex = /^010-\d{4}-\d{4}$/;

    function validateId(show = true) {
        const id = document.getElementById('id').value.trim();
        const err = document.getElementById('idError');

        if (!id) {
            if (show) {
                err.textContent = "아이디(이메일)를 입력해주세요.";
                err.style.display = "block";
            }
            return false;
        }
        if (!emailRegex.test(id)) {
            if (show) {
                err.textContent = "이메일 형식이 아닙니다.";
                err.style.display = "block";
            }
            return false;
        }
        err.style.display = "none";
        return true;
    }

    function validatePassword(show = true) {
        const pw = document.getElementById('paswd').value;
        const err = document.getElementById('passwordError');

        if (!pw) {
            if (show) {
                err.textContent = "비밀번호를 입력해주세요.";
                err.style.display = "block";
            }
            return false;
        }
        if (!pwRegex.test(pw)) {
            if (show) {
                err.textContent = "비밀번호는 영문 대소문자 각 1자 이상, 숫자 1자 이상 포함, 5~15자여야 합니다.";
                err.style.display = "block";
            }
            return false;
        }
        err.style.display = "none";
        return true;
    }

    function validateMobile(show = true) {
        const mobile = document.getElementById('mobile').value.trim();
        const err = document.getElementById('mobileError');

        if (!mobile) {
            if (show) {
                err.textContent = "휴대폰 번호를 입력해주세요.";
                err.style.display = "block";
            }
            return false;
        }
        if (!mobileRegex.test(mobile)) {
            if (show) {
                err.textContent = "휴대폰 번호는 010-0000-0000 형식이어야 합니다.";
                err.style.display = "block";
            }
            return false;
        }
        err.style.display = "none";
        return true;
    }

    function updateJoinButton() {
        const isValid = validateId(false) && validatePassword(false) && validateMobile(false);
        document.querySelector("button[type='submit']").disabled = !isValid;
    }

    document.getElementById('id').addEventListener('input', updateJoinButton);
    document.getElementById('paswd').addEventListener('input', updateJoinButton);
    document.getElementById('mobile').addEventListener('input', updateJoinButton);

    document.getElementById('id').addEventListener('blur', () => validateId(true));
    document.getElementById('paswd').addEventListener('blur', () => validatePassword(true));
    document.getElementById('mobile').addEventListener('blur', () => validateMobile(true));
</script>
</body>
</html>