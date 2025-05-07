package org.jsp.jsp_19_sgnr.servlet.category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.dao.CategoryDao;
import org.jsp.jsp_19_sgnr.dto.Category;

import java.io.IOException;

@WebServlet("/admin/insertCategory")
public class CategoryInsertServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String regName = request.getParameter("regName");
        String name = request.getParameter("name");
        String levelStr = request.getParameter("level");
        String upperStr = request.getParameter("upperId");
        String fullCategory = request.getParameter("fullName");

        int level = Integer.parseInt(levelStr);
        Integer upperId = (upperStr != null && !upperStr.isEmpty()) ? Integer.parseInt(upperStr) : null;

        CategoryDao categoryDao = new CategoryDao();

        int order = 1;
        if (upperId != null) {
            order = categoryDao.getNextOrder(upperId);
        }

        Category category = new Category();
        category.setName(name);
        category.setLevel(level);
        category.setUpperId(upperId);
        category.setOrder(order);
        category.setFullname(fullCategory);
        category.setRegName(regName);

        int result = categoryDao.insert(category);

        if (result > 0) {
            response.sendRedirect("admin.jsp?menu=category&status=success");
        } else {
            response.sendRedirect("admin.jsp?menu=category&status=fail");
        }
    }
}