package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
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
    public List<HBaseTimeSeriesDataDTO> listHistoryWithSingleTagByScan(HistoryQuerySingleBO single) {
        if (Objects.isNull(single) || Objects.isNull(single.getTagId())
                || Objects.isNull(single.getEnd()) || Objects.isNull(single.getStart()) || single.getEnd() <= single.getStart()) {
            return null;
        }
        String family = H_BASE_FAMILY_NPC_PI_REAL_TIME;
        if (StringUtils.isNotBlank(single.getFeature())) {
            family = single.getFeature();
        }
        try {
            return hBase.listDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, single.getTagId(), single.getStart(), single.getEnd(), family);
        } catch (IOException e) {
            throw new CustomMessageException("查询历史数据失败");
        }
    }

    @Override
    public HistoryDataVO listHistoryDataWithSingleTagByScan(HistoryQuerySingleBO singleBO) {
        String feature = singleBO.getFeature();
        List<HBaseTimeSeriesDataDTO> dataDTOS = listHistoryWithSingleTagByScan(singleBO);
        //Pi测点
        if (StringUtils.isBlank(feature) || !feature.contains(DASH)) {
            HistoryDataVO vo = new HistoryDataVO();
            vo.setActualData(dataDTOS);
            return vo;
        }
        //自装测点
        HistoryDataWithThresholdVO vo = new HistoryDataWithThresholdVO();
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getSensorCode, singleBO.getTagId()).eq(CommonMeasurePointDO::getFeatureType, feature.split(DASH)[0])
                .eq(CommonMeasurePointDO::getFeature, feature.split(DASH)[1]);
        CommonMeasurePointDO one = serviceExt.getOne(wrapper);
        BeanUtils.copyProperties(one, vo);
        vo.setActualData(dataDTOS);
        return vo;
    }
}
