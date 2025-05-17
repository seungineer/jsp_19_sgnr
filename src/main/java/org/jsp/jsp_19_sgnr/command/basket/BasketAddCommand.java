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

/**
 * Command implementation for handling adding items to the basket.
 * This implementation persists basket items to the database.
 */
public class BasketAddCommand implements Command {

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
        int quantity = 1;
        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException e) {
            // Default to 1 if quantity is not provided or invalid
        }

        // Get price from request or from product
        int price = 0;
        try {
            price = Integer.parseInt(request.getParameter("price"));
        } catch (NumberFormatException e) {
            // If price is not provided or invalid, get it from the product
            ProductDao productDao = new ProductDao();
            Product product = productDao.getProductById(productId);
            if (product != null) {
                price = product.getQt_sale_price();
            } else {
                // Product not found, redirect to product list
                response.sendRedirect(request.getContextPath() + "/product/list.do");
                return;
            }
        }

        // Get or create basket in session for temporary storage
        Map<String, Integer> sessionBasket = (Map<String, Integer>) session.getAttribute("basket");
        if (sessionBasket == null) {
            sessionBasket = new HashMap<>();
            session.setAttribute("basket", sessionBasket);
        }

        // Add product to session basket or update quantity if already exists
        if (sessionBasket.containsKey(productId)) {
            sessionBasket.put(productId, sessionBasket.get(productId) + quantity);
        } else {
            sessionBasket.put(productId, quantity);
        }

        // Persist to database
        BasketDao basketDao = new BasketDao();

        // Get or create user's basket
        Basket userBasket = basketDao.getOrCreateBasket(member.getEmail());
        if (userBasket == null) {
            // Failed to get or create basket, show error
            request.setAttribute("errorMessage", "장바구니를 생성할 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        // Add item to basket
        boolean success = basketDao.addOrUpdateBasketItem(userBasket.getBasketId(), productId, quantity, price);
        System.out.println("장바구니 담기 성공여부 : " + success);
        if (!success) {
            // Failed to add item to basket, show error
            request.setAttribute("errorMessage", "장바구니에 상품을 추가할 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        // Dispatch to a page showing the basket contents
        response.sendRedirect(request.getContextPath() + "/basket/view.do");
    }
}
