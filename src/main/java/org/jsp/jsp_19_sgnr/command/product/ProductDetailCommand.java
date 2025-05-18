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

public class ProductDetailCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        
        if (productId == null || productId.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/product/list.do");
            return;
        }
        
        ProductDao productDao = new ProductDao();
        Product product = productDao.getProductById(productId);
        
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/product/list.do");
            return;
        }
        
        List<Integer> categoryIds = productDao.getCategoryMappings(productId);
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.findByIds(categoryIds);
        
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
        
        request.setAttribute("product", product);
        request.setAttribute("categories", categories);
        request.setAttribute("imagePath", imagePath);
        
        request.getRequestDispatcher("/product/detail.jsp").forward(request, response);
    }
}