package com.javabatis.builder;

import com.javabatis.bean.Constants;
import com.javabatis.bean.FieldInfo;
import com.javabatis.bean.TableInfo;
import com.javabatis.utils.DateUtils;
import com.javabatis.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_QUERY);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName()+Constants.SUFFIX_BEAN_PARAM;
        File pofile = new File(folder,className+".java");
//        正式开始写文件
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try{
            out = new FileOutputStream(pofile);
            outw = new OutputStreamWriter(out,"utf-8");
            bw  =new BufferedWriter(outw);

            bw.write("package "+Constants.PACKAGE_QUERY+";");
            bw.newLine();
            if(tableInfo.isHaveBigDecimal()){
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            if(tableInfo.isHaveDate() || tableInfo.isHavedatetime()){
                bw.write("import java.util.Date;");
                bw.newLine();
            }

            bw.newLine();
//            注释类
            BuildComment.createClassComment(bw, tableInfo.getComment()+"查询");
            bw.write("public class "+className+" extends BaseParam{");
            bw.newLine();

//            获取属性
            for(FieldInfo fieldInfo: tableInfo.getFieldList()){
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                bw.write("\tprivate "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName()+";" );
                bw.newLine();
                bw.newLine();
//                String类型的参数
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES,fieldInfo.getSqlType())){
                    String propName = fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_PARAM_FUZZY;
                    bw.write("\tprivate "+fieldInfo.getJavaType()+" "+propName+";" );
                    bw.newLine();
                    bw.newLine();
                }
//                日期类型的参数
                if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType())||ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType())){
                    bw.write("\tprivate String "+fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_PARAM_TIME_START+";" );
                    bw.newLine();
                    bw.newLine();

                    bw.write("\tprivate String "+fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_PARAM_TIME_END+";" );
                    bw.newLine();
                    bw.newLine();
                }
            }

//            实现属性的get和set方法
            buildGetSet(bw,tableInfo.getFieldList());
            buildGetSet(bw,tableInfo.getFieldExtendList());
            bw.write("}");
            bw.flush();
        }catch (Exception e){
            logger.error("创建query失败",e);
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

    private static void buildGetSet(BufferedWriter bw,List<FieldInfo> fieldInfoList) throws IOException {
        for(FieldInfo fieldInfo: fieldInfoList){
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
    }
}
