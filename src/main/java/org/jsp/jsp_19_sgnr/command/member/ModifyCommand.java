package org.jsp.jsp_19_sgnr.command.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.util.regex.Pattern;

public class ModifyCommand implements Command {

    private static final Pattern PASSWORD_REGEX =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{5,15}$");

    private static final Pattern PHONE_REGEX =
            Pattern.compile("^010-\\d{4}-\\d{4}$");

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("member") == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        String id = request.getParameter("id");
        String password = request.getParameter("paswd");
        String username = request.getParameter("username");
        String mobile = request.getParameter("mobile");
        String type = "관리자".equals(request.getParameter("type")) ? "20" : "10";

        boolean validPassword = PASSWORD_REGEX.matcher(password).matches();
        boolean validPhone = PHONE_REGEX.matcher(mobile).matches();

        if (!validPassword || !validPhone) {
            response.sendRedirect(request.getContextPath() + "/member/modifyForm.do?error=invalid");
            return;
        }

        Member member = new Member();
        member.setEmail(id);
        member.setPassword(password);
        member.setName(username);
        member.setStatus("ST01");
        member.setUserType(type);
        member.setPhone(mobile);

        MemberDao dao = new MemberDao();
        int result = dao.update(member);

        if (result == 1) {
            session.setAttribute("member", member);
            response.sendRedirect(request.getContextPath() + "/product/list.do");
        } else {
            response.sendRedirect(request.getContextPath() + "/member/modifyForm.do?error=fail");
        }
    }
}
