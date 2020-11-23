package com.aimsphm.nuclear.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.*;

import static com.aimsphm.nuclear.generator.BasicConstant.*;
import static com.aimsphm.nuclear.generator.PackageInfoConstant.*;
import static com.aimsphm.nuclear.generator.TemplateConstant.*;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.SLASH;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.UNDERSCORE;

/**
 * @Package: com.aimsphm.nuclear.generator
 * @Description: <自动代码生成类>
 * @Author: milla
 * @CreateDate: 2020/11/12 17:29
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/12 17:29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@SpringBootApplication
public class GeneratorApplication {

    public static void main(String[] args) {
        builder();
    }

    /**
     * 执行自动代码生成程序
     */
    private static void builder() {
        builder(false);
    }

    /**
     * 执行自动代码生成程序
     *
     * @param isCreateExt
     */
    private static void builder(boolean isCreateExt) {
        new AutoGenerator().setGlobalConfig(globalConfig())
                .setDataSource(dataSourceConfig())
                .setStrategy(strategyConfig())
                .setCfg(injectionConfig(isCreateExt))
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(PACKAGE_PARENT)
                )
                .execute();
    }


    /**
     * 设置表格/字段等生成策略配置
     *
     * @return
     */
    private static StrategyConfig strategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig()
                // 表名生成策略：下划线连转驼峰
                .setNaming(NamingStrategy.underline_to_camel)
                // 表字段生成策略：下划线连转驼峰
                .setColumnNaming(NamingStrategy.underline_to_camel)
                // 需要生成的表
                .setInclude(TABLES)
                // 生成controller
                .setRestControllerStyle(true)
                // 去除表前缀
                .setTablePrefix(ENTITY_IGNORE_PREFIX)
                // controller映射地址：驼峰转连字符
                .setControllerMappingHyphenStyle(false)
                // 是否启用builder 模式
                .setChainModel(true)
                // 是否为lombok模型; 需要lombok依赖
                .setEntityLombokModel(true)
                // 生成实体类字段注解
                .setEntityTableFieldAnnotationEnable(true)
                // 乐观锁、逻辑删除、表填充
//                .setVersionFieldName("version")
                .setLogicDeleteFieldName("is_deleted")
                .setTableFillList(Arrays.asList(
                        new TableFill("gmt_modified", FieldFill.UPDATE),
                        new TableFill("gmt_create", FieldFill.INSERT)
                ))
                // 生成类的时候排除的字符串(因为这些字段已经在父类中，子类无需再生成)
                .setSuperEntityColumns(
                        SUPER_ENTITY_COLUMNS
                );
        //配置父类
        if (Objects.nonNull(SUPER_ENTITY_CLASS) && SUPER_ENTITY_CLASS.length() > 0) {
            strategyConfig.setSuperEntityClass(SUPER_ENTITY_CLASS);
        }
        return strategyConfig;
    }

    /**
     * 自定义配置
     *
     * @param isCreateExt
     */
    private static InjectionConfig injectionConfig(boolean isCreateExt) {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                // 注入配置
                Map<String, Object> map = new HashMap<>(16);
                //指定表格的名称
                map.put("customerTableName", true);
                //需要生成扩展的时候执行
                //扩展的表名
                map.put("extPackagePre", PACKAGE_PARENT_EXT);
                //controller包路径
                map.put("controllerPackage", PACKAGE_CONTROLLER);
                //service包名
                map.put("service", PACKAGE_NAME_SERVICE);
                //实现类包名
                map.put("serviceImpl", PACKAGE_NAME_IMPL);
                //mapper包名
                map.put("mapper", PACKAGE_NAME_MAPPER);
                //实现后缀
                map.put("serviceImplNameFix", EXT_SERVICE_CLASSNAME_IMPL);
                //类名扩展后缀
                map.put("classNameExt", EXT_ClASS_NAME);
                for (String tableName : TABLES) {
                    //指定每个类的serialVersionUID
                    long serialVersionUID = UUID.nameUUIDFromBytes(tableName.getBytes()).getLeastSignificantBits();
                    map.put(tableName, serialVersionUID);
                    //计算controller的路径
                    StringBuilder sb = new StringBuilder();
                    if (StringUtils.contains(tableName, UNDERSCORE)) {
                        sb.append(SLASH + tableName.substring(0, tableName.indexOf(UNDERSCORE)));
                        String url = tableName.substring(tableName.indexOf(UNDERSCORE) + 1);
                        sb.append(SLASH + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, url));
                        map.put(tableName + "path", sb.toString());
                        continue;
                    }
                    sb.append(SLASH + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, url));
                    map.put(tableName + "path", sb.toString());
                }
                this.setMap(map);
            }
        }
                // 判断是否创建文件
                .setFileCreate((configBuilder, fileType, filePath) -> {
                    // 检查文件目录，不存在自动递归创建
                    File file = new File(filePath);
                    //是否是扩展文件
                    boolean extFile = filePath.contains(EXT_ClASS_NAME) || filePath.contains(CONTROLLER_CLASS_NAME);
                    //文件是否存在
                    boolean exist = file.exists();
                    //如果需要强制生成可设置isCreateExt字段为true,如果扩展类曾经生成过就跳过生成
                    if (!isCreateExt && exist && extFile) {
                        return false;
                    }
                    if (!exist) {
                        file.getParentFile().mkdirs();
                    }
                    return !exist || configBuilder.getGlobalConfig().isFileOverride();
                })
                // 自定义输出文件
                .setFileOutConfigList(fileOutConfigList());
    }

    /**
     * 全局配置
     *
     * @return
     */
    private static GlobalConfig globalConfig() {
        return new GlobalConfig()
                // 打开文件
                .setOpen(false)
                // 文件覆盖
                .setFileOverride(true)
                // 开启activeRecord模式
                .setActiveRecord(false)
                // XML ResultMap: mapper.xml生成查询映射结果
                .setBaseResultMap(true)
                // XML ColumnList: mapper.xml生成查询结果列
                .setBaseColumnList(true)
                // swagger注解; 须添加swagger依赖
                .setSwagger2(true)
                // 作者
                .setAuthor(AUTHOR)
                // 设置实体类名称
                .setEntityName("%sDO")
                //设置服务类名称
                .setServiceName("%sService")
                ;
    }

    /**
     * 数据源配置
     *
     * @return
     */
    private static DataSourceConfig dataSourceConfig() {
        return new DataSourceConfig()
                // 数据库类型
                .setDbType(DB_TYPE)
                // 连接驱动
                .setDriverName(driverClassName)
                // 地址
                .setUrl(url)
                // 用户名
                .setUsername(username)
                // 密码
                .setPassword(password);
    }

    /**
     * 自定义输出路径
     *
     * @param
     * @return
     */
    private static List<FileOutConfig> fileOutConfigList() {
        List<FileOutConfig> list = new ArrayList<>();
        // 当前项目路径
        String projectPath = System.getProperty("user.dir");

        // 实体类文件输出
        list.add(new FileOutConfig(ENTITY_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + ENTITY_OUTPUT_PATH + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });
        // mapper xml文件输出
        list.add(new FileOutConfig(XML_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + XML_OUTPUT_PATH + tableInfo.getMapperName() + StringPool.DOT_XML;
            }
        });
        // mapper文件输出
        list.add(new FileOutConfig(MAPPER_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + MAPPER_OUTPUT_PATH + tableInfo.getMapperName() + StringPool.DOT_JAVA;
            }
        });
        // service文件输出
        list.add(new FileOutConfig(SERVICE_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + SERVICE_OUTPUT_PATH + tableInfo.getServiceName() + StringPool.DOT_JAVA;
            }
        });
        // service impl文件输出
        list.add(new FileOutConfig(SERVICE_IMPL_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + SERVICE_IMPL_OUTPUT_PATH + tableInfo.getServiceImplName() + StringPool.DOT_JAVA;
            }
        });
        //----------------------------------------------是否需要生成扩展------------------------------------------------------
        // mapperExt xml文件输出
        list.add(new FileOutConfig(XML_TEMPLATE_EXT) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + XML_OUTPUT_PATH_EXT + tableInfo.getMapperName() + EXT_ClASS_NAME + StringPool.DOT_XML;
            }
        });
        // mapperExt文件输出
        list.add(new FileOutConfig(MAPPER_TEMPLATE_EXT) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + MAPPER_OUTPUT_PATH_EXT + tableInfo.getMapperName() + EXT_ClASS_NAME + StringPool.DOT_JAVA;
            }
        });
        // serviceExt文件输出
        list.add(new FileOutConfig(SERVICE_TEMPLATE_EXT) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + SERVICE_OUTPUT_PATH_EXT + tableInfo.getServiceName() + EXT_ClASS_NAME + StringPool.DOT_JAVA;
            }
        });
        // serviceExt impl文件输出
        list.add(new FileOutConfig(SERVICE_IMPL_TEMPLATE_EXT) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + SERVICE_IMPL_OUTPUT_PATH_EXT + tableInfo.getServiceName() + EXT_SERVICE_CLASSNAME_IMPL + StringPool.DOT_JAVA;
            }
        });
        //----------------------------------------------------------------------------------------------------

        // controller文件输出
        list.add(new FileOutConfig(CONTROLLER_TEMPLATE) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + CONTROLLER_OUTPUT_PATH + tableInfo.getControllerName() + StringPool.DOT_JAVA;
            }
        });
        return list;
    }
}
