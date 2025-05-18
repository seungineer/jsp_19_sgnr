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
        // Member commands
        commandMap.put("/member/join.do", new org.jsp.jsp_19_sgnr.command.member.JoinCommand());
        commandMap.put("/member/login.do", new org.jsp.jsp_19_sgnr.command.member.LoginCommand());
        commandMap.put("/member/modify.do", new org.jsp.jsp_19_sgnr.command.member.ModifyCommand());
        commandMap.put("/member/signout.do", new org.jsp.jsp_19_sgnr.command.member.SignOutCommand());
        commandMap.put("/member/loginForm.do", new org.jsp.jsp_19_sgnr.command.member.LoginFormCommand());
        commandMap.put("/member/joinForm.do", new org.jsp.jsp_19_sgnr.command.member.JoinFormCommand());
        commandMap.put("/member/modifyForm.do", new org.jsp.jsp_19_sgnr.command.member.ModifyFormCommand());
        commandMap.put("/member/logout.do", new org.jsp.jsp_19_sgnr.command.member.LogoutCommand());
        commandMap.put("/mypage.do", new org.jsp.jsp_19_sgnr.command.member.MypageCommand());

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
        commandMap.put("/basket/update.do", new org.jsp.jsp_19_sgnr.command.basket.BasketUpdateCommand());
        commandMap.put("/basket/remove.do", new org.jsp.jsp_19_sgnr.command.basket.BasketRemoveCommand());
        commandMap.put("/basket/clear.do", new org.jsp.jsp_19_sgnr.command.basket.BasketClearCommand());
        commandMap.put("/basket/view.do", new org.jsp.jsp_19_sgnr.command.basket.BasketViewCommand());

        // Product commands
        commandMap.put("/product/list.do", new org.jsp.jsp_19_sgnr.command.product.ProductListCommand());
        commandMap.put("/product/detail.do", new org.jsp.jsp_19_sgnr.command.product.ProductDetailCommand());

        // Order commands
        commandMap.put("/order/create.do", new org.jsp.jsp_19_sgnr.command.order.OrderCreateCommand());
        commandMap.put("/order/list.do", new org.jsp.jsp_19_sgnr.command.order.OrderListCommand());
        commandMap.put("/order/detail.do", new org.jsp.jsp_19_sgnr.command.order.OrderDetailCommand());
        commandMap.put("/order/cancel.do", new org.jsp.jsp_19_sgnr.command.order.OrderCancelCommand());
        commandMap.put("/order/checkout.do", new org.jsp.jsp_19_sgnr.command.order.OrderCreateCommand());
        commandMap.put("/order/direct.do", new org.jsp.jsp_19_sgnr.command.order.OrderDirectCommand());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String commandPath = requestURI.substring(contextPath.length());

        Command command = commandMap.get(commandPath);

        if (command != null) {
            command.execute(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
