package com.aimsphm.nuclear.report.controller;

import com.aimsphm.nuclear.common.entity.BizReportDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import com.aimsphm.nuclear.common.service.BizReportService;
import com.aimsphm.nuclear.report.service.ReportService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.report.controller
 * @Description: <报告表-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-02-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@RestController
@Api(tags = "BizReport-报告表-相关接口")
@RequestMapping(value = "biz/report", produces = MediaType.APPLICATION_JSON_VALUE)
public class BizReportController {
    @Resource
    private ReportService reportService;

    @Resource
    private BizReportService service;

    @GetMapping("list")
    @ApiOperation(value = "报告表列表查询", notes = "多条件组合查询")
    public List<BizReportDO> listBizReportWithParams(BizReportDO entity, ConditionsQueryBO query) {
        return service.listBizReportWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "报告表分页查询", notes = "多条件组合查询")
    public Page<BizReportDO> listBizReportByPageWithParams(Page<BizReportDO> page, BizReportDO entity, ConditionsQueryBO query) {
        return service.listBizReportByPageWithParams(new QueryBO(page, entity, query));
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "报告表批量删除数据")
    public boolean batchRemoveBizReport(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "报告删除数据")
    public boolean removeBizReport(@PathVariable Long id) {
        return service.removeReportById(id);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "下载文档")
    public void downLoad2Website(@PathVariable Long id) {
        service.downLoad2Website(id);
    }

    @PostMapping
    @ApiOperation(value = "自定义报告", notes = "只能自定义报告名称及报告的起止时间")
    public void saveBizReport(@RequestBody ReportQueryBO query) {
        long currentTimeMillis = System.currentTimeMillis();
        query.setEndTime(currentTimeMillis);
        query.setStartTime(currentTimeMillis - ChronoUnit.DAYS.getDuration().getSeconds() * 30 * 1000);
        reportService.saveManualReport(query);
        long l = System.currentTimeMillis() - currentTimeMillis;
        log.debug("共计用时：{}", l);
    }
}