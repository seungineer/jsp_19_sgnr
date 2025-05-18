package org.jsp.jsp_19_sgnr.dto;

public class OrderItem {
    private String id_order_item;
    private String id_order;
    private int cn_order_item;
    private String no_product;
    private String no_user;
    private int qt_unit_price;
    private int qt_order_item;
    private Integer qt_order_item_amount;
    private int qt_order_item_delivery_fee;
    private String st_payment;
    private String no_register;
    private String da_first_date;

    public OrderItem() {
    }

    public OrderItem(String id_order_item, String id_order, int cn_order_item, String no_product, String no_user,
                    int qt_unit_price, int qt_order_item, Integer qt_order_item_amount, int qt_order_item_delivery_fee,
                    String st_payment, String no_register, String da_first_date) {
        this.id_order_item = id_order_item;
        this.id_order = id_order;
        this.cn_order_item = cn_order_item;
        this.no_product = no_product;
        this.no_user = no_user;
        this.qt_unit_price = qt_unit_price;
        this.qt_order_item = qt_order_item;
        this.qt_order_item_amount = qt_order_item_amount;
        this.qt_order_item_delivery_fee = qt_order_item_delivery_fee;
        this.st_payment = st_payment;
        this.no_register = no_register;
        this.da_first_date = da_first_date;
    }

    public String getId_order_item() {
        return id_order_item;
    }

    public void setId_order_item(String id_order_item) {
        this.id_order_item = id_order_item;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public int getCn_order_item() {
        return cn_order_item;
    }

    public void setCn_order_item(int cn_order_item) {
        this.cn_order_item = cn_order_item;
    }

    public String getNo_product() {
        return no_product;
    }

    public void setNo_product(String no_product) {
        this.no_product = no_product;
    }

    public String getNo_user() {
        return no_user;
    }

    public void setNo_user(String no_user) {
        this.no_user = no_user;
    }

    public int getQt_unit_price() {
        return qt_unit_price;
    }

    public void setQt_unit_price(int qt_unit_price) {
        this.qt_unit_price = qt_unit_price;
    }

    public int getQt_order_item() {
        return qt_order_item;
    }

    public void setQt_order_item(int qt_order_item) {
        this.qt_order_item = qt_order_item;
    }

    public Integer getQt_order_item_amount() {
        return qt_order_item_amount;
    }

    public void setQt_order_item_amount(Integer qt_order_item_amount) {
        this.qt_order_item_amount = qt_order_item_amount;
    }

    public int getQt_order_item_delivery_fee() {
        return qt_order_item_delivery_fee;
    }

    public void setQt_order_item_delivery_fee(int qt_order_item_delivery_fee) {
        this.qt_order_item_delivery_fee = qt_order_item_delivery_fee;
    }

    public String getSt_payment() {
        return st_payment;
    }

    public void setSt_payment(String st_payment) {
        this.st_payment = st_payment;
    }

    public String getNo_register() {
        return no_register;
    }

    public void setNo_register(String no_register) {
        this.no_register = no_register;
    }

    public String getDa_first_date() {
        return da_first_date;
    }

    public void setDa_first_date(String da_first_date) {
        this.da_first_date = da_first_date;
    }

}