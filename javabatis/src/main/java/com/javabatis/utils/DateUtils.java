package com.javabatis.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String _YYYYMMDD = "yyyy/MM/dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static String format(Date date,String patten){
        return new SimpleDateFormat(patten).format(date);
    }

    public static String parse(String date,String patten){
        try {
            new SimpleDateFormat(patten).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
