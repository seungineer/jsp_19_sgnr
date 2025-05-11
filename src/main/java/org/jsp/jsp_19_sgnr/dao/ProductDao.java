package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.jsp.jsp_19_sgnr.db.DBConnection.getConnection;

public class ProductDao {
    public int insertProduct(Product product) {
        String sql = "INSERT INTO TB_PRODUCT (" +
                "NO_PRODUCT, NM_PRODUCT, NM_DETAIL_EXPLAIN, " +
                "QT_CUSTOMER_PRICE, QT_SALE_PRICE, QT_STOCK, " +
                "QT_DELIVERY_FEE, NO_REGISTER, DA_FIRST_DATE" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getNo_product());
            pstmt.setString(2, product.getNm_product());
            pstmt.setString(3, product.getNm_detail_explain());
            pstmt.setInt(4, product.getQt_customer_price());
            pstmt.setInt(5, product.getQt_sale_price());
            pstmt.setInt(6, product.getQt_stock());
            pstmt.setInt(7, product.getQt_delivery_fee());
            pstmt.setString(8, product.getNo_register());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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
