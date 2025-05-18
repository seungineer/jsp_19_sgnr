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

public class OrderCreateCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        BasketDao basketDao = new BasketDao();
        Basket userBasket = basketDao.getOrCreateBasket(member.getEmail());

        if (userBasket == null) {
            request.setAttribute("errorMessage", "장바구니를 찾을 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        String[] selectedItemIds = request.getParameterValues("selectedItemId");
        List<BasketItem> basketItems = new ArrayList<>();

        if (selectedItemIds != null && selectedItemIds.length > 0) {
            for (String itemIdStr : selectedItemIds) {
                try {
                    int itemId = Integer.parseInt(itemIdStr);
                    BasketItem item = basketDao.getBasketItemById(itemId);
                    if (item != null) {
                        String quantityParam = request.getParameter("selectedQuantity-" + itemId);
                        if (quantityParam != null && !quantityParam.isEmpty()) {
                            try {
                                int quantity = Integer.parseInt(quantityParam);
                                if (quantity > 0) {
                                    item.setQuantity(quantity);
                                }
                            } catch (NumberFormatException e) {
                            }
                        }
                        basketItems.add(item);
                    }
                } catch (NumberFormatException e) {
                }
            }
        } else {
            basketItems = basketDao.getBasketItems(userBasket.getBasketId());
        }

        if (basketItems == null || basketItems.isEmpty()) {
            request.setAttribute("errorMessage", "장바구니에 상품이 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        String orderPerson = request.getParameter("orderPerson");
        String receiver = request.getParameter("receiver");
        String zipCode = request.getParameter("zipCode");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String deliveryMessage = request.getParameter("deliveryMessage");

        if (orderPerson == null || orderPerson.isEmpty()) {
            orderPerson = member.getName();
        }
        if (receiver == null || receiver.isEmpty()) {
            receiver = member.getName();
        }
        if (zipCode == null || zipCode.isEmpty()) {
            zipCode = "";
        }
        if (address == null || address.isEmpty()) {
            address = "";
        }
        if (phone == null || phone.isEmpty()) {
            phone = member.getPhone();
        }

        int totalAmount = 0;
        int totalDeliveryFee = 0;

        ProductDao productDao = new ProductDao();

        for (BasketItem item : basketItems) {
            totalAmount += item.getPrice() * item.getQuantity();

            Product product = productDao.getProductById(item.getProductId());
            if (product != null) {
                totalDeliveryFee += product.getQt_delivery_fee();
            }
        }

        Order order = new Order();
        order.setId_order(String.valueOf(System.currentTimeMillis()));
        order.setNo_user(member.getEmail());
        order.setQt_order_amount(totalAmount);
        order.setQt_deli_money(totalDeliveryFee);
        order.setQt_deli_period(3);
        order.setNm_order_person(orderPerson);
        order.setNm_receiver(receiver);
        order.setNo_delivery_zipno(zipCode);
        order.setNm_delivery_address(address);
        order.setNm_receiver_telno(phone);
        order.setNm_delivery_space(deliveryMessage);
        order.setCd_order_type("10");
        order.setSt_order("10");
        order.setSt_payment("20");
        order.setNo_register(member.getEmail());

        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < basketItems.size(); i++) {
            BasketItem basketItem = basketItems.get(i);

            OrderItem orderItem = new OrderItem();
            orderItem.setCn_order_item(i + 1);
            orderItem.setNo_product(basketItem.getProductId());
            orderItem.setNo_user(member.getEmail());
            orderItem.setQt_unit_price(basketItem.getPrice());
            orderItem.setQt_order_item(basketItem.getQuantity());
            orderItem.setQt_order_item_amount(basketItem.getPrice() * basketItem.getQuantity());

            Product product = productDao.getProductById(basketItem.getProductId());
            if (product != null) {
                orderItem.setQt_order_item_delivery_fee(product.getQt_delivery_fee());
            } else {
                orderItem.setQt_order_item_delivery_fee(0);
            }

            orderItem.setSt_payment("20");
            orderItem.setNo_register(member.getEmail());

            orderItems.add(orderItem);
        }

        OrderDao orderDao = new OrderDao();
        String orderId = orderDao.createOrder(order, orderItems);

        if (orderId == null) {
            request.setAttribute("errorMessage", "주문 생성에 실패했습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        basketDao.clearBasket(userBasket.getBasketId());

        request.setAttribute("orderId", orderId);

        request.getRequestDispatcher("/WEB-INF/views/order/confirmation.jsp").forward(request, response);
    }
}
