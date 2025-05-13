package org.jsp.jsp_19_sgnr.command.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.CategoryDao;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CategoryManageCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 관리자 권한 확인
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");

        if (member == null || !"20".equals(member.getUserType())) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        // 요청 파라미터 가져오기
        String action = request.getParameter("action");
        String categoryIdStr = request.getParameter("categoryId");

        if (categoryIdStr == null || categoryIdStr.isEmpty()) {
            redirectWithError(request, response, "카테고리 ID가 제공되지 않았습니다.");
            return;
        }

        int categoryId;
        try {
            categoryId = Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException e) {
            redirectWithError(request, response, "유효하지 않은 카테고리 ID입니다.");
            return;
        }

        CategoryDao categoryDao = new CategoryDao();
        boolean success = false;
        String message = "";

        // 액션에 따라 처리
        if ("update".equals(action)) {
            // 카테고리 이름 업데이트
            String categoryName = request.getParameter("categoryName");

            if (categoryName == null || categoryName.trim().isEmpty()) {
                redirectWithError(request, response, "카테고리 이름이 제공되지 않았습니다.");
                return;
            }

            int result = categoryDao.updateCategoryName(categoryId, categoryName.trim());
            success = (result > 0);
            message = success ? "카테고리 이름이 성공적으로 업데이트되었습니다." : "카테고리 이름 업데이트에 실패했습니다.";

        } else if ("delete".equals(action)) {
            // 카테고리 삭제 (하위 카테고리가 없는 경우에만)
            if (categoryDao.hasChildren(categoryId)) {
                redirectWithError(request, response, "하위 카테고리가 존재하여 삭제할 수 없습니다.");
                return;
            }

            int result = categoryDao.deleteCategory(categoryId);
            success = (result > 0);
            message = success ? "카테고리가 성공적으로 삭제되었습니다." : "카테고리 삭제에 실패했습니다.";

        } else if ("toggle".equals(action)) {
            // 카테고리 활성화/비활성화 토글
            int result = categoryDao.toggleCategoryStatus(categoryId);
            success = (result > 0);
            message = success ? "카테고리 상태가 성공적으로 변경되었습니다." : "카테고리 상태 변경에 실패했습니다.";

        } else {
            redirectWithError(request, response, "알 수 없는 액션입니다.");
            return;
        }

        // 결과에 따라 리다이렉트
        String status = success ? "success" : "fail";
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=categoryManage&status=" + status + "&message=" + encodedMessage);
    }

    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        response.sendRedirect(request.getContextPath() + "/admin/admin.jsp?menu=categoryManage&status=fail&message=" + encodedMessage);
    }
}
