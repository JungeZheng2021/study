package com.aimsphm.nuclear.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * <p>
 * 功能描述:表格操作类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2018/5/4 16:02
 */
@Slf4j
@Component
public class DBTableUtil {

    @Resource
    JdbcTemplate template;

    public Boolean tableExists(String schema, String tbName) {

        Connection conn = null;
        ResultSet tabs = null;
        try {
            conn = template.getDataSource().getConnection();
            DatabaseMetaData dbMetaData = conn.getMetaData();
            String[] types = {"TABLE"};
            tabs = dbMetaData.getTables(schema, null, tbName, types);
            if (tabs.next()) {
                return true;
            }
        } catch (Exception e) {
            log.error("check table error:{}", e);
        } finally {
            try {
                if (Objects.nonNull(tabs)) {
                    tabs.close();
                }
                if (Objects.nonNull(conn)) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("error");
            }
        }
        return false;
    }

    public boolean createAutoDailyDownSampleTable(String schema, String tableName) {
        Boolean aBoolean = tableExists(schema, tableName);
        if (aBoolean) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `" + tableName + "`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `algorithm_type` int(11) NULL DEFAULT 1 COMMENT '算法类型',\n" +
                "  `point_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '测点id',\n" +
                "  `start_timestamp` bigint(20) NULL DEFAULT NULL COMMENT '开始时间',\n" +
                "  `points` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '测点数据值',\n" +
                "  `sort` tinyint(4) NULL DEFAULT NULL COMMENT '排序字段',\n" +
                "  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',\n" +
                "  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',\n" +
                "  `gmt_create` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',\n" +
                "  `gmt_modified` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',\n" +
                "  `deleted` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '是否被删除 1：数据无效（被删除） 0：数据有效',\n" +
                "  PRIMARY KEY (`id`) USING BTREE,\n" +
                "  UNIQUE INDEX `uc`(`point_id`, `algorithm_type`, `start_timestamp`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic");
        try {
            return template.update(sb.toString()) > 0;
        } catch (Exception e) {
            log.error("create table error:{}", e);
        }
        return false;
    }

}
