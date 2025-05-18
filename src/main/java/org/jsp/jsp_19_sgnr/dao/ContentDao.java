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

public class ContentDao {

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

    public String generateSaveFileName(String originalFileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extension = "";

        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }

        return timestamp + extension;
    }

    public String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }

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

    public boolean deleteFileIfNotUsed(String fileId, String productId, Connection conn) throws SQLException {

        if (fileId == null || fileId.isEmpty()) {
            return true;
        }

        String checkSql = "SELECT COUNT(*) FROM TB_PRODUCT WHERE ID_FILE = ? AND NO_PRODUCT != ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, fileId);
            checkStmt.setString(2, productId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    int count = rs.getInt(1);
                    return false;
                }
            }
        } catch (SQLException e) {
            throw e;
        }

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
