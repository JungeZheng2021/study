package com.aimsphm.nuclear.common.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>
 * 功能描述: 动态切换日志级别
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/11 10:54
 */
@RestController
@Api(tags = "logback-config-日志切换")
@RequestMapping(value = "log/level", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogbackController {
    @GetMapping("{level}/{packageName}")
    @ApiOperation(value = "设置指定包的日志级别", notes = "如果是包为空或者是-1就是全部的包")
    public String setLogLevel(@PathVariable String level, @PathVariable String packageName) {
        return getOrSetLogLevel(level, packageName);
    }

    private String getOrSetLogLevel(String level, String packageName) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = Objects.isNull(packageName) || "-1".equals(packageName) ? context.getLogger("root") : context.getLogger(packageName);
        if (Objects.nonNull(level)) {
            root.setLevel(Level.toLevel(level));
        }
        return root.getLevel().toString();
    }

    @GetMapping("{packageName}")
    @ApiOperation(value = "获取指定包的日志级别", notes = "如果是包为空或者是-1就是全部的包")
    public String getLogLevel(@PathVariable String packageName) {
        return getOrSetLogLevel(null, packageName);
    }
}
