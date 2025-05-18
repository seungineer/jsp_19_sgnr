package org.jsp.jsp_19_sgnr.dto;

public class Basket {
    private int basketId;
    private String userEmail;
    private String createDate;
    private String updateDate;
    private String status;

    public Basket() {
    }

    public Basket(int basketId, String userEmail, String createDate, String updateDate, String status) {
        this.basketId = basketId;
        this.userEmail = userEmail;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    public int getBasketId() {
        return basketId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}