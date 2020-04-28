package com.milla.report.constant;

import java.io.File;

/**
 * @Package: com.aimsphm.nuclear.report.constant
 * @Description: <常量类>
 * @Author: MILLA
 * @CreateDate: 2020/4/27 18:09
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/27 18:09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class ReportConstant {
    /**
     * Window系统前缀
     */
    public static final String OS_NAME_PRE_WINDOWS = "Windows";
    /**
     * Mac系统前缀
     */
    public static final String OS_NAME_PRE_MAC = "Mac OS";

    /**
     * Linux系统前缀
     */
    public static final String OS_NAME_PRE_LINUX = "Linux";
    /**
     * 变量中系统的key
     */
    public static final String SYSTEM_CONSTANT_OS_NAME = "os.name";
    /**
     * 驱动运行临时目录
     */
    public static final String SYSTEM_CONSTANT_OS_TEMP_DIR = File.separator + "usr" + File.separator + "share" + File.separator + "locale";
    /**
     * 驱动在项目中的根路径
     */
    public static final String PROJECT_DRIVER_ROOT_DIR = File.separator + "driver" + File.separator;
    /**
     * linux将文件变成可执行文件命令前缀
     */
    public static final String LINUX_EXECUTABLE_CMD_PRE = "chmod +x ";

    /**
     * 空字符串
     */
    public static final String BLANK = "";
    /**
     * 本地浏览器打开文件前缀
     */
    public static final String BROWSER_LOCAL_OPEN_PRE = "file:///";

    /**
     *echarts 图片tag名称
     */
    public static final String ECHARTS_CANVAS = "canvas";
}
