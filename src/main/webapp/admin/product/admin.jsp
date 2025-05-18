<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    String menu = request.getParameter("menu");
    String redirectUrl = request.getContextPath() + "/admin/admin.jsp";
    
    if (menu != null && !menu.isEmpty()) {
        redirectUrl += "?menu=" + menu;
    }
    
    response.sendRedirect(redirectUrl);
%>