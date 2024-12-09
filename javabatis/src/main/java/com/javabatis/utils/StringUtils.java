package com.javabatis.utils;

public class StringUtils {
    //将一个单词转化为首字母大写的
    public static String uperCaseFirstLetter(String field){
        if(org.apache.commons.lang3.StringUtils.isEmpty(field)){
            return field;
        }
        return field.substring(0,1).toUpperCase()+field.substring(1);
    }
    //将一个单词转化为首字母小写的
    public static String lowerCaseFirstLetter(String field){
        if(org.apache.commons.lang3.StringUtils.isEmpty(field)){
            return field;
        }
        return field.substring(0,1).toLowerCase()+field.substring(1);
    }
}
