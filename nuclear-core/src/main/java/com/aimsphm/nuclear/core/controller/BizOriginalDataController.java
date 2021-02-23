package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.BizOriginalDataDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.BizOriginalDataService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <波形数据信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-02-03
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "BizOriginalData-波形数据信息-相关接口")
@RequestMapping(value = "biz/original/data", produces = MediaType.APPLICATION_JSON_VALUE)
public class BizOriginalDataController {

    @Resource
    private BizOriginalDataService service;

    @GetMapping("list")
    @ApiOperation(value = "波形数据信息列表查询", notes = "多条件组合查询")
    public List<BizOriginalDataDO> listBizOriginalDataWithParams(BizOriginalDataDO entity, ConditionsQueryBO query) {
        return service.listBizOriginalDataWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "波形数据信息分页查询", notes = "多条件组合查询")
    public Page<BizOriginalDataDO> listBizOriginalDataByPageWithParams(Page<BizOriginalDataDO> page, BizOriginalDataDO entity, ConditionsQueryBO query) {
        return service.listBizOriginalDataByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "波形数据信息获取某一实体")
    public BizOriginalDataDO getBizOriginalDataDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "波形数据信息新增数据")
    public boolean saveBizOriginalData(@RequestBody BizOriginalDataDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "波形数据信息修改数据")
    public boolean modifyBizOriginalData(@RequestBody BizOriginalDataDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "波形数据信息批量删除数据")
    public boolean batchRemoveBizOriginalData(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "波形数据信息删除数据")
    public boolean removeBizOriginalData(@PathVariable Long id) {
        return service.removeById(id);
    }
}