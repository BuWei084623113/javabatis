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

public class BuildServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BuildServiceImpl.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_SERVICE_IPML);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String interfaceName = tableInfo.getBeanName()+"Service";
        String className = tableInfo.getBeanName()+"ServiceImpl";
        String mapperName = tableInfo.getBeanName()+Constants.SUFFIX_MAPPERS;
        String mapperBeanName = StringUtils.lowerCaseFirstLetter(mapperName);
        File pofile = new File(folder,className+".java");
//        正式开始写文件
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try{
            out = new FileOutputStream(pofile);
            outw = new OutputStreamWriter(out,"utf8");
            bw  =new BufferedWriter(outw);

            bw.write("package "+Constants.PACKAGE_SERVICE_IPML+";");
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

            bw.write("import org.springframework.stereotype.Service;");
            bw.newLine();
            bw.write("import jakarta.annotation.Resource;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanParamName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_VO+".PaginationResultVO;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_SERVICE+"."+interfaceName+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_MAPPER+"."+mapperName+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+".SimplePage;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_ENUM+".PageSize;");
            bw.newLine();

            BuildComment.createClassComment(bw,tableInfo.getComment()+"Service");
            bw.write("@Service(\""+StringUtils.lowerCaseFirstLetter(interfaceName)+"\")");
            bw.newLine();
            bw.write("public class "+className+" implements "+interfaceName+" {");
            bw.newLine();
            bw.newLine();
            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate "+mapperName+"<"+tableInfo.getBeanName()+","+tableInfo.getBeanParamName()+">"+mapperBeanName+";");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw,"根据条件查询列表");
            bw.write("\tpublic List<"+tableInfo.getBeanName()+"> findListByParam("+tableInfo.getBeanParamName()+" param) {");
            bw.newLine();
            bw.write("\t\treturn this."+mapperBeanName+".selectList(param);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            BuildComment.createFieldComment(bw,"根据条件查询数量");
            bw.write("\tpublic Integer findCountByParam("+tableInfo.getBeanParamName()+" param) {");
            bw.newLine();
            bw.write("\t\treturn this."+mapperBeanName+".selectCount(param);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuildComment.createFieldComment(bw,"分页查询");
            bw.write("\tpublic PaginationResultVO<"+tableInfo.getBeanName()+"> findCountByPage("+tableInfo.getBeanParamName()+" param) {");
            bw.newLine();
            bw.write("\t\tint count = this.findCountByParam(param);");
            bw.newLine();
            bw.write("\t\tint pageSize =param.getPageSize()==null ? PageSize.SIZE15.getSize():param.getPageSize();");
            bw.newLine();
            bw.write("\t\tSimplePage page = new SimplePage(param.getPageNo(),count,pageSize);");
            bw.newLine();
            bw.write("\t\tparam.setSimplePage(page);");
            bw.newLine();
            bw.write("\t\tList<"+tableInfo.getBeanName()+"> list = this.findListByParam(param);");
            bw.newLine();
            bw.write("\t\tPaginationResultVO<"+tableInfo.getBeanName()+"> result = new PaginationResultVO(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);");
            bw.newLine();
            bw.write("\t\treturn result;");
            bw.newLine();
            bw.write("\t}");

            bw.newLine();
            BuildComment.createFieldComment(bw,"新增");
            bw.write("\tpublic Integer add("+tableInfo.getBeanName()+" bean) {");
            bw.newLine();
            bw.write("\t\treturn this."+mapperBeanName+".insert(bean);");
            bw.newLine();
            bw.write("\t}");

            bw.newLine();
            BuildComment.createFieldComment(bw,"批量新增");
            bw.write("\tpublic Integer addBatch(List<"+tableInfo.getBeanName()+"> listbean) {");
            bw.newLine();
            bw.write("\t\tif(listbean==null || listbean.isEmpty())");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\treturn this."+mapperBeanName+".insertBatch(listbean);");
            bw.newLine();
            bw.write("\t}");

            bw.newLine();
            BuildComment.createFieldComment(bw,"批量新增或修改");
            bw.write("\tpublic Integer addOrUpdateBatch(List<"+tableInfo.getBeanName()+"> listbean) {");
            bw.newLine();
            bw.write("\t\tif(listbean==null || listbean.isEmpty())");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\treturn this."+mapperBeanName+".insertOrUpdateBatch(listbean);");
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

                BuildComment.createFieldComment(bw,"根据"+methodName+"查询");
                bw.write("\tpublic "+tableInfo.getBeanName()+" get"+tableInfo.getBeanName()+"By"+methodName+"("+methodParams+") {");
                bw.newLine();
                bw.write("\t\treturn this."+mapperBeanName+".selectBy"+methodName+"("+paramsBuffer+");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                BuildComment.createFieldComment(bw,"根据"+methodName+"更新");
                bw.write("\tpublic Integer update"+tableInfo.getBeanName()+"By"+methodName+"("+tableInfo.getBeanName()+" bean, "+methodParams+") {");
                bw.newLine();
                bw.write("\t\treturn this."+mapperBeanName+".updateBy"+methodName+"(bean,"+paramsBuffer+");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                BuildComment.createFieldComment(bw,"根据"+methodName+"删除");
                bw.write("\tpublic Integer delete"+tableInfo.getBeanName()+"By"+methodName+"("+methodParams+") {");
                bw.newLine();
                bw.write("\t\treturn this."+mapperBeanName+".deleteBy"+methodName+"("+paramsBuffer+");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
            }
            bw.newLine();
            bw.write("}");
            bw.flush();
        }catch (Exception e){
            logger.error("创建Service Impl失败",e);
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
