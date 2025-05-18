package org.jsp.jsp_19_sgnr.dto;

/**
 * DTO class for Order with User information.
 * Extends the Order class to include user name.
 */
public class OrderWithUser extends Order {
    private String nm_user; // 사용자명

    public OrderWithUser() {
        super();
    }

    public OrderWithUser(Order order) {
        super(order.getId_order(), order.getNo_user(), order.getQt_order_amount(),
              order.getQt_deli_money(), order.getQt_deli_period(), order.getNm_order_person(),
              order.getNm_receiver(), order.getNo_delivery_zipno(), order.getNm_delivery_address(),
              order.getNm_receiver_telno(), order.getNm_delivery_space(), order.getCd_order_type(),
              order.getDa_order(), order.getSt_order(), order.getSt_payment(),
              order.getNo_register(), order.getDa_first_date());
    }

    public String getNm_user() {
        return nm_user;
    }

    public void setNm_user(String nm_user) {
        this.nm_user = nm_user;
    }
}