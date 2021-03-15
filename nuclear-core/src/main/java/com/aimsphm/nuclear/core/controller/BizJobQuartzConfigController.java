package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.BizJobQuartzConfigDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.BizJobQuartzConfigService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <算法配置-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-02-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "BizJobQuartzConfig-算法配置-相关接口")
@RequestMapping(value = "biz/jobQuartzConfig", produces = MediaType.APPLICATION_JSON_VALUE)
public class BizJobQuartzConfigController {

    @Resource
    private BizJobQuartzConfigService service;

    @GetMapping("list")
    @ApiOperation(value = "算法配置列表查询", notes = "多条件组合查询")
    public List<BizJobQuartzConfigDO> listBizJobQuartzConfigWithParams(BizJobQuartzConfigDO entity, ConditionsQueryBO query) {
        return service.listBizJobQuartzConfigWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "算法配置分页查询", notes = "多条件组合查询")
    public Page<BizJobQuartzConfigDO> listBizJobQuartzConfigByPageWithParams(Page<BizJobQuartzConfigDO> page, BizJobQuartzConfigDO entity, ConditionsQueryBO query) {
        return service.listBizJobQuartzConfigByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "算法配置获取某一实体")
    public BizJobQuartzConfigDO getBizJobQuartzConfigDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "算法配置新增数据")
    public boolean saveBizJobQuartzConfig(@RequestBody BizJobQuartzConfigDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "算法配置修改数据")
    public boolean modifyBizJobQuartzConfig(@RequestBody BizJobQuartzConfigDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "算法配置批量删除数据")
    public boolean batchRemoveBizJobQuartzConfig(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "算法配置删除数据")
    public boolean removeBizJobQuartzConfig(@PathVariable Long id) {
        return service.removeById(id);
    }
}