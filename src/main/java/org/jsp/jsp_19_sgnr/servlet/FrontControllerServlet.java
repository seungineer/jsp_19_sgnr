package org.jsp.jsp_19_sgnr.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Front Controller Servlet that handles all requests and delegates to appropriate Command implementations.
 * This is the single entry point for all requests in the application.
 */
@WebServlet(urlPatterns = {"*.do"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 1024 * 1024 * 10,  // 10MB
    maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class FrontControllerServlet extends HttpServlet {

    private Map<String, Command> commandMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        // Initialize the command map with URI-Command mappings

        // Member commands
        commandMap.put("/member/join.do", new org.jsp.jsp_19_sgnr.command.member.JoinCommand());
        commandMap.put("/member/login.do", new org.jsp.jsp_19_sgnr.command.member.LoginCommand());
        commandMap.put("/member/modify.do", new org.jsp.jsp_19_sgnr.command.member.ModifyCommand());
        commandMap.put("/member/signout.do", new org.jsp.jsp_19_sgnr.command.member.SignOutCommand());

        // Admin commands
        commandMap.put("/admin/category/register.do", new org.jsp.jsp_19_sgnr.command.admin.CategoryRegisterCommand());
        commandMap.put("/admin/category/manage.do", new org.jsp.jsp_19_sgnr.command.admin.CategoryManageCommand());
        commandMap.put("/admin/product/register.do", new org.jsp.jsp_19_sgnr.command.admin.ProductRegisterCommand());
        commandMap.put("/admin/product/modify.do", new org.jsp.jsp_19_sgnr.command.admin.ProductModifyCommand());
        commandMap.put("/admin/product/category-mapping.do", new org.jsp.jsp_19_sgnr.command.admin.ProductCategoryMappingCommand());
        commandMap.put("/admin/product/list.do", new org.jsp.jsp_19_sgnr.command.admin.ProductListCommand());
        commandMap.put("/admin/member/approve.do", new org.jsp.jsp_19_sgnr.command.admin.ApproveMemberCommand());
        commandMap.put("/admin/member/withdraw.do", new org.jsp.jsp_19_sgnr.command.admin.ProcessWithdrawalCommand());
        commandMap.put("/admin/member/update.do", new org.jsp.jsp_19_sgnr.command.admin.UpdateMemberCommand());

        // Basket commands
        commandMap.put("/basket/add.do", new org.jsp.jsp_19_sgnr.command.basket.BasketAddCommand());

        // Product commands
        commandMap.put("/product/list.do", new org.jsp.jsp_19_sgnr.command.admin.ProductListCommand());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Get the request URI
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String commandPath = requestURI.substring(contextPath.length());

        // Find the appropriate command
        Command command = commandMap.get(commandPath);

        if (command != null) {
            // Execute the command
            command.execute(request, response);
        } else {
            // If no command is found, send 404 error
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
