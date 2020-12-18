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

/**
 * @Package: com.study.auth.config.core
 * @Description: <mybatis-plush配置类>
 * @Author: MILLA
 * @CreateDate: 2020/09/04 14:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/09/04 14:42
 * @UpdateRemark: <>
 * @Version: 1.0
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
        dynamicTableNameParser.setTableNameHandlerMap(new HashMap<String, ITableNameHandler>(2) {{
            //动态表规则-生成自己需要的动态表名
            put(DYNAMIC_TABLE_PRE, (metaObject, sql, tableName) -> DynamicTableTreadLocal.INSTANCE.getTableName());
        }});
        paginationInterceptor.setSqlParserList(Collections.singletonList(dynamicTableNameParser));
        return paginationInterceptor;
    }
}
