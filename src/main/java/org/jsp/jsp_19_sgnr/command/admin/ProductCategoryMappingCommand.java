package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.ProductDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;

/**
 * Command implementation for handling category-product mapping operations.
 * This class is responsible for adding and removing mappings between categories and products.
 */
public class ProductCategoryMappingCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        String action = request.getParameter("action");
        String categoryId = request.getParameter("categoryId");

        if (categoryId == null || categoryId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=categoryMapping&status=fail&message=Category ID is required");
            return;
        }

        ProductDao dao = new ProductDao();
        boolean result = false;

        if ("add".equals(action)) {
            // Handle adding product to category
            String productId = request.getParameter("productId");

            if (productId == null || productId.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=categoryMapping&categoryId=" + categoryId + "&status=fail&message=Product ID is required");
                return;
            }

            result = dao.addProductToCategory(Integer.parseInt(categoryId), productId, member.getName());
        } else if ("remove".equals(action)) {
            // Handle removing product from category
            String productId = request.getParameter("productId");

            if (productId == null || productId.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=categoryMapping&categoryId=" + categoryId + "&status=fail&message=Product ID is required");
                return;
            }

            result = dao.removeProductFromCategory(Integer.parseInt(categoryId), productId);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=categoryMapping&status=fail&message=Invalid action");
            return;
        }

        if (result) {
            response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=categoryMapping&categoryId=" + categoryId + "&status=success");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=categoryMapping&categoryId=" + categoryId + "&status=fail");
        }
    }
}
