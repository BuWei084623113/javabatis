package com.javabatis.utils;

/**
 * 调用application.properties中的配置，应用全局
 * 更新时间：2024/11/20
 * */
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtils {
    private static Properties prop = new Properties();
    private static Map<String,String> PROPER_MAP = new ConcurrentHashMap<>();
    static{
        InputStream is = null;
        try{
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(new InputStreamReader(is,"gbk"));

            Iterator<Object> iterator = prop.keySet().iterator();

            while(iterator.hasNext()){
                String key = (String)iterator.next();
                PROPER_MAP.put(key,prop.getProperty(key));
            }
        }catch(Exception e){

        }finally {
            if(is!=null){
                try{
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getString(String key){
        return PROPER_MAP.get(key);
    }

//    public static void main(String[] args){
//        System.out.println(getString("db.driver.name"));
//    }
}
