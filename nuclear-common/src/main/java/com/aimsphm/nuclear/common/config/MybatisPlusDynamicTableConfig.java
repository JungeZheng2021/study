package com.aimsphm.nuclear.common.config;

import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 功能描述:mybatis-plush配置类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/09/04 14:42
 */
@Configuration
@MapperScan("com.aimsphm.nuclear.common.mapper**")
@ConditionalOnProperty(prefix = "spring.config", name = "enableMybatisPlusDynamicTable", havingValue = "true")
public class MybatisPlusDynamicTableConfig {

    private static final String DYNAMIC_TABLE_PRE = "spark_down_sample";

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setLimit(500);
//        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>(2);
        tableNameHandlerMap.put(DYNAMIC_TABLE_PRE, (metaObject, sql, tableName) -> DynamicTableTreadLocal.INSTANCE.getTableName());
        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);
        paginationInterceptor.setSqlParserList(Collections.singletonList(dynamicTableNameParser));
        return paginationInterceptor;
    }
}
