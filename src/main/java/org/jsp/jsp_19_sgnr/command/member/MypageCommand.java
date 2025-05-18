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

public class MypageCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("member") == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        String menu = request.getParameter("menu");
        if (menu == null) {
            menu = "modify";
        }

        request.setAttribute("menu", menu);

        Member member = (Member) session.getAttribute("member");

        if ("orderList".equals(menu)) {
            OrderDao orderDao = new OrderDao();

            int currentPage = 1;
            int pageSize = 10;

            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            int totalOrders = orderDao.countOrdersByUser(member.getEmail());
            int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

            if (totalPages > 0 && currentPage > totalPages) {
                currentPage = totalPages;
            }

            List<Order> orders = orderDao.getPaginatedOrdersByUser(member.getEmail(), currentPage, pageSize);

            request.setAttribute("orders", orders);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalPages", totalPages);
        }

        request.getRequestDispatcher("/WEB-INF/views/member/mypage.jsp").forward(request, response);
    }
}
