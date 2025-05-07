package org.jsp.jsp_19_sgnr.servlet.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

@WebServlet("/loginOk")
public class LoginOk extends HttpServlet {
    private final Pattern emailPattern = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private final Pattern pwPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{5,15}$");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String paswd = request.getParameter("paswd");
        String rememberId = request.getParameter("rememberId");

        if (id == null || paswd == null || !emailPattern.matcher(id).matches() || !pwPattern.matcher(paswd).matches()) {
            String errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
            String encodedMessage = URLEncoder.encode(errorMessage, "UTF-8");
            response.sendRedirect("login.html?error=" + encodedMessage);
            return;
        }
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
