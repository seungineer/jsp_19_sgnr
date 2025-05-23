<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.jsp.jsp_19_sgnr.db.DBConnection" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%
    String idFile = request.getParameter("id");
    if (idFile == null || idFile.trim().isEmpty()) {
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

        String sql = "SELECT BO_SAVE_FILE, CD_FILE_TYPE FROM TB_CONTENT WHERE ID_FILE = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, idFile);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            Blob blob = rs.getBlob("BO_SAVE_FILE");
            String contentType = rs.getString("CD_FILE_TYPE");

            if (blob != null) {
                imageStream = blob.getBinaryStream();

                if (contentType != null && !contentType.trim().isEmpty()) {
                    response.setContentType(contentType);
                } else {
                    response.setContentType("image/jpeg");
                }

                response.setHeader("Cache-Control", "max-age=86400"); // Cache for 1 day

                byte[] buffer = new byte[4096];
                int bytesRead;
                ServletOutputStream outputStream = response.getOutputStream();

                while ((bytesRead = imageStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
                return;
            }
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");

    } catch (Exception e) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving image: " + e.getMessage());
    } finally {
        try { if (imageStream != null) imageStream.close(); } catch (Exception e) {}
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
%>
