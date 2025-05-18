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
                "nb_basket NUMBER PRIMARY KEY, " +
                "no_user VARCHAR2(100) NOT NULL, " +
                "qt_basket_amount NUMBER, " +
                "no_register VARCHAR2(100), " +
                "da_first_date DATE DEFAULT SYSDATE, " +
                "CONSTRAINT FK_BASKET_USER FOREIGN KEY (no_user) REFERENCES TB_MEMBER(EMAIL)" +
                ")";

        String createBasketItemTable = "CREATE TABLE IF NOT EXISTS TB_BASKET_ITEM (" +
                "nb_basket_item NUMBER PRIMARY KEY, " +
                "nb_basket NUMBER NOT NULL, " +
                "cn_basket_item_order NUMBER, " +
                "no_product VARCHAR2(20) NOT NULL, " +
                "no_user VARCHAR2(100) NOT NULL, " +
                "qt_basket_item_price NUMBER NOT NULL, " +
                "qt_basket_item NUMBER DEFAULT 1, " +
                "qt_basket_item_amount NUMBER, " +
                "no_register VARCHAR2(100), " +
                "da_first_date DATE DEFAULT SYSDATE, " +
                "CONSTRAINT FK_BASKET_ITEM_BASKET FOREIGN KEY (nb_basket) REFERENCES TB_BASKET(nb_basket), " +
                "CONSTRAINT FK_BASKET_ITEM_PRODUCT FOREIGN KEY (no_product) REFERENCES TB_PRODUCT(NO_PRODUCT), " +
                "CONSTRAINT FK_BASKET_ITEM_USER FOREIGN KEY (no_user) REFERENCES TB_MEMBER(EMAIL)" +
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
        String selectSql = "SELECT * FROM TB_BASKET WHERE no_user = ?";
        String insertSql = "INSERT INTO TB_BASKET (nb_basket, no_user, qt_basket_amount, no_register, da_first_date) " +
                "VALUES (SEQ_BASKET_ID.NEXTVAL, ?, 0, ?, SYSDATE)";

        try (Connection conn = DBConnection.getConnection()) {
            // Try to find an existing basket
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, userEmail);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Basket basket = new Basket();
                        basket.setBasketId(rs.getInt("nb_basket"));
                        basket.setUserEmail(rs.getString("no_user"));
                        basket.setCreateDate(rs.getString("da_first_date"));
                        // These fields are no longer in the table, but we'll keep them in the DTO for now
                        basket.setUpdateDate(null);
                        basket.setStatus("ACTIVE");
                        return basket;
                    }
                }
            }

            // No basket found, create a new one
            try (PreparedStatement ps = conn.prepareStatement(insertSql, new String[]{"nb_basket"})) {
                ps.setString(1, userEmail);
                ps.setString(2, userEmail); // Using userEmail as register ID for simplicity
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int basketId = rs.getInt(1);
                        Basket basket = new Basket();
                        basket.setBasketId(basketId);
                        basket.setUserEmail(userEmail);
                        basket.setCreateDate(null); // Set by DB default
                        // These fields are no longer in the table, but we'll keep them in the DTO for now
                        basket.setUpdateDate(null);
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
                        basket.setBasketId(rs.getInt("nb_basket"));
                        basket.setUserEmail(rs.getString("no_user"));
                        basket.setCreateDate(rs.getString("da_first_date"));
                        // These fields are no longer in the table, but we'll keep them in the DTO for now
                        basket.setUpdateDate(null);
                        basket.setStatus("ACTIVE");
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
        // First, get the user email for the basket
        String getUserSql = "SELECT no_user FROM TB_BASKET WHERE nb_basket = ?";
        String selectSql = "SELECT * FROM TB_BASKET_ITEM WHERE nb_basket = ? AND no_product = ?";
        String updateSql = "UPDATE TB_BASKET_ITEM SET qt_basket_item = qt_basket_item + ?, qt_basket_item_amount = qt_basket_item_price * (qt_basket_item + ?) WHERE nb_basket = ? AND no_product = ?";
        String insertSql = "INSERT INTO TB_BASKET_ITEM (nb_basket_item, nb_basket, cn_basket_item_order, no_product, no_user, qt_basket_item_price, qt_basket_item, qt_basket_item_amount, no_register, da_first_date) " +
                "VALUES (SEQ_BASKET_ITEM_ID.NEXTVAL, ?, 1, ?, ?, ?, ?, ?, ?, SYSDATE)";

        try (Connection conn = DBConnection.getConnection()) {
            // Get the user email for the basket
            String userEmail = null;
            try (PreparedStatement ps = conn.prepareStatement(getUserSql)) {
                ps.setInt(1, basketId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userEmail = rs.getString("no_user");
                    } else {
                        return false; // Basket not found
                    }
                }
            }

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
                    ps.setInt(2, quantity); // For calculating the new amount
                    ps.setInt(3, basketId);
                    ps.setString(4, productId);
                    return ps.executeUpdate() > 0;
                }
            } else {
                // Insert new item
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setInt(1, basketId);
                    ps.setString(2, productId);
                    ps.setString(3, userEmail);
                    ps.setInt(4, price);
                    ps.setInt(5, quantity);
                    ps.setInt(6, price * quantity); // Calculate the amount
                    ps.setString(7, userEmail); // Using userEmail as register ID for simplicity
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
                "JOIN TB_PRODUCT p ON bi.no_product = p.NO_PRODUCT " +
                "WHERE bi.nb_basket = ? " +
                "ORDER BY bi.da_first_date DESC";

        List<BasketItem> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, basketId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BasketItem item = new BasketItem();
                    item.setItemId(rs.getInt("nb_basket_item"));
                    item.setBasketId(rs.getInt("nb_basket"));
                    item.setProductId(rs.getString("no_product"));
                    item.setQuantity(rs.getInt("qt_basket_item"));
                    item.setPrice(rs.getInt("qt_basket_item_price"));
                    item.setCreateDate(rs.getString("da_first_date"));
                    // These fields are no longer in the table, but we'll keep them in the DTO for now
                    item.setUpdateDate(null);
                    item.setSelected(true); // Default to selected since we no longer have this field

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
        // First, get the price to calculate the new amount
        String getPriceSql = "SELECT qt_basket_item_price FROM TB_BASKET_ITEM WHERE nb_basket_item = ?";
        String updateSql = "UPDATE TB_BASKET_ITEM SET qt_basket_item = ?, qt_basket_item_amount = ? WHERE nb_basket_item = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // Get the price
            int price = 0;
            try (PreparedStatement ps = conn.prepareStatement(getPriceSql)) {
                ps.setInt(1, itemId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        price = rs.getInt("qt_basket_item_price");
                    } else {
                        return false; // Item not found
                    }
                }
            }

            // Update the quantity and amount
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, quantity);
                ps.setInt(2, price * quantity); // Calculate the new amount
                ps.setInt(3, itemId);

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates the selected status of a basket item.
     * Note: This method is kept for backward compatibility, but the SELECTED column has been removed.
     * 
     * @param itemId The item ID
     * @param selected Whether the item is selected
     * @return true always (for backward compatibility)
     */
    public boolean updateBasketItemSelected(int itemId, boolean selected) {
        // The SELECTED column has been removed from the table
        // This method is kept for backward compatibility
        // Always return true as if the operation was successful
        return true;
    }

    /**
     * Removes an item from a basket.
     * 
     * @param itemId The item ID
     * @return true if successful, false otherwise
     */
    public boolean removeBasketItem(int itemId) {
        String sql = "DELETE FROM TB_BASKET_ITEM WHERE nb_basket_item = ?";

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
        String sql = "DELETE FROM TB_BASKET_ITEM WHERE nb_basket = ?";

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
     * Note: This method now returns all items since the SELECTED column has been removed.
     * 
     * @param basketId The basket ID
     * @return List of all basket items
     */
    public List<BasketItem> getSelectedBasketItems(int basketId) {
        // Since the SELECTED column has been removed, we'll return all items
        // This is the same as getBasketItems
        String sql = "SELECT bi.*, p.NM_PRODUCT, p.ID_FILE " +
                "FROM TB_BASKET_ITEM bi " +
                "JOIN TB_PRODUCT p ON bi.no_product = p.NO_PRODUCT " +
                "WHERE bi.nb_basket = ? " +
                "ORDER BY bi.da_first_date DESC";

        List<BasketItem> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, basketId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BasketItem item = new BasketItem();
                    item.setItemId(rs.getInt("nb_basket_item"));
                    item.setBasketId(rs.getInt("nb_basket"));
                    item.setProductId(rs.getString("no_product"));
                    item.setQuantity(rs.getInt("qt_basket_item"));
                    item.setPrice(rs.getInt("qt_basket_item_price"));
                    item.setCreateDate(rs.getString("da_first_date"));
                    // These fields are no longer in the table, but we'll keep them in the DTO for now
                    item.setUpdateDate(null);
                    item.setSelected(true); // All items are considered selected

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
     * Gets a basket item by its ID.
     * 
     * @param itemId The item ID
     * @return The basket item, or null if not found
     */
    public BasketItem getBasketItemById(int itemId) {
        String sql = "SELECT bi.*, p.NM_PRODUCT, p.ID_FILE " +
                "FROM TB_BASKET_ITEM bi " +
                "JOIN TB_PRODUCT p ON bi.no_product = p.NO_PRODUCT " +
                "WHERE bi.nb_basket_item = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BasketItem item = new BasketItem();
                    item.setItemId(rs.getInt("nb_basket_item"));
                    item.setBasketId(rs.getInt("nb_basket"));
                    item.setProductId(rs.getString("no_product"));
                    item.setQuantity(rs.getInt("qt_basket_item"));
                    item.setPrice(rs.getInt("qt_basket_item_price"));
                    item.setCreateDate(rs.getString("da_first_date"));
                    // These fields are no longer in the table, but we'll keep them in the DTO for now
                    item.setUpdateDate(null);
                    item.setSelected(true); // All items are considered selected

                    // Product details
                    item.setProductName(rs.getString("NM_PRODUCT"));
                    item.setProductImage(rs.getString("ID_FILE"));

                    return item;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
