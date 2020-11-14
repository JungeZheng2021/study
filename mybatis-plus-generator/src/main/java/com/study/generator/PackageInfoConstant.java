package com.study.generator;

import java.io.File;

/**
 * @Package: com.study.generator
 * @Description: <自定义生成的文件路径和包路径>
 * @Author: milla
 * @CreateDate: 2020/11/12 17:47
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/12 17:47
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class PackageInfoConstant {
    //------------------------------可能需要修改的配置---------------------------------------------

    /**
     * ------------------------------controller层单独配置--------------------------
     */
    public static String CONTROLLER_OUTPUT_ROOT_PATH = "/com/study/" ;
    /**
     * controller根目录
     */
    public static String ROOT_PROJECT_CONTROLLER_PATH = "/core" ;
    /**
     * 包路径(基础包路径)
     */
    public static String PACKAGE_ROOT = "com.study" ;
    /**
     * Controller模块(笔者的controller和service,dao,entity不在同一个项目中)
     */
    public static String CONTROLLER_MODULE_NAME = "core" ;

    /**
     * controller的包路径
     */
    public static String PACKAGE_CONTROLLER = PACKAGE_ROOT + "." + CONTROLLER_MODULE_NAME + ".controller" ;

    /**
     * ------------------------------controller以外的其他配置--------------------------
     */
    /**
     * 根路径
     */
    public static String ROOT_PROJECT_PATH = "/common" ;
    /**
     * 输出基础包
     */
    public static String OUTPUT_ROOT_PATH = "/com/study/" ;
    /**
     * 配置你包路径下的子模块(例如：com.study.common)
     * (例如：com.study.ext)
     */
    public static String MODULE_NAME = "common" ;
    /**
     * 扩展模块
     */
    public static String EXT_MODULE_NAME = "ext" ;

    public static String PACKAGE_PARENT = PACKAGE_ROOT + "." + MODULE_NAME;
    /**
     * 扩展基础包路径
     */
    public static String PACKAGE_PARENT_EXT = PACKAGE_ROOT + "." + EXT_MODULE_NAME;

    //---------------------------------------------------------------------------
    /**
     * 扩展的实体名称
     */
    public static String EXT_ClASS_NAME = "Ext" ;
    /**
     * 扩展实现类名称
     */
    public static String EXT_SERVICE_CLASSNAME_IMPL = "ExtImpl" ;

    /**
     * 生成文件路径、Dao、XML、Service、Controller
     * 父包名路径(文件输出路径,也是导包的路径)
     */
    public static String ENTITY_PATH = "/entity/" ;
    public static String MAPPER_PATH = "/mapper/" ;
    public static String XML_PATH = "/resources/mapper/" ;
    public static String XML_PATH_EXT = "/resources/mapper/ext/" ;
    public static String SERVICE_PATH = "/service/" ;
    public static String SERVICE_IMPL_PATH = "/service/impl/" ;

    public static String CONTROLLER_PATH = "/controller/" ;

    public static String MAIN_ROOT = "/src/main/" ;
    public static String JAVA_ROOT = MAIN_ROOT + "/java/" ;


    /**
     * 包路径中的mapper,service,impl等常量
     */
    public static String PACKAGE_NAME_MAPPER = "mapper" ;
    public static String PACKAGE_NAME_SERVICE = "service" ;
    public static String PACKAGE_NAME_IMPL = "impl" ;
    /**
     * mapper.xml输出模块路径(需要注意放置的位置:默认从模块/src/main下开始)
     */
    public static String XML_OUTPUT_MODULE = File.separator + MODULE_NAME;
    public static String XML_OUTPUT_PATH = ROOT_PROJECT_PATH + MAIN_ROOT + XML_PATH;
    /**
     * IService.java, serviceImpl.java输出模块路径
     */
    public static String SERVICE_OUTPUT_MODULE = File.separator + MODULE_NAME;
    public static String SERVICE_OUTPUT_PATH = ROOT_PROJECT_PATH + JAVA_ROOT + OUTPUT_ROOT_PATH + SERVICE_OUTPUT_MODULE + SERVICE_PATH;
    public static String SERVICE_IMPL_OUTPUT_PATH = ROOT_PROJECT_PATH + JAVA_ROOT + OUTPUT_ROOT_PATH + SERVICE_OUTPUT_MODULE + SERVICE_IMPL_PATH;
    /**
     * Entity.java, Mapper.java, Mapper.xml输出模块路径
     */
    public static String DAO_OUTPUT_MODULE = File.separator + MODULE_NAME;
    public static String ENTITY_OUTPUT_PATH = ROOT_PROJECT_PATH + JAVA_ROOT + OUTPUT_ROOT_PATH + DAO_OUTPUT_MODULE + ENTITY_PATH;
    public static String MAPPER_OUTPUT_PATH = ROOT_PROJECT_PATH + JAVA_ROOT + OUTPUT_ROOT_PATH + DAO_OUTPUT_MODULE + MAPPER_PATH;
    /**
     * ----------------------------------------EXT_CONFIG----------------------------------------------------
     */
    /**
     * mapper.xml输出模块路径(需要注意放置的位置:默认从模块/src/main下开始)
     */
    public static String XML_OUTPUT_PATH_EXT = ROOT_PROJECT_PATH + MAIN_ROOT + XML_PATH_EXT;
    /**
     * IService.java, serviceImpl.java输出模块路径
     */
    public static String SERVICE_OUTPUT_MODULE_EXT = File.separator + EXT_MODULE_NAME;
    public static String SERVICE_OUTPUT_PATH_EXT = ROOT_PROJECT_PATH + JAVA_ROOT + OUTPUT_ROOT_PATH + SERVICE_OUTPUT_MODULE_EXT + SERVICE_PATH;
    public static String SERVICE_IMPL_OUTPUT_PATH_EXT = ROOT_PROJECT_PATH + JAVA_ROOT + OUTPUT_ROOT_PATH + SERVICE_OUTPUT_MODULE_EXT + SERVICE_IMPL_PATH;
    /**
     * Entity.java, Mapper.java, Mapper.xml输出模块路径
     */
    public static String DAO_OUTPUT_MODULE_EXT = File.separator + EXT_MODULE_NAME;
    public static String MAPPER_OUTPUT_PATH_EXT = ROOT_PROJECT_PATH + JAVA_ROOT + OUTPUT_ROOT_PATH + DAO_OUTPUT_MODULE_EXT + MAPPER_PATH;

    /**
     * ----------------------------------------EXT_CONFIG----------------------------------------------------
     */

    /**
     * Controller.java输出模块路径
     */
    public static String CONTROLLER_OUTPUT_MODULE = File.separator + CONTROLLER_MODULE_NAME;
    public static String CONTROLLER_OUTPUT_PATH = ROOT_PROJECT_CONTROLLER_PATH + JAVA_ROOT + CONTROLLER_OUTPUT_ROOT_PATH + CONTROLLER_OUTPUT_MODULE + CONTROLLER_PATH;
}

