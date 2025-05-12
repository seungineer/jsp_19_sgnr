package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.ProductDao;
import org.jsp.jsp_19_sgnr.dto.Member;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.io.IOException;

/**
 * Command implementation for handling product registration.
 */
public class ProductRegisterCommand implements Command {

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

        String noProduct = "Product" + System.currentTimeMillis();

        Product product = new Product();
        product.setNo_product(noProduct);
        product.setNm_product(request.getParameter("nm_product"));
        product.setNm_detail_explain(request.getParameter("nm_detail_explain"));
        product.setDt_start_date(request.getParameter("dt_start_date"));
        product.setDt_end_date(request.getParameter("dt_end_date"));
        product.setQt_customer_price(Integer.parseInt(request.getParameter("qt_customer")));
        product.setQt_sale_price(Integer.parseInt(request.getParameter("qt_sale_price")));
        product.setQt_stock(Integer.parseInt(request.getParameter("qt_stock")));
        product.setQt_delivery_fee(Integer.parseInt(request.getParameter("qt_delivery_fee")));
        product.setNo_register(member.getName());
        product.setSale_status(Integer.parseInt(request.getParameter("sale_status")));

        ProductDao dao = new ProductDao();
        int result = dao.insertProduct(product);

        if (result > 0) {
            response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=product&status=success");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=product&status=fail");
        }
    }
}
