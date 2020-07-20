package com.hacker.mybatis.config.po;

/**
 * Developer:YangHu
 * Date:2016-9-3
 * Describe: 字段信息
 */
public class TableField {

    private boolean keyFlag;
    private String name;
    private String type;
    private String capitalType;
    private String propertyName;
    private String capitalName;
    private String propertyType;
    private String tsPropertyType;
    private String comment;
    private boolean convert;


    public String getTsPropertyType() {
        return tsPropertyType;
    }

    public void setTsPropertyType(String tsPropertyType) {
        this.tsPropertyType = tsPropertyType;
    }

    public boolean isKeyFlag() {
        return keyFlag;
    }

    public void setKeyFlag(boolean keyFlag) {
        this.keyFlag = keyFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isConvert() {
        return !name.equals(propertyName);
    }

    public String getCapitalName() {
        return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    public String getCapitalType() {
        return capitalType;
    }

    public void setCapitalType(String capitalType) {
        this.capitalType = capitalType;
    }

    public void setCapitalName(String capitalName) {
        this.capitalName = capitalName;
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    /**
     * 判断属性字段中是否包含日期类型
     * @return 是否
     */
    public boolean isHasDate() {
        return this.getPropertyType().equals("Date");
    }
}
