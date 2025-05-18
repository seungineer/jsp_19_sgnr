<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  Member loggedInMember = (Member) session.getAttribute("member");
  if (loggedInMember == null) {
    response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
    return;
  }

  String userName = loggedInMember.getName();
  String menu = (String) request.getAttribute("menu");
  if (menu == null) menu = "modify";
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>마이페이지</title>
  <style>
    body { margin: 0; background-color: #f5f5f5; font-family: Arial, sans-serif; }
    .mypage-container { display: flex; height: 100vh; }
    .sidebar {
      width: 220px; background-color: white;
      box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
      padding: 20px; display: flex;
      flex-direction: column; justify-content: space-between;
      position: fixed;
      height: 100vh;
      top: 0;
      left: 0;
      z-index: 1000;
      overflow-y: auto;
    }
    .sidebar h2 { font-size: 18px; margin-bottom: 20px; color: #333; }
    .header-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }
    .go-home {
      font-size: 14px;
      color: #4285f4;
      text-decoration: none;
      margin-left: 10px;
    }
    .go-home:hover {
      text-decoration: underline;
    }
    .menu { flex-grow: 1; }
    .menu-item {
      margin-bottom: 12px; padding: 10px;
      border-radius: 5px; cursor: pointer; color: #333;
      text-decoration: none; display: block;
    }
    .menu-item:hover,
    .menu-item.active {
      background-color: #4285f4; color: white;
    }
    .user-info {
      font-size: 14px; color: #555;
      border-top: 1px solid #ddd; padding-top: 15px;
    }
    .logout-link {
      display: inline-block; margin-top: 5px;
      color: #4285f4; text-decoration: none;
    }
    .logout-link:hover { text-decoration: underline; }
    .content {
      flex: 1; padding: 40px; background-color: #f5f5f5;
      margin-left: 260px;
    }
    .content-box {
      height: 90%;
      background-color: white; padding: 30px 40px;
      border-radius: 15px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      overflow: scroll;
    }
    .content-box h3 { margin-top: 0; color: #333; }

  </style>
</head>
<body>
<div class="mypage-container">
  <div class="sidebar">
    <div class="menu">
      <div class="header-row">
        <h2>마이페이지</h2>
        <a href="${pageContext.request.contextPath}/index.jsp" class="go-home">메인 페이지로 이동</a>
      </div>
      <a class="menu-item <%= "modify".equals(menu) ? "active" : "" %>" href="${pageContext.request.contextPath}/mypage.do?menu=modify">회원정보 수정</a>
      <a class="menu-item <%= "orderList".equals(menu) ? "active" : "" %>" href="${pageContext.request.contextPath}/mypage.do?menu=orderList">주문 내역 조회</a>
    </div>
    <div class="user-info">
      <strong><%= userName %></strong> 님<br>
      <a class="logout-link" href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a>
    </div>
  </div>

  <div class="content">
    <div class="content-box">
      <% if ("modify".equals(menu)) { %>
      <jsp:include page="/WEB-INF/views/member/modify.jsp" />
      <% } else if ("orderList".equals(menu)) { %>
      <jsp:include page="/WEB-INF/views/order/list.jsp" />
      <% } else { %>
      <p>좌측 메뉴를 클릭해주세요.</p>
      <% } %>
    </div>
  </div>
</div>
</body>
</html>
