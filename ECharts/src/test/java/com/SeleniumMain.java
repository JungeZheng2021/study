package com;

import com.github.abel533.echarts.samples.pie.PieTest6;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Package: com.milla.selenium
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/25 15:29
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/25 15:29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class SeleniumMain {
    public static void main(String[] args) throws IOException {

        String html = PieTest6.getHtml(true);
        InputStream resourceAsStream = SeleniumMain.class.getResourceAsStream("/echarts-all-3.js");
        String tempPath = html.substring(0, html.lastIndexOf(File.separator));
        File file = new File(tempPath + "\\echarts-all-3.js");
        if (!file.exists()) {
            FileUtils.copyInputStreamToFile(resourceAsStream, file);
        }
////        System.out.println(html);
////        System.out.println(html.substring(0, html.lastIndexOf(File.separator)));
//        testChrome(html);
    }

    private static void testChrome(String html) {
        WebDriver driver = null;
        try {
            long currentTimeMillis = System.currentTimeMillis();
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\milla\\IdeaProjects\\study\\ECharts\\src\\main\\java\\com\\milla\\selenium\\80\\chromedriver.exe");
//        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "");
            ChromeOptions options = new ChromeOptions();
            //设置 chrome 的无头模式

//            options.setHeadless(Boolean.TRUE);
            //启动一个 chrome 实例
            driver = new ChromeDriver(options);

//            while (true) {
            driver.get("file:///" + html);
            Thread.sleep(10L);
            WebElement canvas = driver.findElement(By.tagName("canvas"));
            File srcFile = canvas.getScreenshotAs(OutputType.FILE);
//                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File("C:\\Users\\milla\\Desktop\\test\\" + System.currentTimeMillis() + ".png"));
            System.out.println("共计用时：" + (System.currentTimeMillis() - currentTimeMillis));
            currentTimeMillis = System.currentTimeMillis();
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            driver.close();
        }
    }
}
