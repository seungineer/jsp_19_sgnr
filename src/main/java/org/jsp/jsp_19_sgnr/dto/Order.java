package org.jsp.jsp_19_sgnr.dto;

/**
 * DTO class for TB_ORDER table.
 */
public class Order {
    private String id_order;
    private String no_user;
    private int qt_order_amount;
    private int qt_deli_money;
    private int qt_deli_period;
    private String nm_order_person;
    private String nm_receiver;
    private String no_delivery_zipno;
    private String nm_delivery_address;
    private String nm_receiver_telno;
    private String nm_delivery_space;
    private String cd_order_type;
    private String da_order;
    private String st_order;
    private String st_payment;
    private String no_register;
    private String da_first_date;

    /**
     * Default constructor
     */
    public Order() {
    }

    /**
     * Constructor with all fields
     */
    public Order(String id_order, String no_user, int qt_order_amount, int qt_deli_money, int qt_deli_period,
                String nm_order_person, String nm_receiver, String no_delivery_zipno, String nm_delivery_address,
                String nm_receiver_telno, String nm_delivery_space, String cd_order_type, String da_order,
                String st_order, String st_payment, String no_register, String da_first_date) {
        this.id_order = id_order;
        this.no_user = no_user;
        this.qt_order_amount = qt_order_amount;
        this.qt_deli_money = qt_deli_money;
        this.qt_deli_period = qt_deli_period;
        this.nm_order_person = nm_order_person;
        this.nm_receiver = nm_receiver;
        this.no_delivery_zipno = no_delivery_zipno;
        this.nm_delivery_address = nm_delivery_address;
        this.nm_receiver_telno = nm_receiver_telno;
        this.nm_delivery_space = nm_delivery_space;
        this.cd_order_type = cd_order_type;
        this.da_order = da_order;
        this.st_order = st_order;
        this.st_payment = st_payment;
        this.no_register = no_register;
        this.da_first_date = da_first_date;
    }

    // Getters and Setters
    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getNo_user() {
        return no_user;
    }

    public void setNo_user(String no_user) {
        this.no_user = no_user;
    }

    public int getQt_order_amount() {
        return qt_order_amount;
    }

    public void setQt_order_amount(int qt_order_amount) {
        this.qt_order_amount = qt_order_amount;
    }

    public int getQt_deli_money() {
        return qt_deli_money;
    }

    public void setQt_deli_money(int qt_deli_money) {
        this.qt_deli_money = qt_deli_money;
    }

    public int getQt_deli_period() {
        return qt_deli_period;
    }

    public void setQt_deli_period(int qt_deli_period) {
        this.qt_deli_period = qt_deli_period;
    }

    public String getNm_order_person() {
        return nm_order_person;
    }

    public void setNm_order_person(String nm_order_person) {
        this.nm_order_person = nm_order_person;
    }

    public String getNm_receiver() {
        return nm_receiver;
    }

    public void setNm_receiver(String nm_receiver) {
        this.nm_receiver = nm_receiver;
    }

    public String getNo_delivery_zipno() {
        return no_delivery_zipno;
    }

    public void setNo_delivery_zipno(String no_delivery_zipno) {
        this.no_delivery_zipno = no_delivery_zipno;
    }

    public String getNm_delivery_address() {
        return nm_delivery_address;
    }

    public void setNm_delivery_address(String nm_delivery_address) {
        this.nm_delivery_address = nm_delivery_address;
    }

    public String getNm_receiver_telno() {
        return nm_receiver_telno;
    }

    public void setNm_receiver_telno(String nm_receiver_telno) {
        this.nm_receiver_telno = nm_receiver_telno;
    }

    public String getNm_delivery_space() {
        return nm_delivery_space;
    }

    public void setNm_delivery_space(String nm_delivery_space) {
        this.nm_delivery_space = nm_delivery_space;
    }

    public String getCd_order_type() {
        return cd_order_type;
    }

    public void setCd_order_type(String cd_order_type) {
        this.cd_order_type = cd_order_type;
    }

    public String getDa_order() {
        return da_order;
    }

    public void setDa_order(String da_order) {
        this.da_order = da_order;
    }

    public String getSt_order() {
        return st_order;
    }

    public void setSt_order(String st_order) {
        this.st_order = st_order;
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

    @Override
    public String toString() {
        return "Order{" +
                "id_order='" + id_order + '\'' +
                ", no_user='" + no_user + '\'' +
                ", qt_order_amount=" + qt_order_amount +
                ", qt_deli_money=" + qt_deli_money +
                ", qt_deli_period=" + qt_deli_period +
                ", nm_order_person='" + nm_order_person + '\'' +
                ", nm_receiver='" + nm_receiver + '\'' +
                ", no_delivery_zipno='" + no_delivery_zipno + '\'' +
                ", nm_delivery_address='" + nm_delivery_address + '\'' +
                ", nm_receiver_telno='" + nm_receiver_telno + '\'' +
                ", nm_delivery_space='" + nm_delivery_space + '\'' +
                ", cd_order_type='" + cd_order_type + '\'' +
                ", da_order='" + da_order + '\'' +
                ", st_order='" + st_order + '\'' +
                ", st_payment='" + st_payment + '\'' +
                ", no_register='" + no_register + '\'' +
                ", da_first_date='" + da_first_date + '\'' +
                '}';
    }
}