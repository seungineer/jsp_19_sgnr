package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Data Access Object for TB_CONTENT table.
 * Handles operations related to content files (images, etc.)
 */
public class ContentDao {

    /**
     * Inserts a file into the TB_CONTENT table.
     * 
     * @param originalFileName The original file name
     * @param saveFileName The name to save the file as
     * @param filePath The path where the file is saved
     * @param fileData The binary data of the file
     * @param fileExt The file extension
     * @param fileType The MIME type of the file
     * @param registerId The ID of the user who registered the file
     * @param conn The database connection to use (for transaction support)
     * @return The generated file ID if successful, null otherwise
     */
    public String insertFile(
            String originalFileName,
            String saveFileName,
            String filePath,
            InputStream fileData,
            String fileExt,
            String fileType,
            String registerId,
            Connection conn) throws SQLException {

        String fileId = "FILE" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);

        String sql = "INSERT INTO TB_CONTENT (" +
                "ID_FILE, NM_ORG_FILE, NM_SAVE_FILE, NM_FILE_PATH, " +
                "BO_SAVE_FILE, NM_FILE_EXT, CD_FILE_TYPE, " +
                "NO_REGISTER, DA_FIRST_DATE" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fileId);
            pstmt.setString(2, originalFileName);
            pstmt.setString(3, saveFileName);
            pstmt.setString(4, filePath);
            pstmt.setBinaryStream(5, fileData);
            pstmt.setString(6, fileExt);
            pstmt.setString(7, fileType);
            pstmt.setString(8, registerId);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                return fileId;
            } else {
                return null;
            }
        }
    }

    /**
     * Generates a unique file name for saving.
     * 
     * @param originalFileName The original file name
     * @return A unique file name based on timestamp
     */
    public String generateSaveFileName(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extension = "";

        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }

        return timestamp + extension;
    }

    /**
     * Extracts the file extension from a file name.
     * 
     * @param fileName The file name
     * @return The file extension (e.g., ".jpg")
     */
    public String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }

    /**
     * Retrieves file information by file ID.
     * 
     * @param fileId The file ID to look up
     * @return A map containing file information (path, name, extension, etc.)
     */
    public Map<String, String> getFileInfo(String fileId) {
        Map<String, String> fileInfo = new HashMap<>();

        if (fileId == null || fileId.isEmpty()) {
            return fileInfo;
        }

        String sql = "SELECT NM_ORG_FILE, NM_SAVE_FILE, NM_FILE_PATH, NM_FILE_EXT, CD_FILE_TYPE " +
                     "FROM TB_CONTENT WHERE ID_FILE = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fileId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    fileInfo.put("originalName", rs.getString("NM_ORG_FILE"));
                    fileInfo.put("saveName", rs.getString("NM_SAVE_FILE"));
                    fileInfo.put("filePath", rs.getString("NM_FILE_PATH"));
                    fileInfo.put("fileExt", rs.getString("NM_FILE_EXT"));
                    fileInfo.put("fileType", rs.getString("CD_FILE_TYPE"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fileInfo;
    }

    /**
     * Checks if a file is used by any products other than the specified one.
     * If not, deletes the file from TB_CONTENT.
     * 
     * @param fileId The file ID to check
     * @param productId The product ID being deleted (to exclude from the check)
     * @param conn The database connection to use
     * @return true if the file was deleted or if fileId is null, false if the file is still in use or if an error occurred
     * @throws SQLException If a database error occurs
     */
    public boolean deleteFileIfNotUsed(String fileId, String productId, Connection conn) throws SQLException {

        if (fileId == null || fileId.isEmpty()) {
            return true; // No file to delete
        }

        // Check if the file is used by other products
        String checkSql = "SELECT COUNT(*) FROM TB_PRODUCT WHERE ID_FILE = ? AND NO_PRODUCT != ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, fileId);
            checkStmt.setString(2, productId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // File is used by other products, don't delete
                    int count = rs.getInt(1);
                    return false;
                }
            }
        } catch (SQLException e) {
            throw e;
        }

        // File is not used by other products, delete it
        String deleteSql = "DELETE FROM TB_CONTENT WHERE ID_FILE = ?";

        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setString(1, fileId);

            int result = deleteStmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw e;
        }
    }
}
