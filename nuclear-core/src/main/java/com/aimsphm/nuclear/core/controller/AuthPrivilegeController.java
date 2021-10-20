package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AuthPrivilegeDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.AuthPrivilegeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 功能描述:权限资源信息-前端控制器
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-5-26 13:35
 */
@RestController
@Api(tags = "AuthPrivilege-权限资源信息-相关接口")
@RequestMapping(value = "auth/privilege", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthPrivilegeController {

    @Resource
    private AuthPrivilegeService service;

    @GetMapping("list")
    @ApiOperation(value = "权限资源信息列表查询", notes = "多条件组合查询")
    public List<AuthPrivilegeDO> listAuthPrivilegeWithParams(AuthPrivilegeDO entity, ConditionsQueryBO query) {
        return service.listAuthPrivilegeWithParams(new QueryBO(entity, query));
    }

    @GetMapping("data")
    @ApiOperation(value = "权限资源信息列表查询", notes = "多条件组合查询")
    public List<AuthPrivilegeDO> listAuthPrivilege() {
        return service.listAuthPrivilege();
    }

    @GetMapping("records")
    @ApiOperation(value = "获取某用户下具备的权限列表")
    public List<AuthPrivilegeDO> listAuthPrivilege(HttpServletRequest request) {
        String username = request.getHeader("token");
        String sysCode = request.getHeader("sysCode");
        String structured = request.getHeader("structured");
        return service.listAuthPrivilege(username, sysCode, structured);
    }

    @GetMapping("getUserId")
    @ApiOperation(value = "获取用户对应的userid")
    public String getUserIdByUsername(HttpServletRequest request) {
        String username = request.getHeader("token");
        return service.getUserIdByUsername(username);
    }

    @GetMapping("pages")
    @ApiOperation(value = "权限资源信息分页查询", notes = "多条件组合查询")
    public Page<AuthPrivilegeDO> listAuthPrivilegeByPageWithParams(Page<AuthPrivilegeDO> page, AuthPrivilegeDO entity, ConditionsQueryBO query) {
        return service.listAuthPrivilegeByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "权限资源信息获取某一实体")
    public AuthPrivilegeDO getAuthPrivilegeDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "权限资源信息新增数据")
    public boolean saveAuthPrivilege(@RequestBody AuthPrivilegeDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "权限资源信息修改数据")
    public boolean modifyAuthPrivilege(@RequestBody AuthPrivilegeDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "权限资源信息批量删除数据")
    public boolean batchRemoveAuthPrivilege(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "权限资源信息删除数据")
    public boolean removeAuthPrivilege(@PathVariable Long id) {
        return service.removeById(id);
    }
}