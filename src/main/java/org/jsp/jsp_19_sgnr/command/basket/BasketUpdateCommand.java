package org.jsp.jsp_19_sgnr.command.basket;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.BasketDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BasketUpdateCommand implements Command {

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

        int basketId;
        try {
            basketId = Integer.parseInt(request.getParameter("basketId"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "잘못된 장바구니 ID입니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        BasketDao basketDao = new BasketDao();

        String[] selectedItems = request.getParameterValues("selectedItems");
        Map<Integer, Boolean> selectedStatus = new HashMap<>();

        basketDao.getBasketItems(basketId).forEach(item -> {
            selectedStatus.put(item.getItemId(), false);
        });

        if (selectedItems != null) {
            for (String itemIdStr : selectedItems) {
                try {
                    int itemId = Integer.parseInt(itemIdStr);
                    selectedStatus.put(itemId, true);
                } catch (NumberFormatException e) {
                }
            }
        }

        for (Map.Entry<Integer, Boolean> entry : selectedStatus.entrySet()) {
            basketDao.updateBasketItemSelected(entry.getKey(), entry.getValue());
        }

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();

            if (paramName.startsWith("quantity-")) {
                try {
                    int itemId = Integer.parseInt(paramName.substring("quantity-".length()));
                    int quantity = Integer.parseInt(request.getParameter(paramName));

                    if (quantity > 0) {
                        basketDao.updateBasketItemQuantity(itemId, quantity);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }

        Map<String, Integer> sessionBasket = new HashMap<>();
        basketDao.getBasketItems(basketId).forEach(item -> {
            sessionBasket.put(item.getProductId(), item.getQuantity());
        });
        session.setAttribute("basket", sessionBasket);

        response.sendRedirect(request.getContextPath() + "/basket/view.do");
    }
}
