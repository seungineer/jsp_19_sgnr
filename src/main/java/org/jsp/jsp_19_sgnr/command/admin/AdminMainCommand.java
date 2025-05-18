package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;

public class AdminMainCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 관리자 권한 확인
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");

        if (member == null || !"20".equals(member.getUserType())) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        // 관리자 메인 페이지로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/admin/admin.jsp");
    }
}