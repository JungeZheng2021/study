package com.aimsphm.nuclear.common.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @Package: com.aimsphm.nuclear.common.response
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
    private static Properties propsZh = new Properties();
    private static Properties propsEn = new Properties();

    static {
        //加载英文
        loadProps(propsEn, "properties/message_en_US.properties");
        //加载中文
        loadProps(propsZh, "properties/message_zh_CN.properties");
    }

    /**
     * 第一种，通过类加载器进行获取properties文件流
     * 第二种，通过类进行获取properties文件流
     * in = PropertyUtil.class.getResourceAsStream("/properties/message_en_US.properties");
     * in = PropertiesReaderUtil.class.getClassLoader().getResourceAsStream("properties/message_en_US.properties");
     */
    synchronized static private void loadProps(Properties properties, String fileName) {
        logger.debug("start loading properties");
        InputStream in = null;
        try {
            in = PropertiesReaderUtil.class.getClassLoader().getResourceAsStream(fileName);
            properties.load(new InputStreamReader(in, ENCODING));
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
        return getPropertyZh(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return getPropertyZh(key, defaultValue);
    }

    public static String getPropertyZh(String key) {
        return propsZh.getProperty(key);
    }

    public static String getPropertyZh(String key, String defaultValue) {
        return propsZh.getProperty(key, defaultValue);
    }

    public static String getPropertyEn(String key) {
        return propsEn.getProperty(key);
    }

    public static String getPropertyEn(String key, String defaultValue) {
        return propsEn.getProperty(key, defaultValue);
    }
}
