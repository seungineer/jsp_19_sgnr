<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
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
        form {
            display: flex;
            flex-direction: column;
            position: relative;
        }

        .login-container {
            background-color: white;
            padding: 30px 40px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            width: 320px;
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 24px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }

        input[type="text"],
        input[type="password"] {

            padding: 10px;
            margin-bottom: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        #login-container {
            position: relative;
            display: flex;
            justify-content: center;
        }

        #remember-container {
            display: flex;
        }

        button {
            width: 80%;
            position: relative;
            justify-content: center;
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

        a {
            text-align: end;
            margin-top: 20px;
        }

        .link {
            display: block;
            text-align: center;
            margin-top: 16px;
            color: #555;
            text-decoration: none;
        }

        .link:hover {
            text-decoration: underline;
        }

        .error-message {
            color: red;
            font-size: 0.9em;
            margin-top: -10px;
            margin-bottom: 10px;
            display: none;
        }
    </style>
</head>
<body>
<div id="outline">
    <h1>로그인</h1>

    <form id="loginForm" action="${pageContext.request.contextPath}/member/login.do" method="POST" onsubmit="return validateForm()">
        <label for="id">아이디</label>
        <input type="text" id="id" name="id" required onblur="validateEmail()">
        <div id="emailError" class="error-message"></div>

        <label for="paswd">비밀번호</label>
        <input type="password" id="paswd" name="paswd" required onblur="validatePassword()">
        <div id="passwordError" class="error-message"></div>

        <div id="remember-container">
            <input type="checkbox" id="rememberId" name="rememberId">
            <label for="rememberId">아이디 기억하기</label>
        </div>

        <div id="login-container">
            <button type="submit">로그인</button>
        </div>
        <a href="${pageContext.request.contextPath}/member/joinForm.do">회원가입 하러 가기</a>
    </form>
</div>

<script>
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const pwRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{5,15}$/;

    function validateEmail(showError = true) {
        const id = document.getElementById('id').value.trim();
        const emailError = document.getElementById('emailError');

        if (!id) {
            if (showError) {
                emailError.textContent = "아이디를 입력해주세요.";
                emailError.style.display = "block";
            }
            return false;
        }

        if (!emailRegex.test(id)) {
            if (showError) {
                emailError.textContent = "올바른 이메일 형식이 아닙니다.";
                emailError.style.display = "block";
            }
            return false;
        }

        emailError.style.display = "none";
        return true;
    }

    function validatePassword(showError = true) {
        const paswd = document.getElementById('paswd').value;
        const passwordError = document.getElementById('passwordError');

        if (!paswd) {
            if (showError) {
                passwordError.textContent = "비밀번호를 입력해주세요.";
                passwordError.style.display = "block";
            }
            return false;
        }

        if (!pwRegex.test(paswd)) {
            if (showError) {
                passwordError.textContent = "비밀번호는 영문 대소문자 각각 1자 이상, 숫자 1자 이상을 포함한 5~15자여야 합니다.";
                passwordError.style.display = "block";
            }
            return false;
        }

        passwordError.style.display = "none";
        return true;
    }

    function updateButtonState() {
        const button = document.querySelector("button[type='submit']");
        // showError = false → 경고 메시지 표시 안 함
        const valid = validateEmail(false) && validatePassword(false);
        button.disabled = !valid;
    }

    function validateForm() {
        const isEmailValid = validateEmail();
        const isPasswordValid = validatePassword();

        return isEmailValid && isPasswordValid;
    }

    function getCookie(name) {
        const value = document.cookie.split('; ').find(row => row.startsWith(name + '='));
        return value ? value.split('=')[1] : '';
    }

    const savedId = getCookie('savedId');
    if (savedId) {
        document.getElementById('id').value = decodeURIComponent(savedId);
        document.getElementById('rememberId').checked = true;
    }

    // Check for error attribute in request (for forwarded requests)
    // This will be populated by server-side code when the page loads
    const errorElement = document.getElementById("serverError");
    if (errorElement && errorElement.value) {
        const errorBox = document.createElement("p");
        errorBox.textContent = errorElement.value;
        errorBox.style.color = "red";
        errorBox.style.marginTop = "10px";
        errorBox.style.textAlign = "center";
        document.getElementById("outline").prepend(errorBox);
    }

    // Also check URL parameters for backward compatibility
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    if (error) {
        const errorBox = document.createElement("p");
        errorBox.textContent = decodeURIComponent(error);
        errorBox.style.color = "red";
        errorBox.style.marginTop = "10px";
        errorBox.style.textAlign = "center";
        document.getElementById("outline").prepend(errorBox);
    }

    document.getElementById('id').addEventListener('blur', () => validateEmail(true));
    document.getElementById('paswd').addEventListener('blur', () => validatePassword(true));

    document.getElementById('id').addEventListener('input', updateButtonState);
    document.getElementById('paswd').addEventListener('input', updateButtonState);

</script>
</body>
</html>