package org.jsp.jsp_19_sgnr.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/modifyOk")
public class ModifyOk extends HttpServlet {

    private static final Pattern PASSWORD_REGEX =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{5,15}$");

    private static final Pattern PHONE_REGEX =
            Pattern.compile("^010-\\d{4}-\\d{4}$");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("member") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        String id = req.getParameter("id");
        String password = req.getParameter("paswd");
        String username = req.getParameter("username");
        String mobile = req.getParameter("mobile");
        String type = "관리자".equals(req.getParameter("type")) ? "20" : "10";


        boolean validPassword = PASSWORD_REGEX.matcher(password).matches();
        boolean validPhone = PHONE_REGEX.matcher(mobile).matches();

        if (!validPassword || !validPhone) {
            resp.sendRedirect("modify.jsp?error=invalid");
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
        System.out.println(result);
        if (result == 1) {
            session.setAttribute("member", member);
            resp.sendRedirect("main.jsp?status=success");
        } else {
            resp.sendRedirect("modify.jsp?error=fail");
        }
    }
}
