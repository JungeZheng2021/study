package com.aimsphm.nuclear.report.config;

import com.aimsphm.nuclear.common.constant.ReportConstant;
import com.aimsphm.nuclear.report.enums.DriverEnum;
import com.aimsphm.nuclear.report.util.ScreenshotUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;

/**
 * <p>
 * 功能描述:浏览器驱动配置类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/26 13:53
 */
@Slf4j
@Configuration
public class WebDriverConfig {
    public static void main(String[] args) throws IOException, InterruptedException {
        WebDriverConfig config = new WebDriverConfig();
        WebDriver webDriver = config.phantomJSDriver();
        ScreenshotUtils utils = new ScreenshotUtils();
        utils.setDriver(webDriver);
    }

    @Bean("phantomJSDriver")
    @Scope("prototype")
    public WebDriver phantomJSDriver() throws IOException {
//          //设置必要参数
        DesiredCapabilities options = new DesiredCapabilities();
        //ssl证书支持
        options.setCapability("acceptSslCerts", true);
        //截屏支持
        options.setCapability("takesScreenshot", true);
        //css搜索支持
        options.setCapability("cssSelectorsEnabled", true);
        //js支持
        options.setJavascriptEnabled(true);
        //驱动支持
        initPhantomJSEnvironment(options);
        //创建无界面浏览器对象
        PhantomJSDriver driver = new PhantomJSDriver(options);
//        driver.setLogLevel(Level.OFF);//关闭日志
        driver.manage().window().maximize();
        driver.setLogLevel(Level.ALL);
        return driver;
    }


    private void decompressionEChartsJs2TempPath(ClassLoader classLoader, String eChartsJsName) throws IOException {
        InputStream inputStream = null;
        try {
            //获取临时目录
            URL resource = classLoader.getResource(ReportConstant.PROJECT_STATIC_ROOT_DIR + eChartsJsName);
            //上线要去除

            if (resource == null) {
                inputStream = new FileInputStream("D:\\Java\\workspace\\nuclear_power\\nuclear-report\\src\\main\\resources\\static\\" + eChartsJsName);
            } else {
                inputStream = resource.openStream();
            }
            File file = new File(ReportConstant.ECHARTS_TEMP_DIR + eChartsJsName);
            if (!file.exists()) {
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
            File docTemp = new File(ReportConstant.DOC_TEMP_DIR_PRE);
            if (!docTemp.exists()) {
                docTemp.mkdirs();//创建文档目录
            }
        } catch (IOException e) {
            log.error("", e);
        } finally {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
        }
    }

    /**
     * 解压文件到指定的工作路径
     *
     * @param classLoader 类加载器
     * @param driverName  需要运行的文件名称
     * @param isCmd       是否将文件设置程可执行文件
     * @return
     * @throws IOException
     */
    private File decompressionDriver2TempPath(ClassLoader classLoader, String driverName, boolean isCmd) throws IOException {
        //获取临时目录
        URL resource = classLoader.getResource(ReportConstant.PROJECT_DRIVER_ROOT_DIR + driverName);
        File file = null;
        InputStream inputStream = null;
        try {
            file = new File(ReportConstant.SYSTEM_CONSTANT_OS_TEMP_DIR + File.separator + driverName);
            //上线要去除
            if (resource == null) {
                inputStream = new FileInputStream("D:\\Java\\workspace\\nuclear_phm\\nuclear-report\\src\\main\\resources\\driver\\" + driverName);
            } else {
                inputStream = resource.openStream();
            }
            if (!file.exists()) {
                FileUtils.copyInputStreamToFile(inputStream, file);
                //将文件变成可执行状态
                if (isCmd) {
                    Runtime r = Runtime.getRuntime();
                    r.exec(ReportConstant.LINUX_EXECUTABLE_CMD_PRE + file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            log.error("error");
        } finally {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
        }
        return file;
    }

    /**
     * 初始化运行环境
     *
     * @param options 配置项
     * @throws IOException
     */
    private void initPhantomJSEnvironment(DesiredCapabilities options) throws IOException {
        ClassLoader classLoader = WebDriverConfig.class.getClassLoader();
        String phantomJSPath;
        //获取操作系统的名字
        String osName = System.getProperty(ReportConstant.SYSTEM_CONSTANT_OS_NAME, ReportConstant.BLANK);
        if (osName.startsWith(ReportConstant.OS_NAME_PRE_MAC)) {
            File file = decompressionDriver2TempPath(classLoader, DriverEnum.PHANTOM_JS_DRIVER.getDriverNameMac(), false);
            phantomJSPath = file.getAbsolutePath();
            //windows的打开方式
        } else if (osName.startsWith(ReportConstant.OS_NAME_PRE_WINDOWS)) {
            File file = decompressionDriver2TempPath(classLoader, DriverEnum.PHANTOM_JS_DRIVER.getDriverNameWin(), false);
            phantomJSPath = file.getAbsolutePath();
        } else {//unix,linux
            File file = decompressionDriver2TempPath(classLoader, DriverEnum.PHANTOM_JS_DRIVER.getDriverNameLinux(), true);
            phantomJSPath = file.getAbsolutePath();
        }
        options.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJSPath);
        //没有网络的情况下需要将js拷贝到指定的路径下
        decompressionEChartsJs2TempPath(classLoader, ReportConstant.ECHARTS_JS_NAME);
    }
}