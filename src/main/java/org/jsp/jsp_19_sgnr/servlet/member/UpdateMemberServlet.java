package org.jsp.jsp_19_sgnr.servlet.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.dao.MemberDao;

import java.io.IOException;

@WebServlet("/admin/updateMember")
public class UpdateMemberServlet extends HttpServlet {
    private MemberDao memberDao = new MemberDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String[] idList = req.getParameterValues("idList");
        if (idList != null) {
            for (String id : idList) {
                String name = req.getParameter("name_" + id);
                String mobile = req.getParameter("mobile_" + id);
                String status = req.getParameter("status_" + id);
                String type = req.getParameter("type_" + id);
                if (mobile != null && status != null && name != null) {
                    memberDao.updateMemberInfo(id, name, mobile, status, type);
                }
            }
        }

        resp.sendRedirect("admin.jsp?menu=member&status=success");
    }
}