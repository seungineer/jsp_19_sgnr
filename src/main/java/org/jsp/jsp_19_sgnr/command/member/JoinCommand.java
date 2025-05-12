package org.jsp.jsp_19_sgnr.command.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.MemberDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Command implementation for handling member registration.
 */
public class JoinCommand implements Command {

    // 정규식 패턴
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{5,15}$");
    private static final Pattern MOBILE_REGEX = Pattern.compile("^010-\\d{4}-\\d{4}$");

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
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
            request.setAttribute("error", "입력 형식이 올바르지 않습니다.");
            request.getRequestDispatcher("/join.html").forward(request, response);
            return;
        }

        MemberDao memberDao = new MemberDao();
        Member existing = memberDao.findById(id);

        if (existing != null && "ST02".equals(existing.getStatus())) {
            existing.setName(username);
            existing.setPassword(paswd);
            existing.setPhone(mobile);
            existing.setStatus("ST00");

            memberDao.updateForRejoin(existing);
            request.setAttribute("status", "rejoined");
            request.getRequestDispatcher("/joinResult.jsp").forward(request, response);

        } else if (existing != null) {
            request.setAttribute("status", "duplicate");
            request.getRequestDispatcher("/join.html").forward(request, response);
        } else {
            Member newMember = new Member(id, paswd, username, mobile);
            newMember.setStatus("ST00");
            newMember.setUserType("10");
            memberDao.insert(newMember);
            request.setAttribute("status", "success");
            request.getRequestDispatcher("/joinResult.jsp").forward(request, response);
        }
    }
}
