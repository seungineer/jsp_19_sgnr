package org.jsp.jsp_19_sgnr.dto;

public class Category {
    private int id;
    private String name;
    private Integer upperId;
    private int order;
    private String regDate;
    private int level;
    private String fullname;
    private String ynUse;

    private String regName;

    public Category() {}

    public Category(int id, String name, Integer upperId, int order, String regDate, Integer level) {
        this.id = id;
        this.name = name;
        this.upperId = upperId;
        this.order = order;
        this.regDate = regDate;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUpperId() {
        return upperId;
    }

    public void setUpperId(Integer upperId) {
        this.upperId = upperId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFullname() {
        return fullname;
    }

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDisplayName() {
        return upperId == null ? name : "[" + upperId + "] " + name;
    }

    public String getYnUse() {
        return ynUse;
    }

    public void setYnUse(String ynUse) {
        this.ynUse = ynUse;
    }
}
