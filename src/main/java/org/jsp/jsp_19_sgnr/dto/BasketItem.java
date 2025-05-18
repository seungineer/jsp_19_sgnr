package org.jsp.jsp_19_sgnr.dto;

public class BasketItem {
    private int itemId;
    private int basketId;
    private String productId;
    private int quantity;
    private int price;
    private String createDate;
    private String updateDate;
    private boolean selected;

    private String productName;
    private String productImage;
    
    public BasketItem() {
    }

    public BasketItem(int itemId, int basketId, String productId, int quantity, int price, 
                     String createDate, String updateDate, boolean selected) {
        this.itemId = itemId;
        this.basketId = basketId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.selected = selected;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getBasketId() {
        return basketId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    
    // Calculate total price for this item
    public int getTotalPrice() {
        return price * quantity;
    }
}