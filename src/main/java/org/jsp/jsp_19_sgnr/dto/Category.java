package org.jsp.jsp_19_sgnr.dto;

public class Category {
    private int nbCategory;
    private String nmCategory;
    private Integer nbParentCategory;
    private int cnOrder;
    private String daFirstDate;
    private int cnLevel;
    private String nmFullCategory;
    private String nmExplain;
    private String ynUse;
    private String ynDelete;

    private String noRegister;

    public Category() {}

    public Category(int nbCategory, String nmCategory, Integer nbParentCategory, int cnOrder, String daFirstDate, Integer cnLevel) {
        this.nbCategory = nbCategory;
        this.nmCategory = nmCategory;
        this.nbParentCategory = nbParentCategory;
        this.cnOrder = cnOrder;
        this.daFirstDate = daFirstDate;
        this.cnLevel = cnLevel;
    }

    public int getNbCategory() {
        return nbCategory;
    }

    public void setNbCategory(int nbCategory) {
        this.nbCategory = nbCategory;
    }

    // Keeping backward compatibility
    public int getId() {
        return nbCategory;
    }

    public void setId(int id) {
        this.nbCategory = id;
    }

    public String getNmCategory() {
        return nmCategory;
    }

    public void setNmCategory(String nmCategory) {
        this.nmCategory = nmCategory;
    }

    // Keeping backward compatibility
    public String getName() {
        return nmCategory;
    }

    public void setName(String name) {
        this.nmCategory = name;
    }

    public Integer getNbParentCategory() {
        return nbParentCategory;
    }

    public void setNbParentCategory(Integer nbParentCategory) {
        this.nbParentCategory = nbParentCategory;
    }

    // Keeping backward compatibility
    public Integer getUpperId() {
        return nbParentCategory;
    }

    public void setUpperId(Integer upperId) {
        this.nbParentCategory = upperId;
    }

    public int getCnOrder() {
        return cnOrder;
    }

    public void setCnOrder(int cnOrder) {
        this.cnOrder = cnOrder;
    }

    // Keeping backward compatibility
    public int getOrder() {
        return cnOrder;
    }

    public void setOrder(int order) {
        this.cnOrder = order;
    }

    public String getDaFirstDate() {
        return daFirstDate;
    }

    public void setDaFirstDate(String daFirstDate) {
        this.daFirstDate = daFirstDate;
    }

    // Keeping backward compatibility
    public String getRegDate() {
        return daFirstDate;
    }

    public void setRegDate(String regDate) {
        this.daFirstDate = regDate;
    }

    public int getCnLevel() {
        return cnLevel;
    }

    public void setCnLevel(int cnLevel) {
        this.cnLevel = cnLevel;
    }

    // Keeping backward compatibility
    public int getLevel() {
        return cnLevel;
    }

    public void setLevel(int level) {
        this.cnLevel = level;
    }

    public String getNmFullCategory() {
        return nmFullCategory;
    }

    public void setNmFullCategory(String nmFullCategory) {
        this.nmFullCategory = nmFullCategory;
    }

    // Keeping backward compatibility
    public String getFullname() {
        return nmFullCategory;
    }

    public void setFullname(String fullname) {
        this.nmFullCategory = fullname;
    }

    public String getNoRegister() {
        return noRegister;
    }

    public void setNoRegister(String noRegister) {
        this.noRegister = noRegister;
    }

    // Keeping backward compatibility
    public String getRegName() {
        return noRegister;
    }

    public void setRegName(String regName) {
        this.noRegister = regName;
    }

    public String getNmExplain() {
        return nmExplain;
    }

    public void setNmExplain(String nmExplain) {
        this.nmExplain = nmExplain;
    }

    public String getYnDelete() {
        return ynDelete;
    }

    public void setYnDelete(String ynDelete) {
        this.ynDelete = ynDelete;
    }

    public String getDisplayName() {
        return nbParentCategory == null ? nmCategory : "[" + nbParentCategory + "] " + nmCategory;
    }

    public String getYnUse() {
        return ynUse;
    }

    public void setYnUse(String ynUse) {
        this.ynUse = ynUse;
    }
}
