package com.mmall.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties props;
    //静态块在类加载时被执行，且只执行一次，优先级高,不能有修饰符
    static{
        String filename="mmall.properties";
        props =new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(filename),"UTF-8"));
        } catch (IOException e) {
           logger.error("配置文件读取异常",e);
        }
    }
    public static String getProperty(String key){
        String value=props.getProperty(key.trim());
        if(value==null){
            return null;
        }
        return value.trim();
    }
    public static String getProperty(String key,String defaultVlue){
        String value=props.getProperty(key.trim());
        if(value==null){
            value=defaultVlue;
        }
        return value.trim();
    }
}
