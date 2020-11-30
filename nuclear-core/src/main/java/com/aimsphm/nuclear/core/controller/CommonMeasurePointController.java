package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.PointFeatureVO;
import com.aimsphm.nuclear.ext.service.CommonMeasurePointServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <测点信息-前端控制器>
 * @Author: milla
 * @CreateDate: 2020-11-17
 * @UpdateUser: milla
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "测点信息-相关接口")
@RequestMapping(value = "/common/measurePoint", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonMeasurePointController {
    @Autowired
    private CommonMeasurePointServiceExt iCommonMeasurePointServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "测点信息分页查询")
    public Page<CommonMeasurePointDO> listCommonMeasurePointServiceByPage(QueryBO<CommonMeasurePointDO> query) {
        return iCommonMeasurePointServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "测点信息获取某一实体")
    public CommonMeasurePointDO getCommonMeasurePointServiceDetails(@PathVariable Long id) {
        return iCommonMeasurePointServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "测点信息新增数据")
    public boolean saveCommonMeasurePointService(@RequestBody CommonMeasurePointDO dto) {
        return iCommonMeasurePointServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "测点信息修改数据")
    public boolean modifyCommonMeasurePointService(@RequestBody CommonMeasurePointDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iCommonMeasurePointServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "测点信息批量删除数据")
    public boolean batchRemoveCommonMeasurePointService(@RequestParam(value = "ids") List<Long> ids) {
        return iCommonMeasurePointServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "测点信息删除数据")
    public boolean removeCommonMeasurePointService(@PathVariable Long id) {
        return iCommonMeasurePointServiceExt.removeById(id);
    }

    @GetMapping("features/{sensorCode}")
    @ApiOperation(value = "某个测点编号下的所有特征")
    public PointFeatureVO listFeaturesBySensorCode(@PathVariable String sensorCode) {
        return iCommonMeasurePointServiceExt.listFeatures(sensorCode);
    }

    @GetMapping("{deviceId}/points/{visible}")
    @ApiOperation(value = "获取某个设备下所有测点信息")
    public List<CommonMeasurePointDO> listPointsByDeviceId(@PathVariable Long deviceId, @PathVariable Integer visible) {
        return iCommonMeasurePointServiceExt.listPointsByDeviceId(deviceId, visible);
    }

    @GetMapping("features")
    @ApiOperation(value = "测点信息获取某一实体----")
    public Set<String> listFeatures() {
        return iCommonMeasurePointServiceExt.listFeatures();
    }

}