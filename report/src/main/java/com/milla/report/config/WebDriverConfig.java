package com.milla.report.config;

import com.milla.report.constant.ReportConstant;
import com.milla.report.enumeration.DriverEnum;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;

/**
 * @Package: com.aimsphm.nuclear.report.config
 * @Description: <浏览器驱动配置类>
 * @Author: MILLA
 * @CreateDate: 2020/4/26 13:53
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/26 13:53
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Configuration
public class WebDriverConfig {
    @Bean("phantomJSDriver")
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

    //    @Bean("chromeDriver")//目前linux执行存在问题
    public WebDriver chromeDriver() throws IOException {
        initLoadByOsName(DriverEnum.ChromeDriver);
        ChromeOptions options = new ChromeOptions();
        //设置 chrome 的无头模式
        options.setHeadless(Boolean.TRUE);
        //启动一个 chrome 实例
        return new ChromeDriver(options);
    }

    //    @Bean("firefoxDriver")//目前linux执行存在问题
    public WebDriver firefoxDriver() throws IOException {
        initLoadByOsName(DriverEnum.FirefoxDriver);
        FirefoxOptions options = new FirefoxOptions();
        //设置 chrome 的无头模式
        options.setHeadless(Boolean.TRUE);
        //启动一个 chrome 实例
        return new FirefoxDriver(options);
    }

    /**
     * 根据系统名称进行初始化
     *
     * @param driver
     * @throws IOException
     */
    private void initLoadByOsName(DriverEnum driver) throws IOException {
        ClassLoader classLoader = WebDriverConfig.class.getClassLoader();
        //获取操作系统的名字
        String osName = System.getProperty(ReportConstant.SYSTEM_CONSTANT_OS_NAME, ReportConstant.BLANK);
        if (osName.startsWith(ReportConstant.OS_NAME_PRE_MAC)) {//苹果的打开方式
            File file = decompressionDriver2TempPath(classLoader, driver.getDriverNameMac(), false);
            System.setProperty(driver.getBinPath(), file.getAbsolutePath());
        } else if (osName.startsWith(ReportConstant.OS_NAME_PRE_WINDOWS)) {//windows的打开方式
            File file = decompressionDriver2TempPath(classLoader, driver.getDriverNameWin(), false);
            System.setProperty(driver.getBinPath(), file.getAbsolutePath());
        } else {//unix,linux
            File file = decompressionDriver2TempPath(classLoader, driver.getDriverNameLinux(), true);
            System.setProperty(driver.getBinPath(), file.getAbsolutePath());


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
        InputStream inputStream = resource.openStream();
        File file = new File(ReportConstant.SYSTEM_CONSTANT_OS_TEMP_DIR + File.separator + driverName);
        if (!file.exists()) {
            FileUtils.copyInputStreamToFile(inputStream, file);
            if (isCmd) {//将文件变成可执行状态
                Runtime r = Runtime.getRuntime();
                r.exec(ReportConstant.LINUX_EXECUTABLE_CMD_PRE + file.getAbsolutePath());
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
            File file = decompressionDriver2TempPath(classLoader, DriverEnum.PhantomJSDriver.getDriverNameMac(), false);
            phantomJSPath = file.getAbsolutePath();
        } else if (osName.startsWith(ReportConstant.OS_NAME_PRE_WINDOWS)) {//windows的打开方式
            File file = decompressionDriver2TempPath(classLoader, DriverEnum.PhantomJSDriver.getDriverNameWin(), false);
            phantomJSPath = file.getAbsolutePath();
        } else {//unix,linux
            File file = decompressionDriver2TempPath(classLoader, DriverEnum.PhantomJSDriver.getDriverNameLinux(), true);
            phantomJSPath = file.getAbsolutePath();
        }
        options.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJSPath);
    }
}