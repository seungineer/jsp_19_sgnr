package org.jsp.jsp_19_sgnr.command.basket;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.BasketDao;
import org.jsp.jsp_19_sgnr.dto.Basket;
import org.jsp.jsp_19_sgnr.dto.BasketItem;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.util.List;

public class BasketViewCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("member") == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }
        
        Member member = (Member) session.getAttribute("member");
        
        BasketDao basketDao = new BasketDao();
        Basket userBasket = basketDao.getOrCreateBasket(member.getEmail());
        
        if (userBasket != null) {
            List<BasketItem> basketItems = basketDao.getBasketItems(userBasket.getBasketId());
            request.setAttribute("basketItems", basketItems);
            request.setAttribute("userBasket", userBasket);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/basket/view.jsp").forward(request, response);
    }
}