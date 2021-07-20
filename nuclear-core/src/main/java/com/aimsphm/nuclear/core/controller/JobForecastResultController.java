package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.JobForecastResultDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.JobForecastResultVO;
import com.aimsphm.nuclear.common.service.JobForecastResultService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <预测结果信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-07-15
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-15
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "JobForecastResult-预测结果信息-相关接口")
@RequestMapping(value = "job/forecastResult", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobForecastResultController {

    @Resource
    private JobForecastResultService service;

    @GetMapping("list")
    @ApiOperation(value = "预测结果信息列表查询", notes = "多条件组合查询")
    public List<JobForecastResultDO> listJobForecastResultWithParams(JobForecastResultDO entity, ConditionsQueryBO query) {
        return service.listJobForecastResultWithParams(new QueryBO(entity, query));
    }

    @GetMapping("list/{deviceId}/{componentId}")
    @ApiOperation(value = "根据部件位置性能预测查询", notes = "")
    public JobForecastResultVO listJobForecastResultByIds(@PathVariable Long deviceId, @PathVariable Long componentId) {
        return service.listJobForecastResultByIds(deviceId, componentId);
    }

    @GetMapping("pages")
    @ApiOperation(value = "预测结果信息分页查询", notes = "多条件组合查询")
    public Page<JobForecastResultDO> listJobForecastResultByPageWithParams(Page<JobForecastResultDO> page, JobForecastResultDO entity, ConditionsQueryBO query) {
        return service.listJobForecastResultByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "预测结果信息获取某一实体")
    public JobForecastResultDO getJobForecastResultDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "预测结果信息新增数据")
    public boolean saveJobForecastResult(@RequestBody JobForecastResultDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "预测结果信息修改数据")
    public boolean modifyJobForecastResult(@RequestBody JobForecastResultDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "预测结果信息批量删除数据")
    public boolean batchRemoveJobForecastResult(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "预测结果信息删除数据")
    public boolean removeJobForecastResult(@PathVariable Long id) {
        return service.removeById(id);
    }
}