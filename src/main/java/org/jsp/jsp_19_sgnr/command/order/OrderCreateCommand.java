package org.jsp.jsp_19_sgnr.command.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.BasketDao;
import org.jsp.jsp_19_sgnr.dao.OrderDao;
import org.jsp.jsp_19_sgnr.dao.ProductDao;
import org.jsp.jsp_19_sgnr.dto.Basket;
import org.jsp.jsp_19_sgnr.dto.BasketItem;
import org.jsp.jsp_19_sgnr.dto.Member;
import org.jsp.jsp_19_sgnr.dto.Order;
import org.jsp.jsp_19_sgnr.dto.OrderItem;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command implementation for creating a new order from basket items.
 * This command creates an order and order items in a transaction.
 */
public class OrderCreateCommand implements Command {

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

        // Get user's basket
        BasketDao basketDao = new BasketDao();
        Basket userBasket = basketDao.getOrCreateBasket(member.getEmail());

        if (userBasket == null) {
            request.setAttribute("errorMessage", "장바구니를 찾을 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        // Get selected basket items
        List<BasketItem> basketItems = basketDao.getSelectedBasketItems(userBasket.getBasketId());

        if (basketItems == null || basketItems.isEmpty()) {
            // If no items are explicitly selected, get all basket items
            basketItems = basketDao.getBasketItems(userBasket.getBasketId());
        }

        if (basketItems == null || basketItems.isEmpty()) {
            request.setAttribute("errorMessage", "장바구니에 상품이 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        // Get order information from request
        String orderPerson = request.getParameter("orderPerson");
        String receiver = request.getParameter("receiver");
        String zipCode = request.getParameter("zipCode");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String deliveryMessage = request.getParameter("deliveryMessage");

        // If order information is not provided, use member information or defaults
        if (orderPerson == null || orderPerson.isEmpty()) {
            orderPerson = member.getName();
        }
        if (receiver == null || receiver.isEmpty()) {
            receiver = member.getName();
        }
        if (zipCode == null || zipCode.isEmpty()) {
            zipCode = ""; // Default empty zipcode
        }
        if (address == null || address.isEmpty()) {
            address = ""; // Default empty address
        }
        if (phone == null || phone.isEmpty()) {
            phone = member.getPhone();
        }

        // Calculate order amount and delivery fee
        int totalAmount = 0;
        int totalDeliveryFee = 0;

        ProductDao productDao = new ProductDao();

        for (BasketItem item : basketItems) {
            totalAmount += item.getPrice() * item.getQuantity();

            // Get product to get delivery fee
            Product product = productDao.getProductById(item.getProductId());
            if (product != null) {
                totalDeliveryFee += product.getQt_delivery_fee();
            }
        }

        // Create order
        Order order = new Order();
        // Generate order ID using System.currentTimeMillis()
        order.setId_order(String.valueOf(System.currentTimeMillis()));
        order.setNo_user(member.getEmail());
        order.setQt_order_amount(totalAmount);
        order.setQt_deli_money(totalDeliveryFee);
        order.setQt_deli_period(3); // Default delivery period (3 days)
        order.setNm_order_person(orderPerson);
        order.setNm_receiver(receiver);
        order.setNo_delivery_zipno(zipCode);
        order.setNm_delivery_address(address);
        order.setNm_receiver_telno(phone);
        order.setNm_delivery_space(deliveryMessage);
        order.setCd_order_type("10"); // Normal order
        order.setSt_order("10"); // Waiting for payment
        order.setSt_payment("20"); // 결제완료 가정
        order.setNo_register(member.getEmail());

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < basketItems.size(); i++) {
            BasketItem basketItem = basketItems.get(i);

            OrderItem orderItem = new OrderItem();
            orderItem.setCn_order_item(i + 1); // Order item number
            orderItem.setNo_product(basketItem.getProductId());
            orderItem.setNo_user(member.getEmail());
            orderItem.setQt_unit_price(basketItem.getPrice());
            orderItem.setQt_order_item(basketItem.getQuantity());
            orderItem.setQt_order_item_amount(basketItem.getPrice() * basketItem.getQuantity());

            // Get product to get delivery fee
            Product product = productDao.getProductById(basketItem.getProductId());
            if (product != null) {
                orderItem.setQt_order_item_delivery_fee(product.getQt_delivery_fee());
            } else {
                orderItem.setQt_order_item_delivery_fee(0);
            }

            orderItem.setSt_payment("20"); // Payment completed
            orderItem.setNo_register(member.getEmail());

            orderItems.add(orderItem);
        }

        // Create order with items in a transaction
        OrderDao orderDao = new OrderDao();
        String orderId = orderDao.createOrder(order, orderItems);

        if (orderId == null) {
            request.setAttribute("errorMessage", "주문 생성에 실패했습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        // Clear basket after successful order creation
        basketDao.clearBasket(userBasket.getBasketId());

        // Set order ID in request for confirmation page
        request.setAttribute("orderId", orderId);

        // Forward to order confirmation page
        request.getRequestDispatcher("/WEB-INF/views/order/confirmation.jsp").forward(request, response);
    }
}
