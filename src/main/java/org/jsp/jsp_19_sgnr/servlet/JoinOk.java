package org.jsp.jsp_19_sgnr.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

@WebServlet("/joinOk")
public class JoinOk extends HttpServlet {

    // 정규식 패턴
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{5,15}$");
    private static final Pattern MOBILE_REGEX = Pattern.compile("^010-\\d{4}-\\d{4}$");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String paswd = request.getParameter("paswd");
        String username = request.getParameter("username");
        String mobile = request.getParameter("mobile");

        boolean isValid = EMAIL_REGEX.matcher(id).matches()
                && PASSWORD_REGEX.matcher(paswd).matches()
                && MOBILE_REGEX.matcher(mobile).matches();

        if (!isValid) {
            String msg = URLEncoder.encode("입력 형식이 올바르지 않습니다.", "UTF-8");
            response.sendRedirect("join.html?error=" + msg);
            return;
        }

        MemberDao memberDao = new MemberDao();
        boolean isDuplicate = memberDao.existsById(id);

        if (isDuplicate) {
            response.sendRedirect("join.html?status=duplicate");
            return;
        }

        Member member = new Member(id, paswd, username, mobile);
        member.setStatus("ST00");
        member.setUserType("10");

        int result = memberDao.insert(member);

        if (result > 0) {
            response.sendRedirect("joinResult.jsp?status=success");
        } else {
            response.sendRedirect("joinResult.jsp?status=fail");
        }
    }
}
