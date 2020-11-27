package com.study.auth.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @Package: com.milla.navicat.comm
 * @Description: <读取配置properties工具类>
 * @Author: MILLA
 * @CreateDate: 2018/8/10 10:30
 * @UpdateUser: MILLA
 * @UpdateDate: 2018/8/10 10:30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class PropertiesReaderUtil {
    private static final String ENCODING = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(PropertiesReaderUtil.class);
    private static Properties propsZH;
    private static Properties propsCN;
    private static String name = null;

    static {
        //加载英文
        //loadProps(false);
        //加载中文
        loadProps(true);
    }

    /**
     * 第一种，通过类加载器进行获取properties文件流
     * 第二种，通过类进行获取properties文件流
     * in = PropertyUtil.class.getResourceAsStream("/properties/message_ZH.properties");
     * in = PropertiesReaderUtil.class.getClassLoader().getResourceAsStream("properties/message_ZH.properties");
     */
    synchronized static private void loadProps(boolean isZh) {
        System.out.println("--------------");
        logger.debug("start loading properties");
        InputStream in = null;
        if (isZh) {
            propsZH = new Properties();
            name = "properties/message_ZH.properties";
            in = PropertiesReaderUtil.class.getClassLoader().getResourceAsStream(name);
        } else {
            propsCN = new Properties();
            name = "properties/message_EN.properties";
            in = PropertiesReaderUtil.class.getClassLoader().getResourceAsStream(name);
        }
        try {
            if (isZh) {
                propsZH.load(new InputStreamReader(in, ENCODING));
            } else {
                propsCN.load(new InputStreamReader(in, ENCODING));
            }
        } catch (Exception e) {
            logger.debug("loading properties error :{}", e);
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.debug("closing properties io error :{}", e);
            }
        }
    }

    public static String getProperty(String key) {
        return getPropertyZH(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return getPropertyZH(key, defaultValue);
    }

    public static String getPropertyZH(String key) {
        if (null == propsZH) {
            loadProps(true);
        }
        return propsZH.getProperty(key);
    }

    public static String getPropertyZH(String key, String defaultValue) {
        if (null == propsZH) {
            loadProps(true);
        }
        return propsZH.getProperty(key, defaultValue);
    }

    public static String getPropertyCN(String key) {
        if (null == propsCN) {
            loadProps(false);
        }
        return propsCN.getProperty(key);
    }

    public static String getPropertyCN(String key, String defaultValue) {
        if (null == propsCN) {
            loadProps(false);
        }
        return propsCN.getProperty(key, defaultValue);
    }

    public static void main(String[] args) {
        String property = PropertiesReaderUtil.getProperty("1001");
        System.out.println(property);
        String property1 = PropertiesReaderUtil.getProperty("1001");
        System.out.println(property1);
    }

}
