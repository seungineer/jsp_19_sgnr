package org.jsp.jsp_19_sgnr.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

@WebServlet("/loginOk")
public class LoginOk extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String paswd = request.getParameter("paswd");
        String rememberId = request.getParameter("rememberId");

        MemberDao memberDao = new MemberDao();
        Member member = memberDao.findByIdAndPswd(id, paswd);

        if (member != null && member.getPassword().equals(paswd)) {
            HttpSession session = request.getSession();
            session.setAttribute("member", member);

            if ("on".equals(rememberId)) {
                Cookie idCookie = new Cookie("savedId", URLEncoder.encode(id, "UTF-8"));
                idCookie.setMaxAge(60 * 60 * 24 * 7);
                idCookie.setPath("/");
                response.addCookie(idCookie);
            } else {
                Cookie idCookie = new Cookie("savedId", null);
                idCookie.setMaxAge(0);
                idCookie.setPath("/");
                response.addCookie(idCookie);
            }

            response.sendRedirect("main.jsp?status=success");
        } else {
            response.sendRedirect("main.jsp?status=fail");
        }
    }
}
