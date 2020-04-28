package com.milla.report.enumeration;

/**
 * @Package: com.aimsphm.nuclear.report.enumeration
 * @Description: <驱动枚举类[可设置多版本]>
 * @Author: MILLA
 * @CreateDate: 2020/4/27 18:15
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/27 18:15
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum DriverEnum {
    ChromeDriver("webdriver.chrome.driver", "chromedriver-win32.exe", "chromedriver-mac", "chromedriver-linux64"),//谷歌驱动
    FirefoxDriver("webdriver.gecko.driver", "geckodriver-win64.exe", "geckodriver-mac", "geckodriver-linux64"),//火狐
    PhantomJSDriver("phantomjs.binary.path", "phantomjs-win.exe", "phantomjs-mac", "phantomjs-linux");

    DriverEnum(String binPath, String driverNameWin, String driverNameMac, String driverNameLinux) {
        this.binPath = binPath;
        this.driverNameWin = driverNameWin;
        this.driverNameMac = driverNameMac;
        this.driverNameLinux = driverNameLinux;
    }

    //驱动名称
    private String binPath;
    //window名称
    private String driverNameWin;
    //mac 名称
    private String driverNameMac;
    //linux名称
    private String driverNameLinux;

    public String getBinPath() {
        return binPath;
    }

    public String getDriverNameWin() {
        return driverNameWin;
    }

    public String getDriverNameMac() {
        return driverNameMac;
    }

    public String getDriverNameLinux() {
        return driverNameLinux;
    }
}
