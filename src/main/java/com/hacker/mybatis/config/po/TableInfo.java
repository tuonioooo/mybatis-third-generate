package com.hacker.mybatis.config.po;


import com.hacker.mybatis.config.ConstVal;
import com.hacker.mybatis.config.StrategyConfig;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 表信息，关联到当前字段信息
 *
 * @author YangHu
 * @since 2016/8/30
 */
public class TableInfo {

    private String name;
    private String comment;

    private String entityName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;

    private List<TableField> fields;
    private String fieldNames;
    private boolean hasDate;
    private String PRI;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEntityName() {
        try{
            if(this.name.endsWith(ConstVal.TABLE_SUFFIX)){
                return this.entityName.substring(0, this.entityName.lastIndexOf(ConstVal.TABLE_SUFFIX_ENTITY));
            }
        }catch (Exception e){
            return entityName;
        }
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public void setServiceImplName(String serviceImplName) {
        this.serviceImplName = serviceImplName;
    }

    public List<TableField> getFields() {
        return fields;
    }

    public void setFields(List<TableField> fields) {
        this.fields = fields;
    }

    /**
     * 转换filed实体为xmlmapper中的basecolumn字符串信息
     *
     * @return
     */
    public String getFieldNames() {
        if (StringUtils.isBlank(fieldNames)) {
            StringBuilder names = new StringBuilder();
            for (int i = 0; i < fields.size(); i++) {
                TableField fd = fields.get(i);
                if (i == fields.size() - 1) {
                    names.append(cov2col(fd));
                } else {
                    names.append(cov2col(fd)).append(", ");
                }
            }
            fieldNames = names.toString();
        }
        return fieldNames;
    }

    /**
     * 判断字段中是否包含日期类型
     *
     * @return 是否
     */
    public boolean isHasDate() {
        for (TableField fieldInfo : fields) {
            if (fieldInfo.getPropertyType().equals("Date")) {
                hasDate = true;
                break;
            }
        }
        return hasDate;
    }

    /**
     * 获取表的主键字段PropertyName
     * @return
     */
    public String getPRI(){
        for (TableField fieldInfo : fields) {
            if (fieldInfo.isKeyFlag()) {
                PRI = fieldInfo.getPropertyName();
                break;
            }
        }
        return PRI;
    }

    /**
     * mapper xml中的字字段添加as
     *
     * @param field 字段实体
     * @return 转换后的信息
     */
    private String cov2col(TableField field) {
        if (null != field) {
            return field.isConvert() ? field.getName() + " AS " + field.getPropertyName() : field.getName();
        }
        return StringUtils.EMPTY;
    }
}
