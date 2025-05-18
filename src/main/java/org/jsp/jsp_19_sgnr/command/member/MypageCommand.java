package org.jsp.jsp_19_sgnr.command.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.OrderDao;
import org.jsp.jsp_19_sgnr.dto.Member;
import org.jsp.jsp_19_sgnr.dto.Order;

import java.io.IOException;
import java.util.List;

/**
 * Command for displaying the mypage with a 2-column layout.
 * This command forwards the request to the mypage.jsp page.
 * It checks if the user is logged in before displaying the page.
 */
public class MypageCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("member") == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        // Get menu parameter, default to "modify" if not provided
        String menu = request.getParameter("menu");
        if (menu == null) {
            menu = "modify";
        }

        // Set menu attribute for use in JSP
        request.setAttribute("menu", menu);

        Member member = (Member) session.getAttribute("member");

        // If menu is orderList, get the user's orders with pagination
        if ("orderList".equals(menu)) {
            OrderDao orderDao = new OrderDao();

            // Pagination parameters
            int currentPage = 1;
            int pageSize = 10;

            // Get page from request parameter
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    // If invalid, default to page 1
                    currentPage = 1;
                }
            }

            // Get total count and calculate total pages
            int totalOrders = orderDao.countOrdersByUser(member.getEmail());
            int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

            // Ensure current page is not greater than total pages
            if (totalPages > 0 && currentPage > totalPages) {
                currentPage = totalPages;
            }

            // Get paginated list of orders
            List<Order> orders = orderDao.getPaginatedOrdersByUser(member.getEmail(), currentPage, pageSize);

            // Set pagination attributes
            request.setAttribute("orders", orders);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalPages", totalPages);
        }

        // Forward to mypage.jsp
        request.getRequestDispatcher("/WEB-INF/views/member/mypage.jsp").forward(request, response);
    }
}
