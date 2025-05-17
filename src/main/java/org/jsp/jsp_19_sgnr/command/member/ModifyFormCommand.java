package org.jsp.jsp_19_sgnr.command.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.io.IOException;

/**
 * Command for displaying the member information modification form.
 * This command forwards the request to the modify.jsp page.
 * It checks if the user is logged in before displaying the form.
 */
public class ModifyFormCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("member") == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
            return;
        }
        
        request.getRequestDispatcher("/WEB-INF/views/member/modify.jsp").forward(request, response);
    }
}