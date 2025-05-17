package org.jsp.jsp_19_sgnr.command.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;
import java.net.URLEncoder;

import java.io.IOException;

/**
 * Command implementation for handling user sign-out.
 */
public class SignOutCommand implements Command {
    private MemberDao memberDao = new MemberDao();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        boolean success = false;

        if (session != null) {
            Member loggedInMember = (Member) session.getAttribute("member");

            if (loggedInMember != null) {
                String userId = loggedInMember.getEmail();
                String currentMobile = loggedInMember.getPhone();
                String currentType = loggedInMember.getUserType();
                String currentName = loggedInMember.getName();
                try {
                    memberDao.updateMemberInfo(userId, currentName, currentMobile, "ST03", currentType); // ST03 = 일시 정지
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/member/loginForm.do?message=" + URLEncoder.encode((success ? "회원 탈퇴가 완료되었습니다." : "회원 탈퇴에 실패했습니다."), "UTF-8"));
    }
}
