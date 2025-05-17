package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Basket;
import org.jsp.jsp_19_sgnr.dto.BasketItem;
import org.jsp.jsp_19_sgnr.dto.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for handling basket and basket item operations.
 */
public class BasketDao {

    /**
     * Creates the necessary tables for basket functionality if they don't exist.
     * This method should be called during application initialization.
     */
    public void createTablesIfNotExist() {
        String createBasketTable = "CREATE TABLE IF NOT EXISTS TB_BASKET (" +
                "BASKET_ID NUMBER PRIMARY KEY, " +
                "USER_EMAIL VARCHAR2(100) NOT NULL, " +
                "CREATE_DATE DATE DEFAULT SYSDATE, " +
                "UPDATE_DATE DATE DEFAULT SYSDATE, " +
                "STATUS VARCHAR2(10) DEFAULT 'ACTIVE', " +
                "CONSTRAINT FK_BASKET_USER FOREIGN KEY (USER_EMAIL) REFERENCES TB_MEMBER(EMAIL)" +
                ")";

        String createBasketItemTable = "CREATE TABLE IF NOT EXISTS TB_BASKET_ITEM (" +
                "ITEM_ID NUMBER PRIMARY KEY, " +
                "BASKET_ID NUMBER NOT NULL, " +
                "PRODUCT_ID VARCHAR2(20) NOT NULL, " +
                "QUANTITY NUMBER DEFAULT 1, " +
                "PRICE NUMBER NOT NULL, " +
                "CREATE_DATE DATE DEFAULT SYSDATE, " +
                "UPDATE_DATE DATE DEFAULT SYSDATE, " +
                "SELECTED NUMBER(1) DEFAULT 1, " +
                "CONSTRAINT FK_BASKET_ITEM_BASKET FOREIGN KEY (BASKET_ID) REFERENCES TB_BASKET(BASKET_ID), " +
                "CONSTRAINT FK_BASKET_ITEM_PRODUCT FOREIGN KEY (PRODUCT_ID) REFERENCES TB_PRODUCT(NO_PRODUCT)" +
                ")";

        String createBasketIdSeq = "CREATE SEQUENCE IF NOT EXISTS SEQ_BASKET_ID " +
                "START WITH 1 INCREMENT BY 1 NOCACHE";

        String createBasketItemIdSeq = "CREATE SEQUENCE IF NOT EXISTS SEQ_BASKET_ITEM_ID " +
                "START WITH 1 INCREMENT BY 1 NOCACHE";

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(createBasketTable)) {
                ps.executeUpdate();
            }
            
            try (PreparedStatement ps = conn.prepareStatement(createBasketItemTable)) {
                ps.executeUpdate();
            }
            
            try (PreparedStatement ps = conn.prepareStatement(createBasketIdSeq)) {
                ps.executeUpdate();
            }
            
            try (PreparedStatement ps = conn.prepareStatement(createBasketItemIdSeq)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets or creates a basket for a user.
     * 
     * @param userEmail The user's email
     * @return The user's active basket
     */
    public Basket getOrCreateBasket(String userEmail) {
        String selectSql = "SELECT * FROM TB_BASKET WHERE USER_EMAIL = ? AND STATUS = 'ACTIVE'";
        String insertSql = "INSERT INTO TB_BASKET (BASKET_ID, USER_EMAIL, CREATE_DATE, UPDATE_DATE, STATUS) " +
                "VALUES (SEQ_BASKET_ID.NEXTVAL, ?, SYSDATE, SYSDATE, 'ACTIVE')";
        
        try (Connection conn = DBConnection.getConnection()) {
            // Try to find an existing active basket
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, userEmail);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Basket basket = new Basket();
                        basket.setBasketId(rs.getInt("BASKET_ID"));
                        basket.setUserEmail(rs.getString("USER_EMAIL"));
                        basket.setCreateDate(rs.getString("CREATE_DATE"));
                        basket.setUpdateDate(rs.getString("UPDATE_DATE"));
                        basket.setStatus(rs.getString("STATUS"));
                        return basket;
                    }
                }
            }
            
            // No active basket found, create a new one
            try (PreparedStatement ps = conn.prepareStatement(insertSql, new String[]{"BASKET_ID"})) {
                ps.setString(1, userEmail);
                ps.executeUpdate();
                
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int basketId = rs.getInt(1);
                        Basket basket = new Basket();
                        basket.setBasketId(basketId);
                        basket.setUserEmail(userEmail);
                        basket.setCreateDate(null); // Set by DB default
                        basket.setUpdateDate(null); // Set by DB default
                        basket.setStatus("ACTIVE");
                        return basket;
                    }
                }
            }
            
            // If we couldn't get the generated key, try to fetch the basket again
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, userEmail);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Basket basket = new Basket();
                        basket.setBasketId(rs.getInt("BASKET_ID"));
                        basket.setUserEmail(rs.getString("USER_EMAIL"));
                        basket.setCreateDate(rs.getString("CREATE_DATE"));
                        basket.setUpdateDate(rs.getString("UPDATE_DATE"));
                        basket.setStatus(rs.getString("STATUS"));
                        return basket;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Adds an item to a basket or updates its quantity if it already exists.
     * 
     * @param basketId The basket ID
     * @param productId The product ID
     * @param quantity The quantity to add
     * @param price The price of the product
     * @return true if successful, false otherwise
     */
    public boolean addOrUpdateBasketItem(int basketId, String productId, int quantity, int price) {
        String selectSql = "SELECT * FROM TB_BASKET_ITEM WHERE BASKET_ID = ? AND PRODUCT_ID = ?";
        String updateSql = "UPDATE TB_BASKET_ITEM SET QUANTITY = QUANTITY + ?, UPDATE_DATE = SYSDATE WHERE BASKET_ID = ? AND PRODUCT_ID = ?";
        String insertSql = "INSERT INTO TB_BASKET_ITEM (ITEM_ID, BASKET_ID, PRODUCT_ID, QUANTITY, PRICE, CREATE_DATE, UPDATE_DATE, SELECTED) " +
                "VALUES (SEQ_BASKET_ITEM_ID.NEXTVAL, ?, ?, ?, ?, SYSDATE, SYSDATE, 1)";
        
        try (Connection conn = DBConnection.getConnection()) {
            // Check if the item already exists in the basket
            boolean itemExists = false;
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, basketId);
                ps.setString(2, productId);
                
                try (ResultSet rs = ps.executeQuery()) {
                    itemExists = rs.next();
                }
            }
            
            if (itemExists) {
                // Update existing item
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, quantity);
                    ps.setInt(2, basketId);
                    ps.setString(3, productId);
                    return ps.executeUpdate() > 0;
                }
            } else {
                // Insert new item
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setInt(1, basketId);
                    ps.setString(2, productId);
                    ps.setInt(3, quantity);
                    ps.setInt(4, price);
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Gets all items in a basket with product details.
     * 
     * @param basketId The basket ID
     * @return List of basket items with product details
     */
    public List<BasketItem> getBasketItems(int basketId) {
        String sql = "SELECT bi.*, p.NM_PRODUCT, p.ID_FILE " +
                "FROM TB_BASKET_ITEM bi " +
                "JOIN TB_PRODUCT p ON bi.PRODUCT_ID = p.NO_PRODUCT " +
                "WHERE bi.BASKET_ID = ? " +
                "ORDER BY bi.CREATE_DATE DESC";
        
        List<BasketItem> items = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, basketId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BasketItem item = new BasketItem();
                    item.setItemId(rs.getInt("ITEM_ID"));
                    item.setBasketId(rs.getInt("BASKET_ID"));
                    item.setProductId(rs.getString("PRODUCT_ID"));
                    item.setQuantity(rs.getInt("QUANTITY"));
                    item.setPrice(rs.getInt("PRICE"));
                    item.setCreateDate(rs.getString("CREATE_DATE"));
                    item.setUpdateDate(rs.getString("UPDATE_DATE"));
                    item.setSelected(rs.getInt("SELECTED") == 1);
                    
                    // Product details
                    item.setProductName(rs.getString("NM_PRODUCT"));
                    item.setProductImage(rs.getString("ID_FILE"));
                    
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }

    /**
     * Updates the quantity of a basket item.
     * 
     * @param itemId The item ID
     * @param quantity The new quantity
     * @return true if successful, false otherwise
     */
    public boolean updateBasketItemQuantity(int itemId, int quantity) {
        String sql = "UPDATE TB_BASKET_ITEM SET QUANTITY = ?, UPDATE_DATE = SYSDATE WHERE ITEM_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantity);
            ps.setInt(2, itemId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Updates the selected status of a basket item.
     * 
     * @param itemId The item ID
     * @param selected Whether the item is selected
     * @return true if successful, false otherwise
     */
    public boolean updateBasketItemSelected(int itemId, boolean selected) {
        String sql = "UPDATE TB_BASKET_ITEM SET SELECTED = ?, UPDATE_DATE = SYSDATE WHERE ITEM_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, selected ? 1 : 0);
            ps.setInt(2, itemId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Removes an item from a basket.
     * 
     * @param itemId The item ID
     * @return true if successful, false otherwise
     */
    public boolean removeBasketItem(int itemId) {
        String sql = "DELETE FROM TB_BASKET_ITEM WHERE ITEM_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, itemId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Removes all items from a basket.
     * 
     * @param basketId The basket ID
     * @return true if successful, false otherwise
     */
    public boolean clearBasket(int basketId) {
        String sql = "DELETE FROM TB_BASKET_ITEM WHERE BASKET_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, basketId);
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Gets the selected items from a basket.
     * 
     * @param basketId The basket ID
     * @return List of selected basket items
     */
    public List<BasketItem> getSelectedBasketItems(int basketId) {
        String sql = "SELECT bi.*, p.NM_PRODUCT, p.ID_FILE " +
                "FROM TB_BASKET_ITEM bi " +
                "JOIN TB_PRODUCT p ON bi.PRODUCT_ID = p.NO_PRODUCT " +
                "WHERE bi.BASKET_ID = ? AND bi.SELECTED = 1 " +
                "ORDER BY bi.CREATE_DATE DESC";
        
        List<BasketItem> items = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, basketId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BasketItem item = new BasketItem();
                    item.setItemId(rs.getInt("ITEM_ID"));
                    item.setBasketId(rs.getInt("BASKET_ID"));
                    item.setProductId(rs.getString("PRODUCT_ID"));
                    item.setQuantity(rs.getInt("QUANTITY"));
                    item.setPrice(rs.getInt("PRICE"));
                    item.setCreateDate(rs.getString("CREATE_DATE"));
                    item.setUpdateDate(rs.getString("UPDATE_DATE"));
                    item.setSelected(true);
                    
                    // Product details
                    item.setProductName(rs.getString("NM_PRODUCT"));
                    item.setProductImage(rs.getString("ID_FILE"));
                    
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }
}