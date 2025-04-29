package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDao {
    public int insert(Member member) {
        String sql = "INSERT INTO MEMBER (id, paswd, username, email, mobile, gender) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPaswd());
            pstmt.setString(3, member.getUsername());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getMobile());
            pstmt.setString(6, member.getGender());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Member findByIdAndPswd(String id, String paswd) {
        String sql = "SELECT * FROM MEMBER WHERE id = ? AND paswd = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, paswd);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setId(rs.getString("id"));
                    member.setPaswd(rs.getString("paswd"));
                    member.setUsername(rs.getString("username"));
                    member.setEmail(rs.getString("email"));
                    member.setMobile(rs.getString("mobile"));
                    member.setGender(rs.getString("gender"));

                    return member;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int update(Member member) {
        String sql = "UPDATE MEMBER " +
                "SET paswd = ?, username = ?, email = ?, mobile = ?, gender = ? " +
                "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getPaswd());
            pstmt.setString(2, member.getUsername());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getMobile());
            pstmt.setString(5, member.getGender());
            pstmt.setString(6, member.getId());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
