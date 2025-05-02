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

@WebServlet("/admin/approveMember")
public class ApproveMemberServlet extends HttpServlet {
    private MemberDao memberDao = new MemberDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");

        if (id != null) {
            Member member = memberDao.findById(id);

            if (member != null) {
                String currentMobile = member.getPhone();
                String currentType = member.getUserType();
                String currentName = member.getName();
                memberDao.updateMemberInfo(id, currentName, currentMobile, "ST01", currentType);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/admin.jsp?menu=approval");
    }
}
