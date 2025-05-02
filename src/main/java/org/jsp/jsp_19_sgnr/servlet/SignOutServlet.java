package org.jsp.jsp_19_sgnr.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/signout")
public class SignOutServlet extends HttpServlet {
    private MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        boolean success = false;

        if (session != null) {
            Member loggedInMember = (Member) session.getAttribute("member");

            if (loggedInMember != null) {
                String userId = loggedInMember.getEmail();
                String currentMobile = loggedInMember.getPhone();
                try {
                    memberDao.updateMemberInfo(userId, currentMobile, "ST03"); // ST03 = 일시 정지
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            session.invalidate();
        }

        resp.sendRedirect(req.getContextPath() + "/signout.jsp?result=" + (success ? "success" : "fail"));
    }
}
