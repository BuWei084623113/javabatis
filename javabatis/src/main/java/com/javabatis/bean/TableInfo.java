package com.javabatis.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableInfo {
//   表名
    private String tableName;
//    bean名称
    private String beanName;
//    参数名称
    private String beanParamName;
//    表注释
    private String comment;
//    字段信息
    private List<FieldInfo> fieldList;

    public List<FieldInfo> getFieldExtendList() {
        return fieldExtendList;
    }

    public void setFieldExtendList(List<FieldInfo> fieldExtendList) {
        this.fieldExtendList = fieldExtendList;
    }

    //    拓展字段信息
    private List<FieldInfo> fieldExtendList;
//    唯一索引集合
    private Map<String,List<FieldInfo>> keyIndexMap = new LinkedHashMap<>();
//    是否有date类型
    private boolean haveDate;
//    是否有datetime类型
    private boolean havedatetime;
//    是否存咋bigdecimal类型
    private boolean haveBigDecimal;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBeanParamName() {
        return beanParamName;
    }

    public void setBeanParamName(String beanParamName) {
        this.beanParamName = beanParamName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldInfo> fieldList) {
        this.fieldList = fieldList;
    }

    public Map<String, List<FieldInfo>> getKeyIndexMap() {
        return keyIndexMap;
    }

    public void setKeyIndexMap(Map<String, List<FieldInfo>> keyIndexMap) {
        this.keyIndexMap = keyIndexMap;
    }

    public boolean isHavedatetime() {
        return havedatetime;
    }

    public void setHavedatetime(boolean havedatetime) {
        this.havedatetime = havedatetime;
    }

    public boolean isHaveDate() {
        return haveDate;
    }

    public void setHaveDate(boolean haveDate) {
        this.haveDate = haveDate;
    }

    public boolean isHaveBigDecimal() {
        return haveBigDecimal;
    }

    public void setHaveBigDecimal(boolean haveBigDecimal) {
        this.haveBigDecimal = haveBigDecimal;
    }
}
