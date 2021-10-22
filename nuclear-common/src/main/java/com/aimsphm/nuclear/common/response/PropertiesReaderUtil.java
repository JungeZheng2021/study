package com.aimsphm.nuclear.common.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * <p>
 * 功能描述:读取配置properties工具类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2018/8/10 10:30
 */
public final class PropertiesReaderUtil {
    private static final String ENCODING = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(PropertiesReaderUtil.class);
    private static final Properties PROPS_ZH = new Properties();
    private static final Properties PROPS_EN = new Properties();

    private PropertiesReaderUtil() {
    }

    static {
        //加载英文
        loadProps(PROPS_EN, "properties/message_en_US.properties");
        //加载中文
        loadProps(PROPS_ZH, "properties/message_zh_CN.properties");
    }

    /**
     * 第一种，通过类加载器进行获取properties文件流
     * 第二种，通过类进行获取properties文件流
     * in = PropertyUtil.class.getResourceAsStream("/properties/message_en_US.properties");
     * in = PropertiesReaderUtil.class.getClassLoader().getResourceAsStream("properties/message_en_US.properties");
     */
    private synchronized static void loadProps(Properties properties, String fileName) {
        logger.debug("start loading properties");
        try (InputStream in = PropertiesReaderUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(new InputStreamReader(in, ENCODING));
        } catch (Exception e) {
            logger.debug("loading properties error :{}", e);
        }
    }

    public static String getProperty(String key) {
        return getPropertyZh(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return getPropertyZh(key, defaultValue);
    }

    public static String getPropertyZh(String key) {
        return PROPS_ZH.getProperty(key);
    }

    public static String getPropertyZh(String key, String defaultValue) {
        return PROPS_ZH.getProperty(key, defaultValue);
    }

    public static String getPropertyEn(String key) {
        return PROPS_EN.getProperty(key);
    }

    public static String getPropertyEn(String key, String defaultValue) {
        return PROPS_EN.getProperty(key, defaultValue);
    }
}
