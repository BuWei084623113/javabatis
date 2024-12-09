package com.javabatis.builder;

import com.javabatis.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildBase {
    public static Logger logger = LoggerFactory.getLogger(BuildBase.class);
    public static void execute(){
        List<String> headInfoList = new ArrayList<>();

//        生成日期枚举
        headInfoList.add("package "+Constants.PACKAGE_ENUM);
        build(headInfoList,"DateTimePatternEnum", Constants.PATH_ENUM);

        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_UTILS);
        build(headInfoList,"DateUtils", Constants.PATH_UTILS);

        //生成baseMapper
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_MAPPER);
        build(headInfoList,"BaseMapper", Constants.PATH_MAPPER);

//        生成PageSize枚举
        headInfoList.clear();
            headInfoList.add("package "+Constants.PACKAGE_ENUM);
        build(headInfoList,"PageSize", Constants.PATH_ENUM);

//        生成分页信息
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_QUERY);
        headInfoList.add("import "+Constants.PACKAGE_ENUM+".PageSize");
        build(headInfoList,"SimplePage", Constants.PATH_QUERY);

        //        生成BaseParam
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_QUERY);
        build(headInfoList,"BaseParam", Constants.PATH_QUERY);

//        生成PaginationResultVO
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_VO);
        build(headInfoList,"PaginationResultVO", Constants.PATH_VO);

        //        生成EXCEPTION
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_EXCEPTION);
        headInfoList.add("import "+Constants.PACKAGE_ENUM+".ResponseCodeEnum;");
        build(headInfoList,"BusinessException", Constants.PATH_EXCEPTION);

        //        生成ResponseCodeEnum
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_ENUM);
        build(headInfoList,"ResponseCodeEnum", Constants.PATH_ENUM);

        //        生成ResponseVO
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_VO);
        build(headInfoList,"ResponseVO", Constants.PATH_VO);

        //        生成CONTROLLER
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_CONTROLLER);
        headInfoList.add("import "+Constants.PACKAGE_VO+".ResponseVO");
        headInfoList.add("import "+Constants.PACKAGE_ENUM+".ResponseCodeEnum");
        build(headInfoList,"ABaseController", Constants.PATH_CONTROLLER);

        //        生成AGlobalExceptionHandlerController
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_CONTROLLER);
        headInfoList.add("import "+Constants.PACKAGE_VO+".ResponseVO");
        headInfoList.add("import "+Constants.PACKAGE_ENUM+".ResponseCodeEnum");
        headInfoList.add("import "+Constants.PACKAGE_EXCEPTION+".BusinessException");
        build(headInfoList,"AGlobalExceptionHandlerController", Constants.PATH_CONTROLLER);
    }

    private static void build(List<String> headInfoList, String fileName, String outPath){
        File folder = new File(outPath);
        if(!folder.exists()){
            folder.mkdirs();
        }

        File javaFile = new File(outPath,fileName+".java");

        OutputStream outputStream = null;
        OutputStreamWriter outw=null;
        BufferedWriter bw = null;

        InputStream inputStream = null;
        InputStreamReader inr = null;
        BufferedReader bf = null;
        try{
            outputStream = new FileOutputStream(javaFile);
            outw = new OutputStreamWriter(outputStream,"utf-8");
            bw = new BufferedWriter(outw);

            String tempPath = BuildBase.class.getClassLoader().getResource("template/"+fileName+".txt").getPath();
            inputStream = new FileInputStream(tempPath);
            inr = new InputStreamReader(inputStream,"utf-8");
            bf = new BufferedReader(inr);

            for(String head :headInfoList){
                bw.write(head+";");
                bw.newLine();
                if(head.contains("package"))
                    bw.newLine();
            }
            bw.flush();

            String lineinfo = null;
            while((lineinfo= bf.readLine())!=null){
                bw.write(lineinfo);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            logger.error("Failed to generate basic class {}",fileName,e);
        }finally {
            if(bf!=null){
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inr!=null){
                try {
                    inr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outw != null){
                try {
                    outw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
