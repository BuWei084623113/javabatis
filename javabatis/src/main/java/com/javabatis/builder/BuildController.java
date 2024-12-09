package com.javabatis.builder;

import com.javabatis.bean.Constants;
import com.javabatis.bean.FieldInfo;
import com.javabatis.bean.TableInfo;
import com.javabatis.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildController {
    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_CONTROLLER);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName()+"Controller";
        String serviceName = tableInfo.getBeanName()+"Service";
        String serviceBeanName = StringUtils.lowerCaseFirstLetter(serviceName);
        File pofile = new File(folder,className+".java");
//        正式开始写文件
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try{
            out = new FileOutputStream(pofile);
            outw = new OutputStreamWriter(out,"utf8");
            bw  =new BufferedWriter(outw);

            bw.write("package "+Constants.PACKAGE_CONTROLLER+";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.io.Serializable;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;");
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

            bw.write("import jakarta.annotation.Resource;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestBody;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RestController;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanParamName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_VO+".PaginationResultVO;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_SERVICE+"."+ serviceName +";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+".SimplePage;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_ENUM+".PageSize;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_VO+".ResponseVO;");
            bw.newLine();

            BuildComment.createClassComment(bw,tableInfo.getComment()+"Controller");
            bw.newLine();
            bw.write("@RestController");
            bw.newLine();
            bw.write("@RequestMapping(\"/"+StringUtils.lowerCaseFirstLetter(tableInfo.getBeanName())+"\")");
            bw.newLine();
            bw.write("public class "+className+" extends ABaseController{");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate "+ serviceName+" "+ serviceBeanName +";");
            bw.newLine();
            bw.write("\t@RequestMapping(\"loadDataList\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO loadDataList("+tableInfo.getBeanParamName()+" query) {");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO("+serviceBeanName+".findCountByPage(query));");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            bw.newLine();
            bw.write("\t@RequestMapping(\"add\")");
            bw.newLine();
            BuildComment.createFieldComment(bw,"新增");
            bw.write("\tpublic ResponseVO add("+tableInfo.getBeanName()+" bean) {");
            bw.newLine();
            bw.write("\t\tthis."+ serviceBeanName +".add(bean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");

            bw.newLine();
            bw.write("\t@RequestMapping(\"addBatch\")");
            bw.newLine();
            BuildComment.createFieldComment(bw,"批量新增");
            bw.write("\tpublic ResponseVO addBatch(@RequestBody List<"+tableInfo.getBeanName()+"> listbean) {");
            bw.newLine();
            bw.write("\t\tthis."+ serviceBeanName +".addBatch(listbean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");

            bw.newLine();
            bw.write("\t@RequestMapping(\"addOrUpdateBatch\")");
            bw.newLine();
            BuildComment.createFieldComment(bw,"批量新增或修改");
            bw.write("\tpublic ResponseVO addOrUpdateBatch(@RequestBody List<"+tableInfo.getBeanName()+"> listbean) {");
            bw.newLine();
            bw.write("\t\tthis."+ serviceBeanName +".addOrUpdateBatch(listbean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            for(Map.Entry<String, List<FieldInfo>> entry: tableInfo.getKeyIndexMap().entrySet()){
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParams = new StringBuilder();
                StringBuilder paramsBuffer = new StringBuilder();
                for(FieldInfo fieldInfo: keyFieldInfoList) {
                    index++;
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < keyFieldInfoList.size()) {
                        methodName.append("And");
                    }
                    methodParams.append(fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName());
                    paramsBuffer.append(fieldInfo.getPropertyName());
                    if (index < keyFieldInfoList.size()) {
                        methodParams.append(",");
                        paramsBuffer.append(",");
                    }
                }

                bw.newLine();
                bw.write("\t@RequestMapping(\"get"+tableInfo.getBeanName()+"By"+methodName+"\")");
                BuildComment.createFieldComment(bw,"根据"+methodName+"查询");
                bw.write("\tpublic ResponseVO get"+tableInfo.getBeanName()+"By"+methodName+"("+methodParams+") {");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(this."+ serviceBeanName +".get"+tableInfo.getBeanName()+"By"+methodName+"("+paramsBuffer+"));");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                bw.newLine();
                bw.write("\t@RequestMapping(\"update"+tableInfo.getBeanName()+"By"+methodName+"\")");
                BuildComment.createFieldComment(bw,"根据"+methodName+"更新");
                bw.write("\tpublic ResponseVO update"+tableInfo.getBeanName()+"By"+methodName+"("+tableInfo.getBeanName()+" bean, "+methodParams+") {");
                bw.newLine();
                bw.write("\t\tthis."+ serviceBeanName +".update"+tableInfo.getBeanName()+"By"+methodName+"(bean,"+paramsBuffer+");");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(null);");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                bw.newLine();
                bw.write("\t@RequestMapping(\"delete"+tableInfo.getBeanName()+"By"+methodName+"\")");
                bw.newLine();
                BuildComment.createFieldComment(bw,"根据"+methodName+"删除");
                bw.write("\tpublic ResponseVO delete"+tableInfo.getBeanName()+"By"+methodName+"("+methodParams+") {");
                bw.newLine();
                bw.write("\t\tthis."+ serviceBeanName +".delete"+tableInfo.getBeanName()+"By"+methodName+"("+paramsBuffer+");");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(null);");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
            }
            bw.newLine();
            bw.write("}");
            bw.flush();
        }catch (Exception e){
            logger.error("创建Controller失败",e);
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
