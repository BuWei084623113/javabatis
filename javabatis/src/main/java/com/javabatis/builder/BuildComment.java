package com.javabatis.builder;

import com.javabatis.bean.Constants;
import com.javabatis.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

/**
 * 注解类
 * */
public class BuildComment {
    public static void createClassComment(BufferedWriter bw,String classcoment) throws IOException {
        /**
         * 基本结构：
         * @Description:
         * @date:
         * */
        bw.write("/**");
        bw.newLine();
        bw.write(" * @Description:"+classcoment);
        bw.newLine();
        bw.write(" * @date:"+ DateUtils.format(new Date(),DateUtils._YYYYMMDD));
        bw.newLine();
        bw.write(" * @author:"+ Constants.AUTHOR_COMMENT);
        bw.newLine();
        bw.write(" **/");
        bw.newLine();
    }
    public static void createFieldComment(BufferedWriter bw,String filedcoment) throws IOException {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * "+filedcoment);
        bw.newLine();
        bw.write("\t */");
        bw.newLine();
    }
    public static void createMethodComment(){

    }
}
