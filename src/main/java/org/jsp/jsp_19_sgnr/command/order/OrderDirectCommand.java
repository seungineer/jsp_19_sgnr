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
import java.util.ArrayList;
import java.util.List;

/**
 * Command implementation for creating a new order directly from product detail page.
 * This command creates an order and order item in a transaction without using the basket.
 */
public class OrderDirectCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Check if user is logged in
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        // Get product information from request
        String productId = request.getParameter("productId");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int price = Integer.parseInt(request.getParameter("price"));
        
        if (productId == null || productId.isEmpty()) {
            request.setAttribute("errorMessage", "상품 정보를 찾을 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        // Get product from database to get delivery fee
        ProductDao productDao = new ProductDao();
        Product product = productDao.getProductById(productId);
        
        if (product == null) {
            request.setAttribute("errorMessage", "상품 정보를 찾을 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        // Calculate order amount and delivery fee
        int totalAmount = price * quantity;
        int deliveryFee = product.getQt_delivery_fee();

        // Create order
        Order order = new Order();
        // Generate order ID using System.currentTimeMillis()
        order.setId_order(String.valueOf(System.currentTimeMillis()));
        order.setNo_user(member.getEmail());
        order.setQt_order_amount(totalAmount);
        order.setQt_deli_money(deliveryFee);
        order.setQt_deli_period(3); // Default delivery period (3 days)
        order.setNm_order_person(member.getName());
        order.setNm_receiver(member.getName());
        order.setNo_delivery_zipno(""); // Default empty zipcode
        order.setNm_delivery_address(""); // Default empty address
        order.setNm_receiver_telno(member.getPhone());
        order.setNm_delivery_space(""); // Default empty delivery message
        order.setCd_order_type("10"); // Normal order
        order.setSt_order("10"); // Waiting for payment
        order.setSt_payment("20"); // 결제완료 가정
        order.setNo_register(member.getEmail());

        // Create order item
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setCn_order_item(1); // Order item number
        orderItem.setNo_product(productId);
        orderItem.setNo_user(member.getEmail());
        orderItem.setQt_unit_price(price);
        orderItem.setQt_order_item(quantity);
        orderItem.setQt_order_item_amount(price * quantity);
        orderItem.setQt_order_item_delivery_fee(deliveryFee);
        orderItem.setSt_payment("20"); // Payment completed
        orderItem.setNo_register(member.getEmail());
        orderItems.add(orderItem);

        // Create order with item in a transaction
        OrderDao orderDao = new OrderDao();
        String orderId = orderDao.createOrder(order, orderItems);

        if (orderId == null) {
            request.setAttribute("errorMessage", "주문 생성에 실패했습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        // Set order ID in request for confirmation page
        request.setAttribute("orderId", orderId);

        // Forward to order confirmation page
        request.getRequestDispatcher("/WEB-INF/views/order/confirmation.jsp").forward(request, response);
    }
}