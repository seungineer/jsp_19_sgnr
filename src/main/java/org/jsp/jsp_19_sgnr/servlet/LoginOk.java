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

@WebServlet("/loginOk")
public class LoginOk extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String paswd = request.getParameter("paswd");

        MemberDao memberDao = new MemberDao();
        Member member = memberDao.findByIdAndPswd(id, paswd);

        if (member != null && member.getPaswd().equals(paswd)) {
            HttpSession session = request.getSession();
            session.setAttribute("member", member);
            response.sendRedirect("loginResult.jsp?status=success");
        } else {
            response.sendRedirect("loginResult.jsp?status=fail");
        }
    }
}
