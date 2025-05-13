package org.jsp.jsp_19_sgnr.dto;

public class Product {
    private String no_product;
    private String nm_product;
    private String nm_detail_explain;
    private String dt_start_date;
    private String dt_end_date;
    private int qt_customer_price;
    private int qt_sale_price;
    private int qt_stock;
    private int qt_delivery_fee;
    private String no_register;
    private int sale_status;
    private String id_file;

    public String getNo_product() {
        return no_product;
    }

    public void setNo_product(String no_product) {
        this.no_product = no_product;
    }

    public String getNm_product() {
        return nm_product;
    }

    public void setNm_product(String nm_product) {
        this.nm_product = nm_product;
    }

    public String getNm_detail_explain() {
        return nm_detail_explain;
    }

    public void setNm_detail_explain(String nm_detail_explain) {
        this.nm_detail_explain = nm_detail_explain;
    }

    public String getDt_start_date() {
        return dt_start_date;
    }

    public void setDt_start_date(String dt_start_date) {
        this.dt_start_date = dt_start_date;
    }

    public String getDt_end_date() {
        return dt_end_date;
    }

    public void setDt_end_date(String dt_end_date) {
        this.dt_end_date = dt_end_date;
    }

    public int getQt_customer_price() {
        return qt_customer_price;
    }

    public void setQt_customer_price(int qt_customer) {
        this.qt_customer_price = qt_customer;
    }

    public int getQt_sale_price() {
        return qt_sale_price;
    }

    public void setQt_sale_price(int qt_sale_price) {
        this.qt_sale_price = qt_sale_price;
    }

    public int getQt_stock() {
        return qt_stock;
    }

    public void setQt_stock(int qt_stock) {
        this.qt_stock = qt_stock;
    }

    public int getQt_delivery_fee() {
        return qt_delivery_fee;
    }

    public void setQt_delivery_fee(int qt_delivery_fee) {
        this.qt_delivery_fee = qt_delivery_fee;
    }

    public String getNo_register() {
        return no_register;
    }

    public void setNo_register(String no_register) {
        this.no_register = no_register;
    }

    public int getSale_status() {
        return sale_status;
    }

    public void setSale_status(int sale_status) {
        this.sale_status = sale_status;
    }

    public String getId_file() {
        return id_file;
    }

    public void setId_file(String id_file) {
        this.id_file = id_file;
    }
}
