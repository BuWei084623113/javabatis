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
import java.util.List;
import java.util.Map;

public class BuildService {
    private static final Logger logger = LoggerFactory.getLogger(BuildService.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_SERVICE);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName()+"Service";
        File pofile = new File(folder,className+".java");
//        正式开始写文件
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try{
            out = new FileOutputStream(pofile);
            outw = new OutputStreamWriter(out,"utf8");
            bw  =new BufferedWriter(outw);

            bw.write("package "+Constants.PACKAGE_SERVICE+";");
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
            bw.write("import java.util.List;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanParamName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_VO+".PaginationResultVO;");
            bw.newLine();

            BuildComment.createClassComment(bw,tableInfo.getComment()+"Service");
            bw.write("public interface "+className+" {");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw,"根据条件查询列表");
            bw.write("\tList<"+tableInfo.getBeanName()+"> findListByParam("+tableInfo.getBeanParamName()+" param);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"根据条件查询数量");
            bw.write("\tInteger findCountByParam("+tableInfo.getBeanParamName()+" param);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"分页查询");
            bw.write("\tPaginationResultVO<"+tableInfo.getBeanName()+"> findCountByPage("+tableInfo.getBeanParamName()+" param);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"新增");
            bw.write("\tInteger add("+tableInfo.getBeanName()+" bean);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"批量新增");
            bw.write("\tInteger addBatch(List<"+tableInfo.getBeanName()+"> listbean);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw,"批量新增或修改");
            bw.write("\tInteger addOrUpdateBatch(List<"+tableInfo.getBeanName()+"> listbean);");

            for(Map.Entry<String, List<FieldInfo>> entry: tableInfo.getKeyIndexMap().entrySet()){
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParams = new StringBuilder();
                for(FieldInfo fieldInfo: keyFieldInfoList) {
                    index++;
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < keyFieldInfoList.size()) {
                        methodName.append("And");
                    }
                    methodParams.append(fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName());
                    if (index < keyFieldInfoList.size()) {
                        methodParams.append(",");
                    }
                }

                BuildComment.createFieldComment(bw,"根据"+methodName+"查询");
                bw.write("\t"+tableInfo.getBeanName()+" get"+tableInfo.getBeanName()+"By"+methodName+"("+methodParams+");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw,"根据"+methodName+"更新");
                bw.write("\t Integer update"+tableInfo.getBeanName()+"By"+methodName+"("+tableInfo.getBeanName()+" t, "+methodParams+");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw,"根据"+methodName+"删除");
                bw.write("\t Integer delete"+tableInfo.getBeanName()+"By"+methodName+"("+methodParams+");");
                bw.newLine();
                bw.newLine();
            }
            bw.newLine();
            bw.write("}");
            bw.flush();
        }catch (Exception e){
            logger.error("创建Service失败",e);
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
