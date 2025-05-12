package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.CategoryDao;
import org.jsp.jsp_19_sgnr.dto.Category;

import java.io.IOException;

/**
 * Command implementation for handling category registration.
 */
public class CategoryRegisterCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
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
            response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=category&status=success");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=category&status=fail");
        }
    }
}