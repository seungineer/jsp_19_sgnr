package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.MemberDao;

import java.io.IOException;

public class UpdateMemberCommand implements Command {
    private MemberDao memberDao = new MemberDao();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String[] idList = request.getParameterValues("idList");
        String menu = request.getParameter("menu");
        String page = request.getParameter("page");

        if (menu == null || menu.isEmpty()) {
            menu = "member";
        }

        if (idList != null) {
            for (String id : idList) {
                String name = request.getParameter("name_" + id);
                String mobile = request.getParameter("mobile_" + id);
                String status = request.getParameter("status_" + id);
                String type = request.getParameter("type_" + id);
                if (mobile != null && status != null && name != null) {
                    memberDao.updateMemberInfo(id, name, mobile, status, type);
                }
            }
        }

        String redirectUrl = request.getContextPath() + "/admin/admin.jsp?menu=" + menu + "&status=success";
        if (page != null && !page.isEmpty()) {
            redirectUrl += "&page=" + page;
        }
        response.sendRedirect(redirectUrl);
    }
}
