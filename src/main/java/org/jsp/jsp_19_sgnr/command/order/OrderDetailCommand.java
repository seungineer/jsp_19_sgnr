package org.jsp.jsp_19_sgnr.command.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.OrderDao;
import org.jsp.jsp_19_sgnr.dao.ProductDao;
import org.jsp.jsp_19_sgnr.dto.Member;
import org.jsp.jsp_19_sgnr.dto.Order;
import org.jsp.jsp_19_sgnr.dto.OrderItem;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailCommand implements Command {

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
        List<OrderItem> orderItems = orderDao.getOrderItems(orderId);
        Order order = orderDao.getOrderById(orderId);

        if (orderItems == null || orderItems.isEmpty() || order == null) {
            request.setAttribute("errorMessage", "주문 정보를 찾을 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        ProductDao productDao = new ProductDao();
        Map<String, Product> productMap = new HashMap<>();

        for (OrderItem item : orderItems) {
            Product product = productDao.getProductById(item.getNo_product());
            if (product != null) {
                productMap.put(item.getNo_product(), product);
            }
        }

        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        request.setAttribute("productMap", productMap);

        request.getRequestDispatcher("/WEB-INF/views/order/detail.jsp").forward(request, response);
    }
}
