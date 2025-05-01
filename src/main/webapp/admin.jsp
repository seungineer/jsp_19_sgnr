<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  Member loggedInMember = (Member) session.getAttribute("member");
  if (loggedInMember == null || !"20".equals(loggedInMember.getUserType())) {
    out.println("<script>alert('관리자만 접근 가능합니다.'); location.href='login.html';</script>");
    return;
  }

  String userName = loggedInMember.getName();
  String menu = request.getParameter("menu");
  if (menu == null) menu = "member";
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리자 페이지</title>
  <style>
    body { margin: 0; background-color: #f5f5f5; font-family: Arial, sans-serif; }
    .admin-container { display: flex; height: 100vh; }
    .sidebar {
      width: 220px; background-color: white;
      box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
      padding: 20px; display: flex;
      flex-direction: column; justify-content: space-between;
    }
    .sidebar h2 { font-size: 18px; margin-bottom: 20px; color: #333; }
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
    }
    .content-box {
      background-color: white; padding: 30px 40px;
      border-radius: 15px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    }
    .content-box h3 { margin-top: 0; color: #333; }
  </style>
</head>
<body>
<div class="admin-container">
  <div class="sidebar">
    <div class="menu">
      <h2>관리자 메뉴</h2>
      <a class="menu-item <%= "member".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=member">회원 관리</a>
      <a class="menu-item <%= "approval".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=approval">가입 승인</a>
      <a class="menu-item <%= "withdrawal".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=withdrawal">탈퇴 승인</a>
    </div>
    <div class="user-info">
      <strong><%= userName %></strong> 님<br>
      <a class="logout-link" href="logout.jsp">로그아웃</a>
    </div>
  </div>

  <div class="content">
    <div class="content-box">
      <% if ("member".equals(menu)) { %>
      <jsp:include page="admin/member.jsp" />
      <% } else if ("approval".equals(menu)) { %>
      <jsp:include page="admin/approval.jsp" />
      <% } else if ("withdrawal".equals(menu)) { %>
      <jsp:include page="admin/withdrawal.jsp" />
      <% } else { %>
      <p>좌측 메뉴를 클릭해주세요.</p>
      <% } %>
    </div>
  </div>
</div>
</body>
</html>
