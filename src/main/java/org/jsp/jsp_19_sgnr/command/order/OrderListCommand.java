package org.jsp.jsp_19_sgnr.command.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.OrderDao;
import org.jsp.jsp_19_sgnr.dto.Member;
import org.jsp.jsp_19_sgnr.dto.Order;

import java.io.IOException;
import java.util.List;

public class OrderListCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("member") == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        Member member = (Member) session.getAttribute("member");

        response.sendRedirect(request.getContextPath() + "/mypage.do?menu=orderList");
    }
}
