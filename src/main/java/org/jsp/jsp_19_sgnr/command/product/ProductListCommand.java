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

public class ProductListCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryIdStr = request.getParameter("categoryId");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");
        String keyword = request.getParameter("keyword");
        String pageStr = request.getParameter("page");

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "category";
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "asc";
        }

        int currentPage = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageStr);
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

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

        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
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

        final String finalSortOrder = sortOrder; // Make effectively final for lambda

        if ("category".equals(sortBy)) {
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
            products.sort((p1, p2) -> {
                int result = Integer.compare(p1.getQt_sale_price(), p2.getQt_sale_price());
                return "asc".equals(finalSortOrder) ? result : -result;
            });
        }

        request.setAttribute("products", products);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);

        request.getRequestDispatcher("/WEB-INF/views/product/productList.jsp").forward(request, response);
    }

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
