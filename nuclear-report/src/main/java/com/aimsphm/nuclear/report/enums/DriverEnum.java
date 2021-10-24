package com.aimsphm.nuclear.report.enums;

/**
 * <p>
 * 功能描述:驱动枚举类[可设置多版本]
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/27 18:15
 */
public enum DriverEnum {


    /**
     * 谷歌驱动
     */
    CHROME_DRIVER("webdriver.chrome.driver", "chromedriver-win32.exe", "chromedriver-mac", "chromedriver-linux64"),

    /**
     * 火狐
     */
    FIREFOX_DRIVER("webdriver.gecko.driver", "geckodriver-win64.exe", "geckodriver-mac", "geckodriver-linux64"),

    /**
     * phantomjs驱动
     */
    PHANTOM_JS_DRIVER("phantomjs.binary.path", "phantomjs-win.exe", "phantomjs-mac", "phantomjs-linux");

    DriverEnum(String binPath, String driverNameWin, String driverNameMac, String driverNameLinux) {
        this.binPath = binPath;
        this.driverNameWin = driverNameWin;
        this.driverNameMac = driverNameMac;
        this.driverNameLinux = driverNameLinux;
    }

    /**
     * 驱动名称
     */
    private String binPath;
    /**
     * window名称
     */
    private String driverNameWin;
    /**
     * mac 名称
     */
    private String driverNameMac;
    /**
     * linux名称
     */
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
