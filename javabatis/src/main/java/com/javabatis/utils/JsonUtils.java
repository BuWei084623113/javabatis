package com.javabatis.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {
    public static String convertObj2Json(Object object){
        if(null==object)
            return null;
        return JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
    }
}
