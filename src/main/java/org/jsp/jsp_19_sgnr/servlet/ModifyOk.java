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

@WebServlet("/modifyOk")
public class ModifyOk extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("member") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Member oldMember = (Member) session.getAttribute("member");

        String id = request.getParameter("id");
        String paswd = request.getParameter("paswd");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String gender = request.getParameter("gender");

        Member updatedMember = new Member(id, paswd, username, email, mobile, gender);

        MemberDao memberDao = new MemberDao();
        int result = memberDao.update(updatedMember);

        if (result > 0) {
            session.setAttribute("member", updatedMember);
            response.sendRedirect("modifyResult.jsp?status=success");
        } else {
            response.sendRedirect("modifyResult.jsp?status=fail");
        }
    }
}
