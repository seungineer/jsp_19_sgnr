package org.jsp.jsp_19_sgnr.command.member;

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

/**
 * Command for displaying the mypage with a 2-column layout.
 * This command forwards the request to the mypage.jsp page.
 * It checks if the user is logged in before displaying the page.
 */
public class MypageCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("member") == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        // Get menu parameter, default to "modify" if not provided
        String menu = request.getParameter("menu");
        if (menu == null) {
            menu = "modify";
        }

        // Set menu attribute for use in JSP
        request.setAttribute("menu", menu);

        Member member = (Member) session.getAttribute("member");

        // If menu is orderList, get the user's orders
        if ("orderList".equals(menu)) {
            OrderDao orderDao = new OrderDao();
            List<Order> orders = orderDao.getOrdersByUser(member.getEmail());
            request.setAttribute("orders", orders);
        }

        // Forward to mypage.jsp
        request.getRequestDispatcher("/WEB-INF/views/member/mypage.jsp").forward(request, response);
    }
}
