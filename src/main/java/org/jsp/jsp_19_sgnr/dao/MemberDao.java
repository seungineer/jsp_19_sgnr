package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.jsp.jsp_19_sgnr.db.DBConnection.getConnection;

public class MemberDao {
    public int insert(Member member) {
        String sql = "INSERT INTO TB_USER (ID_USER, NM_USER, NM_PASWD, NO_MOBILE, NM_EMAIL, ST_STATUS, CD_USER_TYPE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
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

        try (Connection conn = getConnection();
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
        String sql = "SELECT * FROM TB_USER WHERE ST_STATUS like 'ST01' AND ID_USER = ? AND NM_PASWD = ?";

        try (Connection conn = getConnection();
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

    public List<Member> findAllExceptAdmin() {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM TB_USER WHERE CD_USER_TYPE != '20'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Member m = new Member();
                m.setEmail(rs.getString("ID_USER"));
                m.setName(rs.getString("NM_USER"));
                m.setEmail(rs.getString("NM_EMAIL"));
                m.setPhone(rs.getString("NO_MOBILE"));
                m.setStatus(rs.getString("ST_STATUS"));
                m.setUserType(rs.getString("CD_USER_TYPE"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Member> findOnlyAdmin() {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM TB_USER WHERE CD_USER_TYPE = '20'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Member m = new Member();
                m.setEmail(rs.getString("ID_USER"));
                m.setName(rs.getString("NM_USER"));
                m.setEmail(rs.getString("NM_EMAIL"));
                m.setPhone(rs.getString("NO_MOBILE"));
                m.setStatus(rs.getString("ST_STATUS"));
                m.setUserType(rs.getString("CD_USER_TYPE"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Member> findByStatusAndUserType(String status, String userType) {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM TB_USER WHERE ST_STATUS = ? AND CD_USER_TYPE = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, userType);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Member m = new Member();
                    m.setEmail(rs.getString("ID_USER"));
                    m.setName(rs.getString("NM_USER"));
                    m.setEmail(rs.getString("NM_EMAIL"));
                    m.setPhone(rs.getString("NO_MOBILE"));
                    m.setStatus(rs.getString("ST_STATUS"));
                    m.setUserType(rs.getString("CD_USER_TYPE"));
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Member findById(String id) {
        String sql = "SELECT * FROM TB_USER WHERE ID_USER = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Member m = new Member();
                    m.setEmail(rs.getString("ID_USER"));
                    m.setName(rs.getString("NM_USER"));
                    m.setEmail(rs.getString("NM_EMAIL"));
                    m.setPhone(rs.getString("NO_MOBILE"));
                    m.setStatus(rs.getString("ST_STATUS"));
                    m.setUserType(rs.getString("CD_USER_TYPE"));
                    return m;
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

        try (Connection conn = getConnection();
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

    public void updateMemberInfo(String id, String name, String mobile, String status, String userType) {
        String sql = "UPDATE TB_USER SET NM_USER = ?, NO_MOBILE = ?, ST_STATUS = ?, CD_USER_TYPE = ? WHERE ID_USER = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, mobile);
            pstmt.setString(3, status);
            pstmt.setString(4, userType);
            pstmt.setString(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateForRejoin(Member member) {
        String sql = "UPDATE TB_USER SET NM_USER = ?, NM_PASWD = ?, NO_MOBILE = ?, ST_STATUS = ?, DA_FIRST_DATE = SYSDATE WHERE ID_USER = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getStatus());
            pstmt.setString(5, member.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}