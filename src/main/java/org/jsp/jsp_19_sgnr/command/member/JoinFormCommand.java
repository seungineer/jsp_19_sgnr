package org.jsp.jsp_19_sgnr.command.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;

import java.io.IOException;

/**
 * Command for displaying the join (registration) form.
 * This command forwards the request to the join.jsp page.
 */
public class JoinFormCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/member/join.jsp").forward(request, response);
    }
}