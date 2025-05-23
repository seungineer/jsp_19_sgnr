<%@ page import="org.jsp.jsp_19_sgnr.dto.Member" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  Member loggedInMember = (Member) session.getAttribute("member");
  if (loggedInMember == null || !"20".equals(loggedInMember.getUserType())) {
    response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
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
<div class="admin-container">
  <div class="sidebar">
    <div class="menu">
      <h2>관리자 메뉴</h2>
      <a class="menu-item <%= "member".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=member">일반사용자 관리</a>
      <a class="menu-item <%= "adminMember".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=adminMember">관리자 관리</a>
      <a class="menu-item <%= "approval".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=approval">가입 승인</a>
      <a class="menu-item <%= "withdrawal".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=withdrawal">탈퇴 승인</a>

      <h2>상품</h2>
      <a class="menu-item <%= "product".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=product">상품 등록</a>
      <a class="menu-item <%= "productModify".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=productModify">상품 수정</a>
      <a class="menu-item <%= "categoryMapping".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=categoryMapping">카테고리 매핑 관리</a>
      <a class="menu-item <%= "productList".equals(menu) ? "active" : "" %>" href="<%= request.getContextPath() %>/admin/product/list.do">상품 조회</a>

      <h2 style="margin-top: 40px;">카테고리</h2>
      <a class="menu-item <%= "category".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=category">카테고리 등록</a>
      <a class="menu-item <%= "categoryManage".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=categoryManage">카테고리 관리</a>

      <h2 style="margin-top: 40px;">주문</h2>
      <a class="menu-item <%= "orderList".equals(menu) ? "active" : "" %>" href="admin.jsp?menu=orderList">주문 조회</a>
    </div>
    <div class="user-info">
      <strong><%= userName %></strong> 님<br>
      <a class="logout-link" href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a>
    </div>
  </div>

  <div class="content">
    <div class="content-box">
      <% if ("member".equals(menu)) { %>
      <jsp:include page="member/member.jsp" />
      <% } else if ("adminMember".equals(menu)) { %>
      <jsp:include page="member/adminMember.jsp" />
      <% } else if ("approval".equals(menu)) { %>
      <jsp:include page="member/approval.jsp" />
      <% } else if ("withdrawal".equals(menu)) { %>
      <jsp:include page="member/withdrawal.jsp" />
      <% } else if ("product".equals(menu)) { %>
      <jsp:include page="product/register.jsp" />
      <% } else if ("category".equals(menu)) { %>
      <jsp:include page="category/register.jsp" />
      <% } else if ("categoryMapping".equals(menu)) { %>
      <jsp:include page="product/category_mapping.jsp" />
      <% } else if ("categoryManage".equals(menu)) { %>
      <jsp:include page="category/manage.jsp" />
      <% } else if ("productModify".equals(menu)) { %>
      <jsp:include page="product/modify.jsp" />
      <% } else if ("productList".equals(menu)) { %>
      <jsp:include page="/WEB-INF/views/admin/productList.jsp" />
      <% } else if ("orderList".equals(menu)) { %>
      <jsp:include page="order/list.jsp" />
      <% } else { %>
      <p>좌측 메뉴를 클릭해주세요.</p>
      <% } %>
    </div>
  </div>
</div>
</body>
</html>
