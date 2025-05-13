package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    /**
     * 카테고리 이름 업데이트 및 하위 카테고리의 전체 경로 업데이트
     * @param categoryId 업데이트할 카테고리 ID
     * @param newName 새 카테고리 이름
     * @return 업데이트된 행 수
     */
    public int updateCategoryName(int categoryId, String newName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. 현재 카테고리 정보 조회
            String selectSql = "SELECT NB_CATEGORY, NB_PARENT_CATEGORY, NM_CATEGORY, CN_LEVEL, NM_FULL_CATEGORY FROM TB_CATEGORY WHERE NB_CATEGORY = ?";
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int level = rs.getInt("CN_LEVEL");
                String oldFullPath = rs.getString("NM_FULL_CATEGORY");
                String newFullPath = "";

                // Store parentId before closing ResultSet
                Integer parentId = null;
                if (level > 1) {
                    parentId = rs.getInt("NB_PARENT_CATEGORY");
                    if (rs.wasNull()) {
                        parentId = null;
                    }
                }

                // Close the first ResultSet
                rs.close();

                // 2. 카테고리 이름 업데이트
                pstmt.close();
                String updateSql = "UPDATE TB_CATEGORY SET NM_CATEGORY = ? WHERE NB_CATEGORY = ?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setString(1, newName);
                pstmt.setInt(2, categoryId);
                result = pstmt.executeUpdate();

                // 3. 전체 경로 계산 및 업데이트
                if (level == 1) {
                    // 1단계 카테고리인 경우 자신의 이름이 전체 경로
                    newFullPath = newName;
                } else {
                    // 2, 3단계 카테고리인 경우 상위 카테고리 경로 + 자신의 이름
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

                // 4. 자신의 전체 경로 업데이트
                pstmt.close();
                String updateFullPathSql = "UPDATE TB_CATEGORY SET NM_FULL_CATEGORY = ? WHERE NB_CATEGORY = ?";
                pstmt = conn.prepareStatement(updateFullPathSql);
                pstmt.setString(1, newFullPath);
                pstmt.setInt(2, categoryId);
                pstmt.executeUpdate();

                // 5. 하위 카테고리의 전체 경로 업데이트
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

    /**
     * 하위 카테고리의 전체 경로 업데이트
     * @param conn 데이터베이스 연결
     * @param parentId 상위 카테고리 ID
     * @param oldParentPath 이전 상위 경로
     * @param newParentPath 새 상위 경로
     * @throws SQLException SQL 예외
     */
    private void updateChildrenFullPath(Connection conn, int parentId, String oldParentPath, String newParentPath) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 1. 직접적인 자식 카테고리 조회
            String selectSql = "SELECT NB_CATEGORY, NM_CATEGORY, NM_FULL_CATEGORY FROM TB_CATEGORY WHERE NB_PARENT_CATEGORY = ?";
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setInt(1, parentId);
            rs = pstmt.executeQuery();

            // Store all child categories before closing the ResultSet
            List<ChildCategory> childCategories = new ArrayList<>();
            while (rs.next()) {
                int childId = rs.getInt("NB_CATEGORY");
                String childName = rs.getString("NM_CATEGORY");
                String oldChildPath = rs.getString("NM_FULL_CATEGORY");
                childCategories.add(new ChildCategory(childId, childName, oldChildPath));
            }

            // Close ResultSet after reading all data
            rs.close();
            rs = null;

            // Process each child category
            for (ChildCategory child : childCategories) {
                // 2. 자식 카테고리의 전체 경로 업데이트
                String newChildPath = newParentPath + " > " + child.name;

                if (pstmt != null) {
                    pstmt.close();
                }

                String updateSql = "UPDATE TB_CATEGORY SET NM_FULL_CATEGORY = ? WHERE NB_CATEGORY = ?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setString(1, newChildPath);
                pstmt.setInt(2, child.id);
                pstmt.executeUpdate();

                // 3. 재귀적으로 자식의 자식 업데이트
                updateChildrenFullPath(conn, child.id, child.oldPath, newChildPath);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }

    // Helper class to store child category information
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

    /**
     * 카테고리에 하위 카테고리가 있는지 확인
     * @param categoryId 확인할 카테고리 ID
     * @return 하위 카테고리가 있으면 true, 없으면 false
     */
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

    /**
     * 카테고리 삭제
     * @param categoryId 삭제할 카테고리 ID
     * @return 삭제된 행 수
     */
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

    /**
     * 카테고리 활성화 상태 토글
     * @param categoryId 토글할 카테고리 ID
     * @return 업데이트된 행 수
     */
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
        String sql = "SELECT NB_CATEGORY, NM_CATEGORY, NB_PARENT_CATEGORY, CN_ORDER, TO_CHAR(DA_FIRST_DATE, 'YYYY-MM-DD'), CN_LEVEL, YN_USE FROM TB_CATEGORY ORDER BY CN_ORDER";

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
