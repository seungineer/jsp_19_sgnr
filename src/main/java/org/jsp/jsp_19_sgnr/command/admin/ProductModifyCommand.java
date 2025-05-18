package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.ContentDao;
import org.jsp.jsp_19_sgnr.dao.ProductDao;
import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Member;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ProductModifyCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        Enumeration<String> paramNames = request.getParameterNames();
        Set<String> modifiedProducts = new HashSet<>();
        Set<String> deletedProducts = new HashSet<>();


        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);

            if (paramName.startsWith("modified_") && "true".equals(paramValue)) {
                String productId = paramName.substring("modified_".length());
                modifiedProducts.add(productId);
            } else if (paramName.startsWith("delete_") && "true".equals(paramValue)) {
                String productId = paramName.substring("delete_".length());
                deletedProducts.add(productId);
            }
        }

        modifiedProducts.removeAll(deletedProducts);


        String page = request.getParameter("page");

        if (modifiedProducts.isEmpty() && deletedProducts.isEmpty()) {
            String redirectUrl = request.getContextPath() + "/admin/admin.jsp?menu=productModify&status=nochange";
            if (page != null && !page.isEmpty()) {
                redirectUrl += "&page=" + page;
            }
            response.sendRedirect(redirectUrl);
            return;
        }

        Connection conn = null;
        boolean success = true;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            ProductDao productDao = new ProductDao();
            ContentDao contentDao = new ContentDao();

            for (String productId : deletedProducts) {
                int deleteResult = productDao.deleteProduct(productId, conn);
                if (deleteResult <= 0) {
                    throw new SQLException("Failed to delete product: " + productId);
                }

            }

            for (String productId : modifiedProducts) {
                Product product = new Product();
                product.setNo_product(productId);
                product.setNm_product(request.getParameter("nm_product_" + productId));
                product.setQt_sale_price(Integer.parseInt(request.getParameter("qt_sale_price_" + productId)));
                product.setQt_stock(Integer.parseInt(request.getParameter("qt_stock_" + productId)));
                product.setSale_status(Integer.parseInt(request.getParameter("sale_status_" + productId)));

                int result = productDao.updateProduct(product, conn);
                if (result <= 0) {
                    throw new SQLException("Failed to update product: " + productId);
                }

                Part filePart = request.getPart("newImage_" + productId);
                if (filePart != null && filePart.getSize() > 0) {
                    String originalFileName = filePart.getSubmittedFileName();

                    String saveFileName = contentDao.generateSaveFileName(originalFileName);
                    String fileExt = contentDao.getFileExtension(originalFileName);
                    String fileType = filePart.getContentType();
                    String filePath = "/upload/images/";

                    String fileId = contentDao.insertFile(
                            originalFileName,
                            saveFileName,
                            filePath,
                            filePart.getInputStream(),
                            fileExt,
                            fileType,
                            member.getName(),
                            conn
                    );

                    if (fileId != null) {
                        result = productDao.updateProductImage(productId, fileId, conn);
                        if (result <= 0) {
                            throw new SQLException("Failed to update product image: " + productId);
                        }
                    } else {
                        throw new SQLException("Failed to insert file data for product: " + productId);
                    }
                }
            }

            conn.commit();  // Commit transaction

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (success) {
            String redirectUrl = request.getContextPath() + "/admin/admin.jsp?menu=productModify&status=success";
            if (page != null && !page.isEmpty()) {
                redirectUrl += "&page=" + page;
            }
            response.sendRedirect(redirectUrl);
        } else {
            String redirectUrl = request.getContextPath() + "/admin/admin.jsp?menu=productModify&status=fail";
            if (page != null && !page.isEmpty()) {
                redirectUrl += "&page=" + page;
            }
            response.sendRedirect(redirectUrl);
        }
    }
}
