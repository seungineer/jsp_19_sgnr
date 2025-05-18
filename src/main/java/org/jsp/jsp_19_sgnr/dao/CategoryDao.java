package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public List<Category> findByIds(List<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < categoryIds.size(); i++) {
            if (i > 0) {
                placeholders.append(", ");
            }
            placeholders.append("?");
        }

        String sql = "SELECT * FROM TB_CATEGORY WHERE NB_CATEGORY IN (" + placeholders.toString() + ")";

        List<Category> categories = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < categoryIds.size(); i++) {
                ps.setInt(i + 1, categoryIds.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("NB_CATEGORY"));
                    category.setName(rs.getString("NM_CATEGORY"));
                    category.setUpperId(rs.getInt("NB_PARENT_CATEGORY"));
                    category.setOrder(rs.getInt("CN_ORDER"));
                    category.setRegDate(rs.getString("DA_FIRST_DATE"));
                    category.setLevel(rs.getInt("CN_LEVEL"));
                    category.setYnUse(rs.getString("YN_USE"));
                    category.setFullname(rs.getString("NM_FULL_CATEGORY"));
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public int updateCategoryName(int categoryId, String newName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String selectSql = "SELECT NB_CATEGORY, NB_PARENT_CATEGORY, NM_CATEGORY, CN_LEVEL, NM_FULL_CATEGORY FROM TB_CATEGORY WHERE NB_CATEGORY = ?";
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int level = rs.getInt("CN_LEVEL");
                String oldFullPath = rs.getString("NM_FULL_CATEGORY");
                String newFullPath = "";

                Integer parentId = null;
                if (level > 1) {
                    parentId = rs.getInt("NB_PARENT_CATEGORY");
                    if (rs.wasNull()) {
                        parentId = null;
                    }
                }

                rs.close();

                pstmt.close();
                String updateSql = "UPDATE TB_CATEGORY SET NM_CATEGORY = ? WHERE NB_CATEGORY = ?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setString(1, newName);
                pstmt.setInt(2, categoryId);
                result = pstmt.executeUpdate();

                if (level == 1) {
                    newFullPath = newName;
                } else {
                    if (parentId != null) {
                        pstmt.close();

                        String parentSql = "SELECT NM_FULL_CATEGORY FROM TB_CATEGORY WHERE NB_CATEGORY = ?";
                        pstmt = conn.prepareStatement(parentSql);
                        pstmt.setInt(1, parentId);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            String parentFullPath = rs.getString("NM_FULL_CATEGORY");
                            newFullPath = parentFullPath + " > " + newName;
                        }
                        rs.close();
                    }
                }

                pstmt.close();
                String updateFullPathSql = "UPDATE TB_CATEGORY SET NM_FULL_CATEGORY = ? WHERE NB_CATEGORY = ?";
                pstmt = conn.prepareStatement(updateFullPathSql);
                pstmt.setString(1, newFullPath);
                pstmt.setInt(2, categoryId);
                pstmt.executeUpdate();

                updateChildrenFullPath(conn, categoryId, oldFullPath, newFullPath);

                conn.commit();
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private void updateChildrenFullPath(Connection conn, int parentId, String oldParentPath, String newParentPath) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String selectSql = "SELECT NB_CATEGORY, NM_CATEGORY, NM_FULL_CATEGORY FROM TB_CATEGORY WHERE NB_PARENT_CATEGORY = ?";
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setInt(1, parentId);
            rs = pstmt.executeQuery();

            List<ChildCategory> childCategories = new ArrayList<>();
            while (rs.next()) {
                int childId = rs.getInt("NB_CATEGORY");
                String childName = rs.getString("NM_CATEGORY");
                String oldChildPath = rs.getString("NM_FULL_CATEGORY");
                childCategories.add(new ChildCategory(childId, childName, oldChildPath));
            }

            rs.close();
            rs = null;

            for (ChildCategory child : childCategories) {
                String newChildPath = newParentPath + " > " + child.name;

                if (pstmt != null) {
                    pstmt.close();
                }

                String updateSql = "UPDATE TB_CATEGORY SET NM_FULL_CATEGORY = ? WHERE NB_CATEGORY = ?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setString(1, newChildPath);
                pstmt.setInt(2, child.id);
                pstmt.executeUpdate();

                updateChildrenFullPath(conn, child.id, child.oldPath, newChildPath);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }

    private static class ChildCategory {
        final int id;
        final String name;
        final String oldPath;

        ChildCategory(int id, String name, String oldPath) {
            this.id = id;
            this.name = name;
            this.oldPath = oldPath;
        }
    }

    public boolean hasChildren(int categoryId) {
        String sql = "SELECT COUNT(*) FROM TB_CATEGORY WHERE NB_PARENT_CATEGORY = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int deleteCategory(int categoryId) {
        String sql = "DELETE FROM TB_CATEGORY WHERE NB_CATEGORY = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int toggleCategoryStatus(int categoryId) {
        String sql = "UPDATE TB_CATEGORY SET YN_USE = CASE WHEN YN_USE = 'Y' THEN 'N' ELSE 'Y' END WHERE NB_CATEGORY = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

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
        String sql = "SELECT NB_CATEGORY, NM_CATEGORY, NB_PARENT_CATEGORY, CN_ORDER, TO_CHAR(DA_FIRST_DATE, 'YYYY-MM-DD'), CN_LEVEL, YN_USE, NM_FULL_CATEGORY FROM TB_CATEGORY ORDER BY CN_ORDER";

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
                c.setYnUse(rs.getString(7));
                c.setFullname(rs.getString(8));
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

    public List<Category> findLeafCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM TB_CATEGORY c " +
                "WHERE NOT EXISTS (SELECT 1 FROM TB_CATEGORY child WHERE child.nb_parent_category = c.nb_category)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("nb_category"));
                c.setFullname(rs.getString("nm_full_category"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
