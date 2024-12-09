package com.javabatis.builder;

import com.javabatis.bean.Constants;
import com.javabatis.bean.FieldInfo;
import com.javabatis.bean.TableInfo;
import com.javabatis.utils.DateUtils;
import com.javabatis.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;

public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_PO);
        if(!folder.exists()){
            folder.mkdirs();
        }
        File pofile = new File(folder,tableInfo.getBeanName()+".java");
//        try {
//            file.createNewFile();
//            System.out.println("PO文件夹建立成功");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        正式开始写文件
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try{
            out = new FileOutputStream(pofile);
            outw = new OutputStreamWriter(out,"utf8");
            bw  =new BufferedWriter(outw);

            bw.write("package "+Constants.PACKAGE_PO+";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.io.Serializable;");
            bw.newLine();
            if(tableInfo.isHaveBigDecimal()){
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            if(tableInfo.isHaveDate() || tableInfo.isHavedatetime()){
                bw.write("import java.util.Date;");
                bw.newLine();
                bw.write(Constants.BEAM_DATE_FORMAT_CLASS+";");
                bw.newLine();
                bw.write(Constants.BEAM_DATE_UNFORMAT_CLASS+";");
                bw.newLine();
                bw.write("import "+Constants.PACKAGE_ENUM+".DateTimePatternEnum;");
                bw.newLine();
                bw.write("import "+Constants.PACKAGE_UTILS+".DateUtils;");
                bw.newLine();
            }

//            导入忽略属性的包
            Boolean haveIgnore=false;
            for(FieldInfo fieldInfo: tableInfo.getFieldList()){
                if(ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","),fieldInfo.getPropertyName())){
                    haveIgnore=true;
                    break;
                }
            }
            if(haveIgnore) {
                bw.write(Constants.IGNORE_BEAN_TOJSON_CLASS+";");
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();

//            类注释
            BuildComment.createClassComment(bw, tableInfo.getComment());
            bw.write("public class "+tableInfo.getBeanName()+" implements Serializable {");
            bw.newLine();

//            获取属性
            for(FieldInfo fieldInfo: tableInfo.getFieldList()){
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType())){
                    bw.write("\t"+String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_mm_ss));
                    bw.newLine();

                    bw.write("\t"+String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_mm_ss));
                    bw.newLine();
                }
                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())){
                    bw.write("\t"+String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                    bw.write("\t"+String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }
                if(ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","),fieldInfo.getPropertyName())){
                    bw.write("\t"+Constants.IGNORE_BEAN_TOJSON_EXPRESSION);
                    bw.newLine();
                }
                bw.write("\tprivate "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName()+";" );
                bw.newLine();
                bw.newLine();
            }
//            实现属性的get和set方法
            for(FieldInfo fieldInfo: tableInfo.getFieldList()){
                String tmpField= StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName());
                bw.write("\tpublic void set"+tmpField+"("+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName()+"){");
                bw.newLine();
                bw.write("\t\tthis."+fieldInfo.getPropertyName()+"="+fieldInfo.getPropertyName()+";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.write("\tpublic "+fieldInfo.getJavaType()+" get"+tmpField+"(){");
                bw.newLine();
                bw.write("\t\treturn "+fieldInfo.getPropertyName()+";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
            }
//            重写toString方法
            int index=0;
           StringBuffer toString = new StringBuffer();
            for(FieldInfo fieldInfo: tableInfo.getFieldList()){
                index++;
                String propName = fieldInfo.getPropertyName();
                if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType())){
                    propName = "DateUtils.format("+propName+",DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())";
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())) {
                    propName = "DateUtils.format("+propName+",DateTimePatternEnum.YYYY_MM_DD.getPattern())";
                }

                toString.append("\""+fieldInfo.getComment()+":\"+("+fieldInfo.getPropertyName()+" == null ? \"空\" : "+propName+") ");
                if(index<tableInfo.getFieldList().size())
                    toString.append("+").append("\",");
            }
            String toStringStr = toString.toString();
            toStringStr = "\""+toStringStr;
            toStringStr.substring(0,toString.lastIndexOf("\","));
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString() {");
            bw.newLine();
            bw.write("\t\treturn "+toStringStr+";");
            bw.newLine();
            bw.write("\t}");
            bw.write("}");
            bw.flush();
        }catch (Exception e){
            logger.error("创建po失败",e);
        }finally {
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outw!=null){
                try {
                    outw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
        }
    }
}
