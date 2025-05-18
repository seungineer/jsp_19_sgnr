package org.jsp.jsp_19_sgnr.command.product;

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
 * Command for handling product listing with filtering and sorting options for regular users.
 * Admin users are redirected to the admin version of this command.
 */
public class ProductListCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryIdStr = request.getParameter("categoryId");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");
        String keyword = request.getParameter("keyword");
        String pageStr = request.getParameter("page");

        // Default sort is by category name ascending
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "category";
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "asc";
        }

        // Default page is 1
        int currentPage = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageStr);
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } catch (NumberFormatException e) {
                // If invalid, use default
                currentPage = 1;
            }
        }

        // Default page size is 9
        int pageSize = 9;

        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.findAll();
        request.setAttribute("categories", categories);

        ProductDao productDao = new ProductDao();
        List<Product> products;
        int totalProducts = 0;

        if (keyword != null && !keyword.isEmpty()) {
            products = productDao.searchPaginatedProductsByKeyword(keyword, currentPage, pageSize);
            totalProducts = productDao.getTotalProductCountByKeyword(keyword);
            request.setAttribute("keyword", keyword);
        }
        else if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryIdStr);
                products = productDao.findPaginatedProductsByCategory(categoryId, currentPage, pageSize);
                totalProducts = productDao.getTotalProductCountByCategory(categoryId);
                request.setAttribute("selectedCategoryId", categoryId);
            } catch (NumberFormatException e) {
                products = productDao.getPaginatedProducts(currentPage, pageSize);
                totalProducts = productDao.getTotalProductCount();
            }
        } else {
            products = productDao.getPaginatedProducts(currentPage, pageSize);
            totalProducts = productDao.getTotalProductCount();
        }

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        // If current page is greater than total pages, set to last page
        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
            // Re-fetch products with corrected page
            if (keyword != null && !keyword.isEmpty()) {
                products = productDao.searchPaginatedProductsByKeyword(keyword, currentPage, pageSize);
            }
            else if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                try {
                    int categoryId = Integer.parseInt(categoryIdStr);
                    products = productDao.findPaginatedProductsByCategory(categoryId, currentPage, pageSize);
                } catch (NumberFormatException e) {
                    products = productDao.getPaginatedProducts(currentPage, pageSize);
                }
            } else {
                products = productDao.getPaginatedProducts(currentPage, pageSize);
            }
        }

        // Sort products if needed (for category sorting which is done in memory)
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
        } else if ("price".equals(sortBy) && products.size() > 1) {
            // Only sort in memory if we have more than one product
            products.sort((p1, p2) -> {
                int result = Integer.compare(p1.getQt_sale_price(), p2.getQt_sale_price());
                return "asc".equals(finalSortOrder) ? result : -result;
            });
        }

        // Set pagination attributes
        request.setAttribute("products", products);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);

        // Forward to productList.jsp for regular users
        request.getRequestDispatcher("/WEB-INF/views/product/productList.jsp").forward(request, response);
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
