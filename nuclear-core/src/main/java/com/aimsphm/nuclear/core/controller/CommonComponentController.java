package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonComponentDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.CommonComponentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <组件信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-06-03
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "CommonComponent-组件信息-相关接口")
@RequestMapping(value = "common/component", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonComponentController {

    @Resource
    private CommonComponentService service;

    @GetMapping("list")
    @ApiOperation(value = "组件信息列表查询", notes = "多条件组合查询")
    public List<CommonComponentDO> listCommonComponentWithParams(CommonComponentDO entity, ConditionsQueryBO query) {
        return service.listCommonComponentWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "组件信息分页查询", notes = "多条件组合查询")
    public Page<CommonComponentDO> listCommonComponentByPageWithParams(Page<CommonComponentDO> page, CommonComponentDO entity, ConditionsQueryBO query) {
        return service.listCommonComponentByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "组件信息获取某一实体")
    public CommonComponentDO getCommonComponentDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "组件信息新增数据")
    public boolean saveCommonComponent(@RequestBody CommonComponentDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "组件信息修改数据")
    public boolean modifyCommonComponent(@RequestBody CommonComponentDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "组件信息批量删除数据")
    public boolean batchRemoveCommonComponent(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "组件信息删除数据")
    public boolean removeCommonComponent(@PathVariable Long id) {
        return service.removeById(id);
    }
}