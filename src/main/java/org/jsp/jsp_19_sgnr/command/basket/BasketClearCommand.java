package org.jsp.jsp_19_sgnr.command.basket;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.BasketDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.util.HashMap;

public class BasketClearCommand implements Command {

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

        boolean success = basketDao.clearBasket(basketId);
        if (!success) {
            request.setAttribute("errorMessage", "장바구니를 비울 수 없습니다.");
            request.getRequestDispatcher("/error/errorPage.jsp").forward(request, response);
            return;
        }

        session.setAttribute("basket", new HashMap<String, Integer>());

        response.sendRedirect(request.getContextPath() + "/basket/view.do");
    }
}
