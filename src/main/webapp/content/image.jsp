<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.jsp.jsp_19_sgnr.db.DBConnection" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%
    // Check for both parameter names (id and fileId) for backward compatibility
    String idFile = request.getParameter("id");
    if (idFile == null || idFile.trim().isEmpty()) {
        // Try the alternative parameter name
        idFile = request.getParameter("fileId");
        if (idFile == null || idFile.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image ID is required");
            return;
        }
    }

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    InputStream imageStream = null;

    try {
        conn = DBConnection.getConnection();

        // Query to get the image data and content type
        String sql = "SELECT BO_SAVE_FILE, CD_FILE_TYPE FROM TB_CONTENT WHERE ID_FILE = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, idFile);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            // Get the binary data and content type
            Blob blob = rs.getBlob("BO_SAVE_FILE");
            String contentType = rs.getString("CD_FILE_TYPE");

            if (blob != null) {
                imageStream = blob.getBinaryStream();

                // Set the content type
                if (contentType != null && !contentType.trim().isEmpty()) {
                    response.setContentType(contentType);
                } else {
                    // Default to JPEG if content type is not specified
                    response.setContentType("image/jpeg");
                }

                // Set cache control headers
                response.setHeader("Cache-Control", "max-age=86400"); // Cache for 1 day

                // Stream the image data to the client
                byte[] buffer = new byte[4096];
                int bytesRead;
                ServletOutputStream outputStream = response.getOutputStream();

                while ((bytesRead = imageStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
                return; // Important: prevent further processing
            }
        }

        // If we get here, the image was not found
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");

    } catch (Exception e) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving image: " + e.getMessage());
    } finally {
        // Close resources
        try { if (imageStream != null) imageStream.close(); } catch (Exception e) {}
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
%>
