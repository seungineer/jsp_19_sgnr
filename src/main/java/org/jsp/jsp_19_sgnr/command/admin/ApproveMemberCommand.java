package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;

/**
 * Command implementation for handling member approval.
 */
public class ApproveMemberCommand implements Command {
    private MemberDao memberDao = new MemberDao();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (id != null) {
            Member member = memberDao.findById(id);

            if (member != null) {
                String currentMobile = member.getPhone();
                String currentType = member.getUserType();
                String currentName = member.getName();
                memberDao.updateMemberInfo(id, currentName, currentMobile, "ST01", currentType);
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=approval");
    }
}