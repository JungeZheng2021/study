package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.core.entity.vo.PanoramaVO;
import com.aimsphm.nuclear.core.service.PanoramaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 功能描述:系统总览-全景
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-12-17 13:47
 */
@RestController()
@Api(tags = "panorama-系统总览控制类")
@RequestMapping(value = "panorama", produces = MediaType.APPLICATION_JSON_VALUE)
public class PanoramaController {
    @Resource
    private PanoramaService panoramaService;

    @GetMapping("details")
    @ApiOperation(value = "系统总览")
    public List<PanoramaVO> getPanoramaDetails(Long subSystemId) {
        try {
            panoramaService.getPanoramaDetails(subSystemId);
            System.out.println("开始等待.....");
            Thread.sleep(301_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return panoramaService.getPanoramaDetails(subSystemId);
    }
}
