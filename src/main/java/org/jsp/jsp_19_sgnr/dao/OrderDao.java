package org.jsp.jsp_19_sgnr.dao;

import org.jsp.jsp_19_sgnr.db.DBConnection;
import org.jsp.jsp_19_sgnr.dto.Order;
import org.jsp.jsp_19_sgnr.dto.OrderItem;
import org.jsp.jsp_19_sgnr.dto.OrderWithUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DAO class for handling order and order item operations.
 */
public class OrderDao {

    /**
     * Inserts a new order into the database.
     * 
     * @param order The order to insert
     * @return The generated order ID if successful, null otherwise
     */
    public String insertOrder(Order order) {
        String sql = "INSERT INTO TB_ORDER (id_order, no_user, qt_order_amount, qt_deli_money, qt_deli_period, " +
                "nm_order_person, nm_receiver, no_delivery_zipno, nm_delivery_address, nm_receiver_telno, " +
                "nm_delivery_space, cd_order_type, da_order, st_order, st_payment, no_register, da_first_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, SYSDATE)";

        // Generate a unique order ID if not provided
        if (order.getId_order() == null || order.getId_order().isEmpty()) {
            order.setId_order(generateOrderId());
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, order.getId_order());
            ps.setString(2, order.getNo_user());
            ps.setInt(3, order.getQt_order_amount());
            ps.setInt(4, order.getQt_deli_money());
            ps.setInt(5, order.getQt_deli_period());
            ps.setString(6, order.getNm_order_person());
            ps.setString(7, order.getNm_receiver());
            ps.setString(8, order.getNo_delivery_zipno());
            ps.setString(9, order.getNm_delivery_address());
            ps.setString(10, order.getNm_receiver_telno());
            ps.setString(11, order.getNm_delivery_space());
            ps.setString(12, order.getCd_order_type());
            ps.setString(13, order.getSt_order());
            ps.setString(14, order.getSt_payment());
            ps.setString(15, order.getNo_register());

            int result = ps.executeUpdate();
            if (result > 0) {
                return order.getId_order();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Inserts a new order item into the database.
     * 
     * @param item The order item to insert
     * @return true if successful, false otherwise
     */
    public boolean insertOrderItem(OrderItem item) {
        String sql = "INSERT INTO TB_ORDER_ITEM (id_order_item, id_order, cn_order_item, no_product, no_user, " +
                "qt_unit_price, qt_order_item, qt_order_item_amount, qt_order_item_delivery_fee, st_payment, " +
                "no_register, da_first_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";

        // Generate a unique order item ID if not provided
        if (item.getId_order_item() == null || item.getId_order_item().isEmpty()) {
            item.setId_order_item(generateOrderItemId());
        }

        // Calculate the order item amount if not provided
        if (item.getQt_order_item_amount() == null) {
            item.setQt_order_item_amount(item.getQt_unit_price() * item.getQt_order_item());
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getId_order_item());
            ps.setString(2, item.getId_order());
            ps.setInt(3, item.getCn_order_item());
            ps.setString(4, item.getNo_product());
            ps.setString(5, item.getNo_user());
            ps.setInt(6, item.getQt_unit_price());
            ps.setInt(7, item.getQt_order_item());
            ps.setInt(8, item.getQt_order_item_amount());
            ps.setInt(9, item.getQt_order_item_delivery_fee());
            ps.setString(10, item.getSt_payment());
            ps.setString(11, item.getNo_register());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Creates an order with its items in a single transaction.
     * 
     * @param order The order to create
     * @param items The order items to create
     * @return The generated order ID if successful, null otherwise
     */
    public String createOrder(Order order, List<OrderItem> items) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // Generate a unique order ID if not provided
            if (order.getId_order() == null || order.getId_order().isEmpty()) {
                order.setId_order(generateOrderId());
            }

            // Insert the order
            String orderSql = "INSERT INTO TB_ORDER (id_order, no_user, qt_order_amount, qt_deli_money, qt_deli_period, " +
                    "nm_order_person, nm_receiver, no_delivery_zipno, nm_delivery_address, nm_receiver_telno, " +
                    "nm_delivery_space, cd_order_type, da_order, st_order, st_payment, no_register, da_first_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, SYSDATE)";

            try (PreparedStatement ps = conn.prepareStatement(orderSql)) {
                ps.setString(1, order.getId_order());
                ps.setString(2, order.getNo_user());
                ps.setInt(3, order.getQt_order_amount());
                ps.setInt(4, order.getQt_deli_money());
                ps.setInt(5, order.getQt_deli_period());
                ps.setString(6, order.getNm_order_person());
                ps.setString(7, order.getNm_receiver());
                ps.setString(8, order.getNo_delivery_zipno());
                ps.setString(9, order.getNm_delivery_address());
                ps.setString(10, order.getNm_receiver_telno());
                ps.setString(11, order.getNm_delivery_space());
                ps.setString(12, order.getCd_order_type());
                ps.setString(13, order.getSt_order());
                ps.setString(14, order.getSt_payment());
                ps.setString(15, order.getNo_register());

                int result = ps.executeUpdate();
                if (result <= 0) {
                    conn.rollback();
                    return null;
                }
            }

            // Insert the order items
            String itemSql = "INSERT INTO TB_ORDER_ITEM (id_order_item, id_order, cn_order_item, no_product, no_user, " +
                    "qt_unit_price, qt_order_item, qt_order_item_amount, qt_order_item_delivery_fee, st_payment, " +
                    "no_register, da_first_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";

            try (PreparedStatement ps = conn.prepareStatement(itemSql)) {
                for (int i = 0; i < items.size(); i++) {
                    OrderItem item = items.get(i);

                    // Set the order ID for the item
                    item.setId_order(order.getId_order());

                    // Generate a unique order item ID if not provided
                    if (item.getId_order_item() == null || item.getId_order_item().isEmpty()) {
                        item.setId_order_item(generateOrderItemId());
                    }

                    // Set the order item number if not provided
                    if (item.getCn_order_item() <= 0) {
                        item.setCn_order_item(i + 1);
                    }

                    // Calculate the order item amount if not provided
                    if (item.getQt_order_item_amount() == null) {
                        item.setQt_order_item_amount(item.getQt_unit_price() * item.getQt_order_item());
                    }

                    ps.setString(1, item.getId_order_item());
                    ps.setString(2, item.getId_order());
                    ps.setInt(3, item.getCn_order_item());
                    ps.setString(4, item.getNo_product());
                    ps.setString(5, item.getNo_user());
                    ps.setInt(6, item.getQt_unit_price());
                    ps.setInt(7, item.getQt_order_item());
                    ps.setInt(8, item.getQt_order_item_amount());
                    ps.setInt(9, item.getQt_order_item_delivery_fee());
                    ps.setString(10, item.getSt_payment());
                    ps.setString(11, item.getNo_register());

                    ps.addBatch();
                }

                int[] results = ps.executeBatch();
                for (int result : results) {
                    if (result <= 0) {
                        conn.rollback();
                        return null;
                    }
                }
            }

            conn.commit();  // Commit transaction
            return order.getId_order();
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Reset auto-commit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * Gets all orders for a user.
     * 
     * @param userId The user ID
     * @return A list of orders for the user
     */
    public List<Order> getOrdersByUser(String userId) {
        String sql = "SELECT * FROM TB_ORDER WHERE no_user = ? ORDER BY da_order DESC";
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId_order(rs.getString("id_order"));
                    order.setNo_user(rs.getString("no_user"));
                    order.setQt_order_amount(rs.getInt("qt_order_amount"));
                    order.setQt_deli_money(rs.getInt("qt_deli_money"));
                    order.setQt_deli_period(rs.getInt("qt_deli_period"));
                    order.setNm_order_person(rs.getString("nm_order_person"));
                    order.setNm_receiver(rs.getString("nm_receiver"));
                    order.setNo_delivery_zipno(rs.getString("no_delivery_zipno"));
                    order.setNm_delivery_address(rs.getString("nm_delivery_address"));
                    order.setNm_receiver_telno(rs.getString("nm_receiver_telno"));
                    order.setNm_delivery_space(rs.getString("nm_delivery_space"));
                    order.setCd_order_type(rs.getString("cd_order_type"));
                    order.setDa_order(rs.getString("da_order"));
                    order.setSt_order(rs.getString("st_order"));
                    order.setSt_payment(rs.getString("st_payment"));
                    order.setNo_register(rs.getString("no_register"));
                    order.setDa_first_date(rs.getString("da_first_date"));

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    /**
     * Gets all order items for an order.
     * 
     * @param orderId The order ID
     * @return A list of order items for the order
     */
    public List<OrderItem> getOrderItems(String orderId) {
        String sql = "SELECT * FROM TB_ORDER_ITEM WHERE id_order = ? ORDER BY cn_order_item";
        List<OrderItem> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId_order_item(rs.getString("id_order_item"));
                    item.setId_order(rs.getString("id_order"));
                    item.setCn_order_item(rs.getInt("cn_order_item"));
                    item.setNo_product(rs.getString("no_product"));
                    item.setNo_user(rs.getString("no_user"));
                    item.setQt_unit_price(rs.getInt("qt_unit_price"));
                    item.setQt_order_item(rs.getInt("qt_order_item"));
                    item.setQt_order_item_amount(rs.getInt("qt_order_item_amount"));
                    item.setQt_order_item_delivery_fee(rs.getInt("qt_order_item_delivery_fee"));
                    item.setSt_payment(rs.getString("st_payment"));
                    item.setNo_register(rs.getString("no_register"));
                    item.setDa_first_date(rs.getString("da_first_date"));

                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Generates a unique order ID.
     * 
     * @return A unique order ID
     */
    private String generateOrderId() {
        return "ORD" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 27);
    }

    /**
     * Generates a unique order item ID.
     * 
     * @return A unique order item ID
     */
    private String generateOrderItemId() {
        return "ORI" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 27);
    }

    /**
     * Updates the status of an order.
     * 
     * @param orderId The order ID
     * @param status The new status
     * @return true if successful, false otherwise
     */
    public boolean updateOrderStatus(String orderId, String status) {
        String sql = "UPDATE TB_ORDER SET st_order = ? WHERE id_order = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, orderId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Gets an order by its ID.
     * 
     * @param orderId The order ID
     * @return The order, or null if not found
     */
    public Order getOrderById(String orderId) {
        String sql = "SELECT * FROM TB_ORDER WHERE id_order = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setId_order(rs.getString("id_order"));
                    order.setNo_user(rs.getString("no_user"));
                    order.setQt_order_amount(rs.getInt("qt_order_amount"));
                    order.setQt_deli_money(rs.getInt("qt_deli_money"));
                    order.setQt_deli_period(rs.getInt("qt_deli_period"));
                    order.setNm_order_person(rs.getString("nm_order_person"));
                    order.setNm_receiver(rs.getString("nm_receiver"));
                    order.setNo_delivery_zipno(rs.getString("no_delivery_zipno"));
                    order.setNm_delivery_address(rs.getString("nm_delivery_address"));
                    order.setNm_receiver_telno(rs.getString("nm_receiver_telno"));
                    order.setNm_delivery_space(rs.getString("nm_delivery_space"));
                    order.setCd_order_type(rs.getString("cd_order_type"));
                    order.setDa_order(rs.getString("da_order"));
                    order.setSt_order(rs.getString("st_order"));
                    order.setSt_payment(rs.getString("st_payment"));
                    order.setNo_register(rs.getString("no_register"));
                    order.setDa_first_date(rs.getString("da_first_date"));

                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets all orders with user information, with optional filtering by order ID or user name.
     * 
     * @param orderId The order ID to filter by (optional)
     * @param userName The user name to filter by (optional)
     * @return A list of orders with user information
     */
    public List<OrderWithUser> getAllOrders(String orderId, String userName) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT o.*, m.NM_USER as nm_user FROM TB_ORDER o ");
        sqlBuilder.append("JOIN TB_USER m ON o.no_user = m.ID_USER ");

        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (orderId != null && !orderId.trim().isEmpty()) {
            conditions.add("o.id_order LIKE ?");
            parameters.add("%" + orderId + "%");
        }

        if (userName != null && !userName.trim().isEmpty()) {
            conditions.add("m.NM_USER LIKE ?");
            parameters.add("%" + userName + "%");
        }

        if (!conditions.isEmpty()) {
            sqlBuilder.append("WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    sqlBuilder.append(" AND ");
                }
                sqlBuilder.append(conditions.get(i));
            }
        }

        sqlBuilder.append(" ORDER BY o.da_order DESC");

        List<OrderWithUser> orders = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderWithUser order = new OrderWithUser();
                    order.setId_order(rs.getString("id_order"));
                    order.setNo_user(rs.getString("no_user"));
                    order.setQt_order_amount(rs.getInt("qt_order_amount"));
                    order.setQt_deli_money(rs.getInt("qt_deli_money"));
                    order.setQt_deli_period(rs.getInt("qt_deli_period"));
                    order.setNm_order_person(rs.getString("nm_order_person"));
                    order.setNm_receiver(rs.getString("nm_receiver"));
                    order.setNo_delivery_zipno(rs.getString("no_delivery_zipno"));
                    order.setNm_delivery_address(rs.getString("nm_delivery_address"));
                    order.setNm_receiver_telno(rs.getString("nm_receiver_telno"));
                    order.setNm_delivery_space(rs.getString("nm_delivery_space"));
                    order.setCd_order_type(rs.getString("cd_order_type"));
                    order.setDa_order(rs.getString("da_order"));
                    order.setSt_order(rs.getString("st_order"));
                    order.setSt_payment(rs.getString("st_payment"));
                    order.setNo_register(rs.getString("no_register"));
                    order.setDa_first_date(rs.getString("da_first_date"));
                    order.setNm_user(rs.getString("nm_user"));

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}
