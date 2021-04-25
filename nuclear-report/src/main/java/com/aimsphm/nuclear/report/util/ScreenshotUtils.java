package com.aimsphm.nuclear.report.util;

import com.aimsphm.nuclear.common.constant.ReportConstant;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.report.util
 * @Description: <截图工具类>
 * @Author: MILLA
 * @CreateDate: 2020/4/28 9:32
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/28 9:32
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@Slf4j
public class ScreenshotUtils {
    @Resource
    @Qualifier("phantomJSDriver")
    private WebDriver driver;

    @Value("${customer.config.html.delete:true}")
    private Boolean htmlDelete;

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * 直接通过driver截图 不延时
     *
     * @param htmlPath   html文件路径
     * @param outputType 输出类型
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     */
    public <X> X getScreenshotAsByDriver(String htmlPath, OutputType<X> outputType) throws WebDriverException, InterruptedException {
        driver.get(ReportConstant.BROWSER_LOCAL_OPEN_PRE + htmlPath);
        return getScreenshotAs(htmlPath, outputType, null);
    }

    /**
     * 直接通过driver截图 延时
     *
     * @param htmlPath   html文件路径
     * @param outputType 输出类型
     * @param sleepTime  延时时间
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     * @throws InterruptedException
     */
    public <X> X getScreenshotAsByDriver(String htmlPath, OutputType<X> outputType, Long sleepTime) throws WebDriverException, InterruptedException {
        //是否有html
        if (Objects.nonNull(htmlPath) && htmlPath.length() > 0) {
            driver.get(ReportConstant.BROWSER_LOCAL_OPEN_PRE + htmlPath);
        }
        //是否延时处理
        if (Objects.nonNull(sleepTime)) {
            Thread.sleep(sleepTime);
        }
        return ((TakesScreenshot) driver).getScreenshotAs(outputType);
    }

    /**
     * 同步进行截图
     *
     * @param htmlPath   html文件路径
     * @param outputType 输出文件类型
     * @param sleepTime  延时时间
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     * @throws InterruptedException
     */
    public synchronized <X> X getScreenshotAs(String htmlPath, OutputType<X> outputType, Long sleepTime) throws WebDriverException, InterruptedException {
        return getScreenshotAs(htmlPath, null, outputType, sleepTime);
    }

    /**
     * 异步执行截图
     *
     * @param htmlPath   html文件路径
     * @param outputType 输出文件类型
     * @param sleepTime  延时时间
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     * @throws InterruptedException
     */
    public <X> X getScreenshotAsAsynchronous(String htmlPath, OutputType<X> outputType, Long sleepTime) throws WebDriverException, InterruptedException {
        return getScreenshotAs(htmlPath, null, outputType, sleepTime);
    }

    /**
     * @param htmlPath   html文件路径
     * @param tagName    html中tag名称
     * @param outputType 输出类型
     * @param sleepTime  延时时间
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     * @throws InterruptedException
     */
    public <X> X getScreenshotAs(String htmlPath, String tagName, OutputType<X> outputType, Long sleepTime) throws WebDriverException, InterruptedException {
        //是否有html
        boolean hasHtml = Objects.nonNull(htmlPath) && htmlPath.length() > 0;
        try {
            if (hasHtml) {
                driver.get(ReportConstant.BROWSER_LOCAL_OPEN_PRE + htmlPath);
            } else {
                return null;
            }
            //是否延时处理
            if (Objects.nonNull(sleepTime)) {
                Thread.sleep(sleepTime);
            }
            //如果有tagName
            if (Objects.nonNull(tagName) && tagName.length() > 0) {
                return getScreenshotAs(outputType, tagName);
            }
            return getScreenshotAs(outputType, ReportConstant.ECHARTS_CANVAS);
        } finally {
            //截图之后删除图片
            try {
                if (hasHtml && htmlDelete) {
//                    new File(htmlPath).delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param htmlPath   html文件路径
     * @param Id         html中Id名称
     * @param outputType 输出类型
     * @param sleepTime  延时时间
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     * @throws InterruptedException
     */
    public <X> X screenshotById(String htmlPath, String Id, OutputType<X> outputType, Long sleepTime) throws WebDriverException, InterruptedException {
        //是否有html
        boolean hasHtml = Objects.nonNull(htmlPath) && htmlPath.length() > 0;
        try {
            if (hasHtml) {
                driver.get(ReportConstant.BROWSER_LOCAL_OPEN_PRE + htmlPath);
            } else {
                return null;
            }
            //是否延时处理
            if (Objects.nonNull(sleepTime)) {
                Thread.sleep(sleepTime);
            }
            //如果有tagName
            if (Objects.nonNull(Id) && Id.length() > 0) {
                return getScreenshotAsById(outputType, Id);
            }
            return getScreenshotAs(outputType, ReportConstant.ECHARTS_CANVAS);
        } finally {
            //截图之后删除图片
            try {
                if (hasHtml && htmlDelete) {
                    new File(htmlPath).delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param outputType 输出类型
     * @param Id         html中元素名称
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     */
    public <X> X getScreenshotAsById(OutputType<X> outputType, String Id) throws WebDriverException {
        //tag名称为空直接采用driver的截图方式
        if (Objects.isNull(Id) && Id.length() == 0) {
            return ((TakesScreenshot) driver).getScreenshotAs(outputType);
        }
        WebElement element;
        try {
            element = driver.findElement(By.id(Id));
        } catch (Exception exception) {
            log.error("使用id->chart截图失败，{}", exception);
            return ((TakesScreenshot) driver).getScreenshotAs(outputType);
        }
        WebDriver.Window window = driver.manage().window();
        Dimension size = element.getSize();
        window.setSize(new Dimension(size.width, size.getHeight()));
        return element.getScreenshotAs(outputType);
    }

    /**
     * 默认tag不延迟
     *
     * @param htmlPath   html文件路径
     * @param outputType 输出类型
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     * @throws InterruptedException
     */
    public <X> X getScreenshotAs(String htmlPath, OutputType<X> outputType) throws WebDriverException, InterruptedException {
        return getScreenshotAs(htmlPath, outputType, null);
    }

    /**
     * @param outputType 输出类型
     * @param tagName    html中元素名称
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     */
    public <X> X getScreenshotAs(OutputType<X> outputType, String tagName) throws WebDriverException {
        //tag名称为空直接采用driver的截图方式
        if (Objects.isNull(tagName) && tagName.length() == 0) {
            return ((TakesScreenshot) driver).getScreenshotAs(outputType);
        }
        WebElement element;
        try {
            //获取不到指定tag的话使用driver进行截图
            element = driver.findElement(By.tagName(tagName));
        } catch (Exception e) {
            log.error("使用canvas截图失败，{}", e);
            try {
                element = driver.findElement(By.id("chart"));
            } catch (Exception exception) {
                log.error("使用id->chart截图失败，{}", e);
                return ((TakesScreenshot) driver).getScreenshotAs(outputType);
            }
        }
        WebDriver.Window window = driver.manage().window();
        Dimension size = element.getSize();
        window.setSize(new Dimension(size.width, size.getHeight()));
        return element.getScreenshotAs(outputType);
    }
}
