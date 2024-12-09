package com.javabatis.bean;

import com.javabatis.utils.PropertiesUtils;

public class Constants {
//    是否忽略表前缀
    public static Boolean IGNORE_TABLE_PERFIX;
//    后缀
    public static String SUFFIX_BEAN_PARAM;
    public static String SUFFIX_BEAN_PARAM_FUZZY;
    public static String SUFFIX_BEAN_PARAM_TIME_START;
    public static String SUFFIX_BEAN_PARAM_TIME_END;
    public static String SUFFIX_MAPPERS;
//    需要忽略的属性
    public static String IGNORE_BEAN_TOJSON_FILED;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;
//    日期序列化与反序列化
//    序列化
    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAM_DATE_FORMAT_CLASS;
//    反序列化
    public static String BEAN_DATE_UNFORMAT_EXPRESSION;
    public static String BEAM_DATE_UNFORMAT_CLASS;
//定义文件输出路径
    public static String PATH_JAVA = "java";

    public static String PATH_RESOURCES = "resources";

    public static String AUTHOR_COMMENT;
//    路径
    public static String PATH_BASE;
    public static String PATH_PO;
    public static String PATH_UTILS;
    public static String PATH_ENUM;
    public static String PATH_QUERY;
    public static String PATH_MAPPER;
    public static String  PATH_MAPPER_XMLS;
    public static String  PATH_SERVICE;
    public static String  PATH_SERVICE_IPML;
    public static String  PATH_VO;
    public static String  PATH_EXCEPTION;
    public static String  PATH_CONTROLLER;
//包
    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_QUERY;
    public static String PACKAGE_UTILS;
    public static String  PACKAGE_ENUM;
    public static String  PACKAGE_MAPPER;
    public static String  PACKAGE_SERVICE;
    public static String  PACKAGE_SERVICE_IPML;
    public static String  PACKAGE_VO;
    public static String  PACKAGE_EXCEPTION;
    public static String  PACKAGE_CONTROLLER;

    static {
        AUTHOR_COMMENT = PropertiesUtils.getString("author_comment");

        //    需要忽略的属性
        IGNORE_BEAN_TOJSON_FILED = PropertiesUtils.getString("ignore.bean.tojson.filed");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");
//    日期序列化与反序列化
//    序列化
        BEAN_DATE_FORMAT_EXPRESSION= PropertiesUtils.getString("bean.date.format.expression");
        BEAM_DATE_FORMAT_CLASS= PropertiesUtils.getString("beam.date.format.class");
//    反序列化
        BEAN_DATE_UNFORMAT_EXPRESSION= PropertiesUtils.getString("bean.date.unformat.expression");
        BEAM_DATE_UNFORMAT_CLASS= PropertiesUtils.getString("beam.date.unformat.class");

        IGNORE_TABLE_PERFIX = Boolean.valueOf( PropertiesUtils.getString("ignore.table.prefix"));
//        生成后缀
        SUFFIX_BEAN_PARAM  = PropertiesUtils.getString("suffix.bean.param");
        SUFFIX_BEAN_PARAM_FUZZY = PropertiesUtils.getString("suffix.bean.param.fuzzy");
        SUFFIX_BEAN_PARAM_TIME_START = PropertiesUtils.getString("suffix.bean.param.time.start");
        SUFFIX_BEAN_PARAM_TIME_END = PropertiesUtils.getString("suffix.bean.param.time.end");
        SUFFIX_MAPPERS = PropertiesUtils.getString("suffix.mappers");

        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        //package
        PACKAGE_PO = PACKAGE_BASE+"."+PropertiesUtils.getString("package.po");
        PACKAGE_UTILS = PACKAGE_BASE+"."+PropertiesUtils.getString("package.utils");
        PACKAGE_ENUM = PACKAGE_BASE+"."+PropertiesUtils.getString("package.enum");
        PACKAGE_QUERY = PACKAGE_BASE+"."+PropertiesUtils.getString("package.query");
        PACKAGE_MAPPER = PACKAGE_BASE+"."+PropertiesUtils.getString("package.mappers");
        PACKAGE_SERVICE = PACKAGE_BASE+"."+PropertiesUtils.getString("package.service");
        PACKAGE_SERVICE_IPML = PACKAGE_BASE+"."+PropertiesUtils.getString("package.service.ipml");
        PACKAGE_VO = PACKAGE_BASE+"."+PropertiesUtils.getString("package.vo");
        PACKAGE_EXCEPTION = PACKAGE_BASE+"."+PropertiesUtils.getString("package.exception");
        PACKAGE_CONTROLLER = PACKAGE_BASE+"."+PropertiesUtils.getString("package.controller");

        //path
        PATH_BASE = PropertiesUtils.getString("path.base");
        PATH_BASE = PATH_BASE +PATH_JAVA;
        PATH_PO =PATH_BASE+"/"+ PACKAGE_PO.replace(".","/");
        PATH_UTILS = PATH_BASE+"/"+ PACKAGE_UTILS.replace(".","/");
        PATH_ENUM = PATH_BASE+"/"+ PACKAGE_ENUM.replace(".","/");
        PATH_QUERY = PATH_BASE+"/"+ PACKAGE_QUERY.replace(".","/");
        PATH_MAPPER = PATH_BASE+"/"+PACKAGE_MAPPER.replace(".","/");
        PATH_SERVICE = PATH_BASE+"/"+PACKAGE_SERVICE.replace(".","/");
        PATH_SERVICE_IPML = PATH_BASE+"/"+PACKAGE_SERVICE_IPML.replace(".","/");
        PATH_VO = PATH_BASE+"/"+PACKAGE_VO.replace(".","/");
        PATH_EXCEPTION = PATH_BASE+"/"+PACKAGE_EXCEPTION.replace(".","/");
        PATH_CONTROLLER = PATH_BASE+"/"+PACKAGE_CONTROLLER.replace(".","/");

        PATH_MAPPER_XMLS = PropertiesUtils.getString("path.base")+PATH_RESOURCES+"/"+PACKAGE_MAPPER.replace(".","/");
    }

//    数据类型（SQL->java）
//    时间
    public final static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime","timestamp"};
//    日期
    public final static String[] SQL_DATE_TYPES = new String[]{"date"};
//    浮点数
    public final static String[] SQL_DECIMAL_TYPES = new String[]{"decimal","double","float"};
//    字符类：字符和字符串
    public final static String[] SQL_STRING_TYPES = new String[]{"char","varchar","text","mediumtext","longtext"};
//    整型
    public final static String[] SQL_INTEGER_TYPES = new String[]{"int","tinyint"};
//    长整型
    public final static String[] SQL_LONG_TYPES = new String[]{"bigint"};

    public static void main(String[] args){
       System.out.println(PATH_CONTROLLER);
        System.out.println(PATH_ENUM);

    }
}
