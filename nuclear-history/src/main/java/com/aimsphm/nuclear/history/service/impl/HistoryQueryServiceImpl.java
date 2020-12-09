package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.ext.service.CommonMeasurePointServiceExt;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataWithThresholdVO;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_FAMILY_NPC_PI_REAL_TIME;
import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.DASH;

/**
 * @Package: com.aimsphm.nuclear.history.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 17:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 17:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class HistoryQueryServiceImpl implements HistoryQueryService {

    @Autowired
    private CommonMeasurePointServiceExt serviceExt;

    private HBaseUtil hBase;

    public HistoryQueryServiceImpl(HBaseUtil hBase) {
        this.hBase = hBase;
    }

    @Override
    public List<HBaseTimeSeriesDataDTO> listHistoryDataWithSingleTagByScan(HistoryQuerySingleWithFeatureBO single) {
        if (Objects.isNull(single) || Objects.isNull(single.getSensorCode())
                || Objects.isNull(single.getEnd()) || Objects.isNull(single.getStart()) || single.getEnd() <= single.getStart()) {
            return null;
        }
        String family = H_BASE_FAMILY_NPC_PI_REAL_TIME;
        if (StringUtils.isNotBlank(single.getFeature())) {
            family = single.getFeature();
        }
        try {
            return hBase.listDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, single.getSensorCode(), single.getStart(), single.getEnd(), family);
        } catch (IOException e) {
            throw new CustomMessageException("查询历史数据失败");
        }
    }

    @Override
    public HistoryDataVO listHistoryDataWithSingleTagByScan(HistoryQuerySingleBO singleBO) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getPointId, singleBO.getPointId());
        CommonMeasurePointDO point = serviceExt.getOne(wrapper);
        if (Objects.isNull(point)) {
            throw new CustomMessageException("要查询的测点不存在");
        }
        HistoryQuerySingleWithFeatureBO featureBO = new HistoryQuerySingleWithFeatureBO();
        if (point.getPointType() != 1 && StringUtils.isNotBlank(point.getFeature())) {
            featureBO.setFeature(point.getFeatureType().concat(DASH).concat(point.getFeature()));
        }
        featureBO.setSensorCode(point.getSensorCode());
        BeanUtils.copyProperties(singleBO, featureBO);
        List<HBaseTimeSeriesDataDTO> dataDTOS = listHistoryDataWithSingleTagByScan(featureBO);
        HistoryDataVO vo = new HistoryDataWithThresholdVO();
        BeanUtils.copyProperties(point, vo);
        vo.setActualData(dataDTOS);
        return vo;
    }
}
