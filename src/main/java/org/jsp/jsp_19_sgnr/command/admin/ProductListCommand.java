package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.CategoryDao;
import org.jsp.jsp_19_sgnr.dao.ProductDao;
import org.jsp.jsp_19_sgnr.dto.Category;
import org.jsp.jsp_19_sgnr.dto.Member;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Command for handling product listing with filtering and sorting options in the admin panel.
 */
public class ProductListCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check admin privileges
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");

        if (member == null || !"20".equals(member.getUserType())) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }

        // Get filter and sort parameters
        String categoryIdStr = request.getParameter("categoryId");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");

        // Default sort is by category name ascending
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "category";
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "asc";
        }

        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.findAll();
        request.setAttribute("categories", categories);

        ProductDao productDao = new ProductDao();
        List<Product> products;

        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryIdStr);
                products = productDao.findProductsByCategory(categoryId);
                request.setAttribute("selectedCategoryId", categoryId);
            } catch (NumberFormatException e) {
                products = productDao.getAllProducts();
            }
        } else {
            products = productDao.getAllProducts();
        }

        // Sort products
        final String finalSortOrder = sortOrder; // Make effectively final for lambda

        if ("category".equals(sortBy)) {
            // For category sorting, we need to get the category names for each product
            // Get all category mappings in a single query to avoid N+1 problem
            Map<String, List<Integer>> allCategoryMappings = productDao.getAllCategoryMappings();

            products.sort((p1, p2) -> {
                List<Integer> cats1 = allCategoryMappings.getOrDefault(p1.getNo_product(), List.of());
                List<Integer> cats2 = allCategoryMappings.getOrDefault(p2.getNo_product(), List.of());

                String cat1Name = getCategoryName(cats1, categories);
                String cat2Name = getCategoryName(cats2, categories);

                return "asc".equals(finalSortOrder) 
                    ? cat1Name.compareTo(cat2Name) 
                    : cat2Name.compareTo(cat1Name);
            });
        } else if ("price".equals(sortBy)) {
            products.sort((p1, p2) -> {
                int result = Integer.compare(p1.getQt_sale_price(), p2.getQt_sale_price());
                return "asc".equals(finalSortOrder) ? result : -result;
            });
        }

        request.setAttribute("products", products);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);

        // Forward to admin.jsp with productList menu
        request.getRequestDispatcher("/admin/admin.jsp?menu=productList").forward(request, response);
    }

    /**
     * Helper method to get the first category name for a product.
     * In a real application, you might want to handle multiple categories differently.
     */
    private String getCategoryName(List<Integer> categoryIds, List<Category> allCategories) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return "";
        }

        int firstCategoryId = categoryIds.get(0);
        for (Category category : allCategories) {
            if (category.getId() == firstCategoryId) {
                return category.getName() != null ? category.getName() : "";
            }
        }

        return "";
    }
}
