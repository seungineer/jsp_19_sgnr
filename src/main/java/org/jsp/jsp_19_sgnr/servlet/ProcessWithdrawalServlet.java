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

@WebServlet("/admin/processWithdrawal")
public class ProcessWithdrawalServlet extends HttpServlet {
    private MemberDao memberDao = new MemberDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String action = req.getParameter("action");

        if (id != null && action != null) {
            Member member = memberDao.findById(id);

            if (member != null) {
                String phone = member.getPhone();
                String newStatus = null;
                String currentType = member.getUserType();
                String currentName = member.getName();

                if ("approve".equals(action)) {
                    newStatus = "ST02";
                } else if ("reject".equals(action)) {
                    newStatus = "ST01";
                }

                if (newStatus != null) {
                    memberDao.updateMemberInfo(id, currentName, phone, newStatus, currentType);
                }
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/admin.jsp?menu=withdrawal");
    }
}
