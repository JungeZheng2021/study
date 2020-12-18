package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonSubSystemDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.service.CommonSubSystemService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <子系统信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "subSystem-子系统信息-相关接口")
@RequestMapping(value = "/common/subSystem", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonSubSystemController {
    @Autowired
    private CommonSubSystemService iCommonSubSystemServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "子系统信息分页查询")
    public Page<CommonSubSystemDO> listCommonSubSystemServiceByPage(Page<CommonSubSystemDO> page, CommonSubSystemDO entity, ConditionsQueryBO query) {
        return iCommonSubSystemServiceExt.listCommonSubSystemByPageWithParams(new QueryBO<>(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "子系统信息获取某一实体")
    public CommonSubSystemDO getCommonSubSystemServiceDetails(@PathVariable Long id) {
        return iCommonSubSystemServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "子系统信息新增数据")
    public boolean saveCommonSubSystemService(@RequestBody CommonSubSystemDO dto) {
        return iCommonSubSystemServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "子系统信息修改数据")
    public boolean modifyCommonSubSystemService(@RequestBody CommonSubSystemDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iCommonSubSystemServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "子系统信息批量删除数据")
    public boolean batchRemoveCommonSubSystemService(@RequestParam(value = "ids") List<Long> ids) {
        return iCommonSubSystemServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "子系统信息删除数据")
    public boolean removeCommonSubSystemService(@PathVariable Long id) {
        return iCommonSubSystemServiceExt.removeById(id);
    }

    @GetMapping("/tree/{id}")
    @ApiOperation(value = "获取某子系统信息结构树")
    public TreeVO getCommonSubSystemTree(@PathVariable Long id) {
        return iCommonSubSystemServiceExt.listCommonSubSystemTree(id);
    }
}