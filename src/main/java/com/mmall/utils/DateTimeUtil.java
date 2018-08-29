package com.mmall.utils;


import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {
    //joda-time

    //time->str
    //str->time
    private static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static Date str2Date(String dateTimeStr, String formatStr){
        DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(formatStr);
        DateTime dateTime=dateTimeFormatter.parseDateTime(dateTimeStr);

        return dateTime.toDate();
    }

    public static String date2Str(Date date,String formatStr){
        if(date==null){
            return "";
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(formatStr);
    }
    public static Date str2Date(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime=dateTimeFormatter.parseDateTime(dateTimeStr);

        return dateTime.toDate();
    }

    public static String date2Str(Date date){
        if(date==null){
            return "";
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }


//    public static void main(String[] args) {
//        System.out.println(DateTimeUtil.date2Str(new Date(),"yyyy-MM--dd HH:mm:ss"));
//        System.out.println(DateTimeUtil.str2Date("2018-12-12 12:12:21","yyyy-MM-dd HH:mm:ss"));
//    }
}
