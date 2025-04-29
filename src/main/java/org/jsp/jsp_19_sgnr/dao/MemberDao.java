package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
