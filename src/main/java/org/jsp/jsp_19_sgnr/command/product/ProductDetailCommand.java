package org.jsp.jsp_19_sgnr.command.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.CategoryDao;
import org.jsp.jsp_19_sgnr.dao.ContentDao;
import org.jsp.jsp_19_sgnr.dao.ProductDao;
import org.jsp.jsp_19_sgnr.dto.Category;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Command for displaying product details.
 */
public class ProductDetailCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get product ID from request
        String productId = request.getParameter("productId");
        
        if (productId == null || productId.isEmpty()) {
            // No product ID provided, redirect to product list
            response.sendRedirect(request.getContextPath() + "/product/list.do");
            return;
        }
        
        // Get product details
        ProductDao productDao = new ProductDao();
        Product product = productDao.getProductById(productId);
        
        if (product == null) {
            // Product not found, redirect to product list
            response.sendRedirect(request.getContextPath() + "/product/list.do");
            return;
        }
        
        // Get product categories
        List<Integer> categoryIds = productDao.getCategoryMappings(productId);
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.findByIds(categoryIds);
        
        // Get product image information
        String fileId = product.getId_file();
        String imagePath = "";
        
        if (fileId != null && !fileId.isEmpty()) {
            ContentDao contentDao = new ContentDao();
            Map<String, String> fileInfo = contentDao.getFileInfo(fileId);
            
            if (!fileInfo.isEmpty()) {
                String filePath = fileInfo.get("filePath");
                String saveName = fileInfo.get("saveName");
                
                if (filePath != null && saveName != null) {
                    imagePath = request.getContextPath() + "/content/image.jsp?fileId=" + fileId;
                }
            }
        }
        
        // Set attributes for the view
        request.setAttribute("product", product);
        request.setAttribute("categories", categories);
        request.setAttribute("imagePath", imagePath);
        
        // Forward to product detail page
        request.getRequestDispatcher("/product/detail.jsp").forward(request, response);
    }
}