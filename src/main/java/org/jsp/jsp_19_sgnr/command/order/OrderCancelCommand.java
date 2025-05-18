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

public class OrderCancelCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("member") == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        Member member = (Member) session.getAttribute("member");

        String orderId = request.getParameter("orderId");

        if (orderId == null || orderId.isEmpty()) {
            request.setAttribute("errorMessage", "주문 ID가 제공되지 않았습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        OrderDao orderDao = new OrderDao();
        Order order = orderDao.getOrderById(orderId);

        if (order == null) {
            request.setAttribute("errorMessage", "주문을 찾을 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        if (!order.getNo_user().equals(member.getEmail())) {
            request.setAttribute("errorMessage", "다른 사용자의 주문을 취소할 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        if (!"10".equals(order.getSt_order()) && !"20".equals(order.getSt_order())) {
            request.setAttribute("errorMessage", "배송 전 상태의 주문만 취소할 수 있습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        boolean success = orderDao.updateOrderStatus(orderId, "30");

        if (!success) {
            request.setAttribute("errorMessage", "주문 취소에 실패했습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/order/list.do");
    }
}
