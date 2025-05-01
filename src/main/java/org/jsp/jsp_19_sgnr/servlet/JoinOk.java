package org.jsp.jsp_19_sgnr.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/joinOk")
public class JoinOk extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String paswd = request.getParameter("paswd");
        String username = request.getParameter("username");
        String mobile = request.getParameter("mobile");

        Member member = new Member(id, paswd, username, mobile);
        MemberDao memberDao = new MemberDao();

        boolean isDuplicate = memberDao.existsById(id);

        if (isDuplicate) {
            response.sendRedirect("join.html?status=duplicate");
            return;
        }

        int result = memberDao.insert(member);

        if (result > 0) {
            response.sendRedirect("main.jsp?status=success");
        } else {
            response.sendRedirect("main.jsp?status=fail");
        }
    }
}
