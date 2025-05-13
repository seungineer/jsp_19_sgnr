package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.jsp.jsp_19_sgnr.db.DBConnection.getConnection;

public class ProductDao {

    /**
     * Finds all products mapped to a specific category.
     * 
     * @param categoryId The category ID
     * @return List of products mapped to the category
     */
    public List<Product> findProductsByCategory(int categoryId) {
        String sql = "SELECT p.* FROM TB_PRODUCT p " +
                "JOIN TB_CATEGORY_PRODUCT_MAPPING m ON p.NO_PRODUCT = m.no_product " +
                "WHERE m.nb_category = ? " +
                "ORDER BY m.cn_order";

        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setNo_product(rs.getString("NO_PRODUCT"));
                    product.setNm_product(rs.getString("NM_PRODUCT"));
                    product.setNm_detail_explain(rs.getString("NM_DETAIL_EXPLAIN"));
                    product.setDt_start_date(rs.getString("DT_START_DATE"));
                    product.setDt_end_date(rs.getString("DT_END_DATE"));
                    product.setQt_customer_price(rs.getInt("QT_CUSTOMER_PRICE"));
                    product.setQt_sale_price(rs.getInt("QT_SALE_PRICE"));
                    product.setQt_stock(rs.getInt("QT_STOCK"));
                    product.setQt_delivery_fee(rs.getInt("QT_DELIVERY_FEE"));
                    product.setNo_register(rs.getString("NO_REGISTER"));
                    product.setSale_status(rs.getInt("SALE_STATUS"));
                    products.add(product);
                }
            }

            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return products;
        }
    }

    /**
     * Finds all products not mapped to a specific category.
     * 
     * @param categoryId The category ID
     * @return List of products not mapped to the category
     */
    public List<Product> findUnmappedProducts(int categoryId) {
        String sql = "SELECT * FROM TB_PRODUCT p " +
                "WHERE NOT EXISTS (" +
                "    SELECT 1 FROM TB_CATEGORY_PRODUCT_MAPPING m " +
                "    WHERE m.no_product = p.NO_PRODUCT AND m.nb_category = ?" +
                ") " +
                "ORDER BY p.DA_FIRST_DATE DESC";

        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setNo_product(rs.getString("NO_PRODUCT"));
                    product.setNm_product(rs.getString("NM_PRODUCT"));
                    product.setNm_detail_explain(rs.getString("NM_DETAIL_EXPLAIN"));
                    product.setDt_start_date(rs.getString("DT_START_DATE"));
                    product.setDt_end_date(rs.getString("DT_END_DATE"));
                    product.setQt_customer_price(rs.getInt("QT_CUSTOMER_PRICE"));
                    product.setQt_sale_price(rs.getInt("QT_SALE_PRICE"));
                    product.setQt_stock(rs.getInt("QT_STOCK"));
                    product.setQt_delivery_fee(rs.getInt("QT_DELIVERY_FEE"));
                    product.setNo_register(rs.getString("NO_REGISTER"));
                    product.setSale_status(rs.getInt("SALE_STATUS"));
                    products.add(product);
                }
            }

            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return products;
        }
    }

    /**
     * Adds a product to a category.
     * 
     * @param categoryId The category ID
     * @param productId The product ID
     * @param registerName The name of the user registering the mapping
     * @return true if successful, false otherwise
     */
    public boolean addProductToCategory(int categoryId, String productId, String registerName) {
        String sqlCheckExisting = "SELECT COUNT(*) FROM TB_CATEGORY_PRODUCT_MAPPING " +
                "WHERE nb_category = ? AND no_product = ?";

        String sqlGetMaxOrder = "SELECT NVL(MAX(cn_order), 0) + 1 FROM TB_CATEGORY_PRODUCT_MAPPING " +
                "WHERE nb_category = ?";

        String sqlInsertMapping = "INSERT INTO TB_CATEGORY_PRODUCT_MAPPING " +
                "(nb_category, no_product, cn_order, no_register, da_first_date) " +
                "VALUES (?, ?, ?, ?, SYSDATE)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Check if mapping already exists
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheckExisting)) {
                psCheck.setInt(1, categoryId);
                psCheck.setString(2, productId);

                try (var rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Mapping already exists
                        return true;
                    }
                }
            }

            // Get the next order number
            int nextOrder = 1;
            try (PreparedStatement psOrder = conn.prepareStatement(sqlGetMaxOrder)) {
                psOrder.setInt(1, categoryId);

                try (var rs = psOrder.executeQuery()) {
                    if (rs.next()) {
                        nextOrder = rs.getInt(1);
                    }
                }
            }

            // Insert the mapping
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsertMapping)) {
                psInsert.setInt(1, categoryId);
                psInsert.setString(2, productId);
                psInsert.setInt(3, nextOrder);
                psInsert.setString(4, registerName);
                psInsert.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes a product from a category.
     * 
     * @param categoryId The category ID
     * @param productId The product ID
     * @return true if successful, false otherwise
     */
    public boolean removeProductFromCategory(int categoryId, String productId) {
        String sql = "DELETE FROM TB_CATEGORY_PRODUCT_MAPPING " +
                "WHERE nb_category = ? AND no_product = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ps.setString(2, productId);

            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Inserts category mappings for a product.
     * 
     * @param productId   The product ID
     * @param categoryIds Array of category IDs
     * @param registerName The name of the user registering the mapping
     * @return true if successful, false otherwise
     */
    public boolean insertCategoryMappings(String productId, String[] categoryIds, String registerName) {
        String sqlCheckExisting = "SELECT COUNT(*) FROM TB_CATEGORY_PRODUCT_MAPPING " +
                "WHERE nb_category = ? AND no_product = ?";

        String sqlInsertMapping = "INSERT INTO TB_CATEGORY_PRODUCT_MAPPING " +
                "(nb_category, no_product, cn_order, no_register, da_first_date) " +
                "VALUES (?, ?, ?, ?, SYSDATE)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheckExisting);
                 PreparedStatement psInsert = conn.prepareStatement(sqlInsertMapping)) {

                for (int i = 0; i < categoryIds.length; i++) {
                    int categoryId = Integer.parseInt(categoryIds[i]);

                    // Check if mapping already exists
                    psCheck.setInt(1, categoryId);
                    psCheck.setString(2, productId);

                    boolean exists = false;
                    try (var rs = psCheck.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            exists = true;
                        }
                    }

                    // Skip if mapping already exists
                    if (!exists) {
                        psInsert.setInt(1, categoryId);
                        psInsert.setString(2, productId);
                        psInsert.setInt(3, i + 1);
                        psInsert.setString(4, registerName);
                        psInsert.addBatch();
                    }
                }

                psInsert.executeBatch();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets all category mappings for a product.
     * 
     * @param productId The product ID
     * @return Array of category IDs mapped to the product
     */
    public List<Integer> getCategoryMappings(String productId) {
        String sql = "SELECT nb_category FROM TB_CATEGORY_PRODUCT_MAPPING " +
                "WHERE no_product = ? ORDER BY cn_order";

        List<Integer> categoryIds = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    categoryIds.add(rs.getInt("nb_category"));
                }
            }

            return categoryIds;
        } catch (Exception e) {
            e.printStackTrace();
            return categoryIds;
        }
    }

    /**
     * Gets all products.
     * 
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM TB_PRODUCT ORDER BY DA_FIRST_DATE DESC";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setNo_product(rs.getString("NO_PRODUCT"));
                    product.setNm_product(rs.getString("NM_PRODUCT"));
                    product.setNm_detail_explain(rs.getString("NM_DETAIL_EXPLAIN"));
                    product.setDt_start_date(rs.getString("DT_START_DATE"));
                    product.setDt_end_date(rs.getString("DT_END_DATE"));
                    product.setQt_customer_price(rs.getInt("QT_CUSTOMER_PRICE"));
                    product.setQt_sale_price(rs.getInt("QT_SALE_PRICE"));
                    product.setQt_stock(rs.getInt("QT_STOCK"));
                    product.setQt_delivery_fee(rs.getInt("QT_DELIVERY_FEE"));
                    product.setNo_register(rs.getString("NO_REGISTER"));
                    product.setSale_status(rs.getInt("SALE_STATUS"));
                    product.setId_file(rs.getString("ID_FILE"));
                    products.add(product);
                }
            }

            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return products;
        }
    }

    /**
     * Removes category mappings for a product.
     * 
     * @param productId   The product ID
     * @param categoryIds Array of category IDs to remove
     * @return true if successful, false otherwise
     */
    public boolean removeCategoryMappings(String productId, String[] categoryIds) {
        String sql = "DELETE FROM TB_CATEGORY_PRODUCT_MAPPING " +
                "WHERE no_product = ? AND nb_category = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (String categoryId : categoryIds) {
                    ps.setString(1, productId);
                    ps.setInt(2, Integer.parseInt(categoryId));
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts a product using the provided connection for transaction support.
     * 
     * @param product The product to insert
     * @param conn The database connection to use
     * @return The number of rows affected
     * @throws SQLException If a database error occurs
     */
    public int insertProduct(Product product, Connection conn) throws SQLException {
        String sql = "INSERT INTO TB_PRODUCT (" +
                "NO_PRODUCT, NM_PRODUCT, NM_DETAIL_EXPLAIN, " +
                "DT_START_DATE, DT_END_DATE, QT_CUSTOMER_PRICE, QT_SALE_PRICE, " +
                "QT_STOCK, QT_DELIVERY_FEE, NO_REGISTER, DA_FIRST_DATE, SALE_STATUS, ID_FILE" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getNo_product());
            pstmt.setString(2, product.getNm_product());
            pstmt.setString(3, product.getNm_detail_explain());
            pstmt.setString(4, product.getDt_start_date());
            pstmt.setString(5, product.getDt_end_date());
            pstmt.setInt(6, product.getQt_customer_price());
            pstmt.setInt(7, product.getQt_sale_price());
            pstmt.setInt(8, product.getQt_stock());
            pstmt.setInt(9, product.getQt_delivery_fee());
            pstmt.setString(10, product.getNo_register());
            pstmt.setInt(11, product.getSale_status());
            pstmt.setString(12, product.getId_file());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Updates a product using the provided connection for transaction support.
     * 
     * @param product The product to update
     * @param conn The database connection to use
     * @return The number of rows affected
     * @throws SQLException If a database error occurs
     */
    public int updateProduct(Product product, Connection conn) throws SQLException {
        String sql = "UPDATE TB_PRODUCT SET " +
                "NM_PRODUCT = ?, QT_SALE_PRICE = ?, QT_STOCK = ?, " +
                "SALE_STATUS = ? WHERE NO_PRODUCT = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getNm_product());
            pstmt.setInt(2, product.getQt_sale_price());
            pstmt.setInt(3, product.getQt_stock());
            pstmt.setInt(4, product.getSale_status());
            pstmt.setString(5, product.getNo_product());

            return pstmt.executeUpdate();
        }
    }

    /**
     * Updates a product's image reference.
     * 
     * @param productId The product ID
     * @param fileId The new file ID
     * @param conn The database connection to use
     * @return The number of rows affected
     * @throws SQLException If a database error occurs
     */
    public int updateProductImage(String productId, String fileId, Connection conn) throws SQLException {
        String sql = "UPDATE TB_PRODUCT SET ID_FILE = ? WHERE NO_PRODUCT = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fileId);
            pstmt.setString(2, productId);

            return pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a product from TB_PRODUCT.
     * 
     * @param productId The product ID to delete
     * @param conn The database connection to use
     * @return The number of rows affected
     * @throws SQLException If a database error occurs
     */
    public int deleteProduct(String productId, Connection conn) throws SQLException {
        String sql = "DELETE FROM TB_PRODUCT WHERE NO_PRODUCT = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);

            int result = pstmt.executeUpdate();
            return result;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Removes all category mappings for a product.
     * 
     * @param productId The product ID
     * @param conn The database connection to use
     * @return The number of rows affected
     * @throws SQLException If a database error occurs
     */
    public int removeAllCategoryMappings(String productId, Connection conn) throws SQLException {
        String sql = "DELETE FROM TB_CATEGORY_PRODUCT_MAPPING WHERE no_product = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);

            int result = pstmt.executeUpdate();
            return result;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Gets the file ID associated with a product.
     * 
     * @param productId The product ID
     * @param conn The database connection to use
     * @return The file ID or null if not found
     * @throws SQLException If a database error occurs
     */
    public String getProductFileId(String productId, Connection conn) throws SQLException {
        String sql = "SELECT ID_FILE FROM TB_PRODUCT WHERE NO_PRODUCT = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String fileId = rs.getString("ID_FILE");
                    return fileId;
                }
                return null;
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean insert(Product product, String[] categoryIds) {
        String sqlProduct = "INSERT INTO TB_PRODUCT (" +
                "NO_PRODUCT, NM_PRODUCT, NM_DETAIL_EXPLAIN, " +
                "DT_START_DATE, DT_END_DATE, QT_CUSTOMER_PRICE, QT_SALE_PRICE, " +
                "QT_STOCK, QT_DELIVERY_FEE, NO_REGISTER, DA_FIRST_DATE, SALE_STATUS" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)";

        String sqlMapping = "INSERT INTO TB_CATEGORY_PRODUCT_MAPPING " +
                "(nb_category, no_product, cn_order, no_register, da_first_date) " +
                "VALUES (?, ?, ?, ?, SYSDATE)";

        try (Connection conn = DBConnection.getConnection()) {
            // 상품 삽입과 카테고리 매핑은 하나의 트랜잭션으로 관리
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlProduct)) {
                ps1.setString(1, product.getNo_product());
                ps1.setString(2, product.getNm_product());
                ps1.setString(3, product.getNm_detail_explain());
                ps1.setString(4, product.getDt_start_date());
                ps1.setString(5, product.getDt_end_date());
                ps1.setInt(6, product.getQt_customer_price());
                ps1.setInt(7, product.getQt_sale_price());
                ps1.setInt(8, product.getQt_stock());
                ps1.setInt(9, product.getQt_delivery_fee());
                ps1.setString(10, product.getNo_register());
                ps1.setInt(11, product.getSale_status());
                ps1.executeUpdate();
            }


            try (PreparedStatement ps2 = conn.prepareStatement(sqlMapping)) {
                for (int i = 0; i < categoryIds.length; i++) {
                    ps2.setInt(1, Integer.parseInt(categoryIds[i]));
                    ps2.setString(2, product.getNo_product());
                    ps2.setInt(3, i + 1);
                    ps2.setString(4, product.getNo_register());
                    ps2.addBatch();
                }
                ps2.executeBatch();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
