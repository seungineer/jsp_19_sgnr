package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDao {
    public int insert(Member member) {
        String sql = "INSERT INTO TB_USER (ID_USER, NM_USER, NM_PASWD, NO_MOBILE, NM_EMAIL, ST_STATUS, CD_USER_TYPE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getEmail());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getEmail());
            pstmt.setString(6, member.getStatus());
            pstmt.setString(7, member.getUserType());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean existsById(String id) {
        String sql = "SELECT * FROM TB_USER WHERE ID_USER = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Member findByIdAndPswd(String id, String paswd) {
        String sql = "SELECT * FROM TB_USER WHERE ID_USER = ? AND NM_PASWD = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, paswd);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setEmail(rs.getString("id_user"));
                    member.setPassword(rs.getString("nm_paswd"));
                    member.setName(rs.getString("nm_user"));
                    member.setEmail(rs.getString("nm_email"));
                    member.setPhone(rs.getString("no_mobile"));
                    member.setStatus(rs.getString("st_status"));
                    member.setUserType(rs.getString("cd_user_type"));

                    return member;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int update(Member member) {
        String sql = "UPDATE TB_USER " +
                "SET nm_paswd = ?, nm_user = ?, nm_email = ?, no_mobile = ?, st_status = ?, cd_user_type = ? " +
                "WHERE id_user = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getPassword());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getStatus());
            pstmt.setString(6, member.getUserType());
            pstmt.setString(7, member.getEmail());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
