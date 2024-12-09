package com.javabatis.builder;

import com.alibaba.fastjson.JSON;
import com.javabatis.bean.Constants;
import com.javabatis.bean.FieldInfo;
import com.javabatis.bean.TableInfo;
import com.javabatis.utils.JsonUtils;
import com.javabatis.utils.PropertiesUtils;
import com.javabatis.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取数据表
 * 更新时间：2024/11/21
 * */
public class BuildTable {
//    创建日志
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;
    public static String SQL_SHOW_TABLE_STATUS = "show table status";
    public static String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";
    public static String SQL_SHOW_TABLE_INDEX = "show index from %s";
//    数据库连接
    static {
//        调用application.properties中的配置，比直接写在代码里好得多
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String user = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            logger.error("数据库连接失败",e);
        }
    }
    /**
     * 读取表的结构和基本信息，MySQL查看数据库中所有表的详细信息：show table status;
     * 更新时间：2024/11/20
     * */
    public static List<TableInfo> getTable() {
        PreparedStatement ps = null;
        ResultSet tableResult = null;

        List<TableInfo> tableInfoList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            while (tableResult.next()) {
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
                logger.info("tableName:{},comment:{}",tableName,comment);
                String beanName = tableName;
//                判断是否忽略前缀
                if (Constants.IGNORE_TABLE_PERFIX) {
//                    忽略前缀
                    beanName = tableName.substring(beanName.indexOf("_") + 1);
                }
                //                    对beanName进行处理
                beanName = processFiled(beanName, true);
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAM);
                readFieldInfo(tableInfo);
                getKeyIndexInfo(tableInfo);
                tableInfoList.add(tableInfo);
            }
        } catch (SQLException e) {
            logger.error("表解析失败！");
        } finally {
            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tableInfoList;
    }

    /**
     * 读取表的字段，MySQL查询语句：show full fields from 表名;
     * 更新时间：2024/11/20
     * */
    private static void readFieldInfo(TableInfo tableInfo){
        PreparedStatement ps = null;
        ResultSet fieldResult = null;

        List<FieldInfo> fieldsInfo = new ArrayList<>();
        List<FieldInfo> fieldsExtendInfo = new ArrayList<>();
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS,tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            Boolean haveDate = false;
            Boolean haveDateTime = false;
            Boolean haveBigDecimal = false;
            while(fieldResult.next()){
                String field = fieldResult.getString("field");
                String type = fieldResult.getString("type");
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");

//                logger.info("field：{}，类型：{}，Extra：{}，注解：{}",field,type,extra,comment);
                if(type.indexOf("(")>0)
                    type = type.substring(0,type.indexOf("("));
                String propertyName = processFiled(field,false);

                FieldInfo fieldInfo = new FieldInfo();
                fieldsInfo.add(fieldInfo);

                fieldInfo.setFieldName(field);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra));
                fieldInfo.setJavaType(processJavaType(type));

                fieldInfo.setPropertyName(propertyName);

                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,type)){
                        haveDate = true;
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,type)){
                    haveDateTime = true;
                }
                if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES,type)){
                    haveBigDecimal = true;
                }
//                拓展实现
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES,type)){
                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType(fieldInfo.getJavaType());
                    fuzzyField.setPropertyName(propertyName+Constants.SUFFIX_BEAN_PARAM_FUZZY);
                    fuzzyField.setFieldName(fieldInfo.getFieldName());
                    fuzzyField.setSqlType(type);
                    fieldsExtendInfo.add(fuzzyField);
                }

                //                日期类型的参数
                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,type)||ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,type)){
                    FieldInfo timeStartField = new FieldInfo();
                    timeStartField.setJavaType("String");
                    timeStartField.setPropertyName(propertyName+Constants.SUFFIX_BEAN_PARAM_TIME_START);
                    timeStartField.setFieldName(fieldInfo.getFieldName());
                    timeStartField.setSqlType(type);
                    fieldsExtendInfo.add(timeStartField);

                    FieldInfo timeEndField = new FieldInfo();
                    timeEndField.setJavaType("String");
                    timeEndField.setPropertyName(propertyName+Constants.SUFFIX_BEAN_PARAM_TIME_END);
                    timeEndField.setFieldName(fieldInfo.getFieldName());
                    timeEndField.setSqlType(type);
                    fieldsExtendInfo.add(timeEndField);
                }
            }
            tableInfo.setHavedatetime(haveDateTime);
            tableInfo.setHaveDate(haveDate);
            tableInfo.setHaveBigDecimal(haveBigDecimal);
            tableInfo.setFieldList(fieldsInfo);
            tableInfo.setFieldExtendList(fieldsExtendInfo);
        } catch (SQLException e) {
            logger.error("表解析失败！");
        }finally {
            if(fieldResult!=null){
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取表的索引：Mysql语句：show index from 表
     * 更新时间：2024/11/21
     * */
    private static List<FieldInfo> getKeyIndexInfo(TableInfo tableInfo){
        PreparedStatement ps = null;
        ResultSet fieldResult = null;

        List<FieldInfo> fieldsInfo = new ArrayList<>();
        try {
//            建立临时哈希表，提高获取速率
            Map<String,FieldInfo> tmpMap = new HashMap<>();
            for(FieldInfo fieldInfo : tableInfo.getFieldList()){
                tmpMap.put(fieldInfo.getFieldName(),fieldInfo);
            }
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX,tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while(fieldResult.next()) {
                String keyName = fieldResult.getString("key_name");
                Integer nonUnique = fieldResult.getInt("non_unique");
                String columnName = fieldResult.getString("column_name");
                if(nonUnique==1){
                    continue;
                }
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if(keyFieldList==null){
                    keyFieldList = new ArrayList<>();
                    tableInfo.getKeyIndexMap().put(keyName,keyFieldList);
                }
//                for(FieldInfo fieldInfo :tableInfo.getFieldList()) {
//                    if (fieldInfo.getFieldName().equals(columnName)) {
//                        keyFieldList.add(fieldInfo);
//                    }
//                }
                keyFieldList.add(tmpMap.get(columnName));
            }
        } catch (SQLException e) {
            logger.error("读取索引失败！");
        }finally {
            if(fieldResult!=null){
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return fieldsInfo;
    }

//    _命名转驼峰命名
    private static String processFiled(String field,Boolean uperCaseFirstLetter){
        StringBuffer sb = new StringBuffer();
        String[] fields = field.split("_");
        sb.append(uperCaseFirstLetter?StringUtils.uperCaseFirstLetter(fields[0]):fields[0]);
        for (int i = 1; i < fields.length; i++) {
            sb.append(StringUtils.uperCaseFirstLetter(fields[i]));
        }
        return sb.toString();
    }
//    SQL数据类型转java数据类型
    private static String processJavaType(String type){
        if(ArrayUtils.contains(Constants.SQL_INTEGER_TYPES,type)){
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPES,type)) {
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES,type) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,type)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPES,type)) {
            return "Long";
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES,type)) {
            return "BigDecimal";
        }else {
            throw new RuntimeException("无法识别类型："+type);
        }
    }
}
