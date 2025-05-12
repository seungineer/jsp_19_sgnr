package org.jsp.jsp_19_sgnr.command;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Command interface for the Command Pattern implementation.
 * All command classes should implement this interface.
 */
public interface Command {
    /**
     * Executes the command with the given request and response.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}