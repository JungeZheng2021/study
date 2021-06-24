package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.entity.vo.PointFeatureVO;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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
@Api(tags = "point-测点信息-相关接口")
@RequestMapping(value = "/common/measurePoint", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonMeasurePointController {
    @Resource
    private CommonMeasurePointService iCommonMeasurePointServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "测点信息分页查询")
    public Page<CommonMeasurePointDO> listCommonMeasurePointServiceByPage(Page<CommonMeasurePointDO> page, CommonMeasurePointDO entity, ConditionsQueryBO query) {
        return iCommonMeasurePointServiceExt.listCommonMeasurePointByPageWithParams(new QueryBO(page, entity, query));
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
        return iCommonMeasurePointServiceExt.modifyCommonMeasurePoint(dto);
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

    @GetMapping("feature/list")
    @ApiOperation(value = "根据条件获取特征值")
    public PointFeatureVO listFeaturesBySensorCode(CommonQueryBO query) {
        return iCommonMeasurePointServiceExt.listFeaturesByConditions(query);
    }

    @GetMapping("/points")
    @ApiOperation(value = "根据条件获取需要的测点", notes = "优先级为：系统、子系统、设备(比如有系统id就不会再使用子系统id)")
    public List<CommonMeasurePointDO> listPointsByDeviceId(CommonQueryBO query) {
        return iCommonMeasurePointServiceExt.listPointsByConditions(query);
    }

    @GetMapping("/points/group")
    @ApiOperation(value = "获取需要的传感器数据", notes = "历史查询模块使用")
    public List<CommonMeasurePointDO> listSensorByGroup(CommonQueryBO query) {
        return iCommonMeasurePointServiceExt.listSensorByGroup(query);
    }

    @GetMapping("/entities")
    @ApiOperation(value = "根据条件获取需要的测点", notes = "不包括使用deviceId和subSystemId查询")
    public List<CommonMeasurePointDO> listPointsByEntity(CommonMeasurePointDO entity) {
        return iCommonMeasurePointServiceExt.list(Wrappers.lambdaQuery(entity));
    }

    @GetMapping("/locations")
    @ApiOperation(value = "获取所有的测点位置", notes = "全量去重数据")
    public List<LabelVO> listLocationInfo(CommonQueryBO query) {
        return iCommonMeasurePointServiceExt.listLocationInfo(query);
    }

    @GetMapping("features")
    @ApiOperation(value = "获取所有的特征")
    public Set<String> listFeatures() {
        return iCommonMeasurePointServiceExt.listFeatures();
    }

    @GetMapping("points/inModel")
    @ApiOperation(value = "判断测点是否在模型中")
    public Map<String, Long> listPointByDeviceIdInModel(@RequestParam("pointIds") List<String> pointIds) {
        return iCommonMeasurePointServiceExt.listPointByDeviceIdInModel(pointIds);
    }

    @GetMapping("points/alias")
    @ApiOperation(value = "获取测点别名与中文名")
    List<CommonMeasurePointDO> listPointAliasAndName(@RequestParam("pointIds") List<String> pointIds, CommonQueryBO queryBO) {
        return iCommonMeasurePointServiceExt.listPointAliasAndName(pointIds, queryBO);
    }
}