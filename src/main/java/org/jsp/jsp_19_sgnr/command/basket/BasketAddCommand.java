package org.jsp.jsp_19_sgnr.command.basket;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command implementation for handling adding items to the basket.
 * This is a simple implementation that can be expanded later.
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
            response.sendRedirect(request.getContextPath() + "/login.html");
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
        
        // Get or create basket in session
        Map<String, Integer> basket = (Map<String, Integer>) session.getAttribute("basket");
        if (basket == null) {
            basket = new HashMap<>();
            session.setAttribute("basket", basket);
        }
        
        // Add product to basket or update quantity if already exists
        if (basket.containsKey(productId)) {
            basket.put(productId, basket.get(productId) + quantity);
        } else {
            basket.put(productId, quantity);
        }
        
        // Redirect to a page showing the basket contents
        response.sendRedirect(request.getContextPath() + "/basket/view.jsp");
    }
}