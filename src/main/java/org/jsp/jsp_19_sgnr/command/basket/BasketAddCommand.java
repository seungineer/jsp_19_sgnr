package org.jsp.jsp_19_sgnr.command.basket;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.BasketDao;
import org.jsp.jsp_19_sgnr.dao.ProductDao;
import org.jsp.jsp_19_sgnr.dto.Basket;
import org.jsp.jsp_19_sgnr.dto.Member;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BasketAddCommand implements Command {

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

        String productId = request.getParameter("productId");
        int quantity = 1;
        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
        }

        int price = 0;
        try {
            price = Integer.parseInt(request.getParameter("price"));
        } catch (NumberFormatException e) {
            ProductDao productDao = new ProductDao();
            Product product = productDao.getProductById(productId);
            if (product != null) {
                price = product.getQt_sale_price();
            } else {
                response.sendRedirect(request.getContextPath() + "/product/list.do");
                return;
            }
        }

        Map<String, Integer> sessionBasket = (Map<String, Integer>) session.getAttribute("basket");
        if (sessionBasket == null) {
            sessionBasket = new HashMap<>();
            session.setAttribute("basket", sessionBasket);
        }

        if (sessionBasket.containsKey(productId)) {
            sessionBasket.put(productId, sessionBasket.get(productId) + quantity);
        } else {
            sessionBasket.put(productId, quantity);
        }

        BasketDao basketDao = new BasketDao();

        Basket userBasket = basketDao.getOrCreateBasket(member.getEmail());
        if (userBasket == null) {
            request.setAttribute("errorMessage", "장바구니를 생성할 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        boolean success = basketDao.addOrUpdateBasketItem(userBasket.getBasketId(), productId, quantity, price);
        if (!success) {
            request.setAttribute("errorMessage", "장바구니에 상품을 추가할 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/basket/view.do");
    }
}
