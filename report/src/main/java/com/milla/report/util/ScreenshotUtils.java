package com.milla.report.util;

import com.milla.report.constant.ReportConstant;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
public class ScreenshotUtils {
    @Autowired
    @Qualifier("phantomJSDriver")
    private WebDriver driver;

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
     * @param htmlPath   html文件路径
     * @param outputType 输出文件类型
     * @param sleepTime  延时时间
     * @param <X>        具体类型
     * @return
     * @throws WebDriverException
     * @throws InterruptedException
     */
    public <X> X getScreenshotAs(String htmlPath, OutputType<X> outputType, Long sleepTime) throws WebDriverException, InterruptedException {
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
        if (Objects.nonNull(htmlPath) && htmlPath.length() > 0) {
            driver.get(ReportConstant.BROWSER_LOCAL_OPEN_PRE + htmlPath);
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
            return ((TakesScreenshot) driver).getScreenshotAs(outputType);
        }
        WebDriver.Window window = driver.manage().window();
        Dimension size = element.getSize();
        window.setSize(new Dimension(size.width, size.getHeight()));
        return element.getScreenshotAs(outputType);
    }
}
