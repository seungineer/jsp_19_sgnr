package org.jsp.jsp_19_sgnr.command.basket;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.BasketDao;
import org.jsp.jsp_19_sgnr.dto.BasketItem;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketRemoveCommand implements Command {

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

        int itemId;
        try {
            itemId = Integer.parseInt(request.getParameter("itemId"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "잘못된 상품 ID입니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        BasketDao basketDao = new BasketDao();

        String productId = null;
        List<BasketItem> basketItems = basketDao.getBasketItems(
            basketDao.getOrCreateBasket(member.getEmail()).getBasketId());
        for (BasketItem item : basketItems) {
            if (item.getItemId() == itemId) {
                productId = item.getProductId();
                break;
            }
        }

        boolean success = basketDao.removeBasketItem(itemId);
        if (!success) {
            request.setAttribute("errorMessage", "장바구니에서 상품을 제거할 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        if (productId != null) {
            Map<String, Integer> sessionBasket = (Map<String, Integer>) session.getAttribute("basket");
            if (sessionBasket != null) {
                sessionBasket.remove(productId);
                session.setAttribute("basket", sessionBasket);
            }
        }

        response.sendRedirect(request.getContextPath() + "/basket/view.do");
    }
}
