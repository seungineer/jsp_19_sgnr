package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public int insert(Category category) {
        String sql = "INSERT INTO TB_CATEGORY (NB_CATEGORY, NM_CATEGORY, NB_PARENT_CATEGORY, CN_ORDER, DA_FIRST_DATE, CN_LEVEL, NM_FULL_CATEGORY, YN_USE, NO_REGISTER) " +
                "VALUES (SEQ_CATEGORY.NEXTVAL, ?, ?, ?, SYSDATE, ?, ?, 'Y', ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getName());

            if (category.getUpperId() != null) {
                pstmt.setInt(2, category.getUpperId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }

            pstmt.setInt(3, category.getOrder());
            pstmt.setInt(4, category.getLevel());
            pstmt.setString(5, category.getFullname());
            pstmt.setString(6, category.getRegName());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT NB_CATEGORY, NM_CATEGORY, NB_PARENT_CATEGORY, CN_ORDER, TO_CHAR(DA_FIRST_DATE, 'YYYY-MM-DD'), CN_LEVEL FROM TB_CATEGORY ORDER BY CN_ORDER";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt(1));
                c.setName(rs.getString(2));
                int upper = rs.getInt(3);
                c.setUpperId(rs.wasNull() ? null : upper);
                c.setOrder(rs.getInt(4));
                c.setRegDate(rs.getString(5));
                c.setLevel(rs.getInt(6));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getNextOrder(int upperId) {
        String sql = "SELECT NVL(MAX(CN_ORDER), 0) + 1 FROM TB_CATEGORY WHERE NB_PARENT_CATEGORY = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, upperId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
