<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page import="org.jsp.jsp_19_sgnr.dto.Basket" %>
<%@ page import="org.jsp.jsp_19_sgnr.dao.BasketDao" %>
<%@ page import="java.util.Map" %>
<%
    // Check if user is logged in
    Member member = (Member) session.getAttribute("member");
%>
<header class="site-header">
    <div class="header-container">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/product/list.do">쇼핑몰</a>
        </div>
        <div class="search-container">
            <form action="${pageContext.request.contextPath}/product/list.do" method="get">
                <input type="text" name="keyword" placeholder="상품명 검색..." class="search-input">
                <button type="submit" class="search-button">검색</button>
            </form>
        </div>
        <nav class="main-nav">
            <ul>
                <% if (member != null) { %>
                    <li><span class="welcome-message"><%= member.getName() %>님 환영합니다</span></li>
                    <li><a href="${pageContext.request.contextPath}/member/modifyForm.do">마이페이지</a></li>
                    <li>
                        <a href="${pageContext.request.contextPath}/basket/view.do" class="cart-link">
                            장바구니
                        </a>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a></li>
                <% } else { %>
                    <li><a href="${pageContext.request.contextPath}/member/loginForm.do">로그인</a></li>
                    <li><a href="${pageContext.request.contextPath}/member/joinForm.do">회원가입</a></li>
                <% } %>
            </ul>
        </nav>
    </div>
</header>

<style>
    .site-header {
        background-color: #ffffff;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        position: sticky;
        top: 0;
        z-index: 1000;
    }
    
    .header-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 15px 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
    
    .logo a {
        font-size: 24px;
        font-weight: bold;
        color: #4285f4;
        text-decoration: none;
    }
    
    .search-container {
        flex-grow: 1;
        margin: 0 20px;
        display: flex;
    }
    
    .search-input {
        width: 100%;
        padding: 10px 15px;
        border: 1px solid #ddd;
        border-radius: 4px 0 0 4px;
        font-size: 14px;
    }
    
    .search-button {
        padding: 10px 15px;
        background-color: #4285f4;
        color: white;
        border: none;
        border-radius: 0 4px 4px 0;
        cursor: pointer;
    }
    
    .main-nav ul {
        display: flex;
        list-style: none;
        margin: 0;
        padding: 0;
    }
    
    .main-nav li {
        margin-left: 20px;
    }
    
    .main-nav a {
        color: #333;
        text-decoration: none;
        font-size: 14px;
    }
    
    .main-nav a:hover {
        color: #4285f4;
    }
    
    .welcome-message {
        font-size: 14px;
        color: #666;
    }
    
    .cart-link {
        position: relative;
    }
    
    .cart-count {
        position: absolute;
        top: -10px;
        right: -10px;
        background-color: #e53935;
        color: white;
        font-size: 12px;
        width: 18px;
        height: 18px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
    }
</style>