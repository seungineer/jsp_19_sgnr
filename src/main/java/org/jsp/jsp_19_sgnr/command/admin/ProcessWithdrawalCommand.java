package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;

public class ProcessWithdrawalCommand implements Command {
    private MemberDao memberDao = new MemberDao();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String action = request.getParameter("action");
        String page = request.getParameter("page");

        if (id != null && action != null) {
            Member member = memberDao.findById(id);

            if (member != null) {
                String phone = member.getPhone();
                String newStatus = null;
                String currentType = member.getUserType();
                String currentName = member.getName();

                if ("approve".equals(action)) {
                    newStatus = "ST02";
                } else if ("reject".equals(action)) {
                    newStatus = "ST01";
                }

                if (newStatus != null) {
                    memberDao.updateMemberInfo(id, currentName, phone, newStatus, currentType);
                }
            }
        }

        String redirectUrl = request.getContextPath() + "/admin/admin.jsp?menu=withdrawal";
        if (page != null && !page.isEmpty()) {
            redirectUrl += "&page=" + page;
        }
        response.sendRedirect(redirectUrl);
    }
}
