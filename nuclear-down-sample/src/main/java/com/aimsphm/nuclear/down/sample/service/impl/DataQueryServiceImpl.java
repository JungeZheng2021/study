package com.aimsphm.nuclear.down.sample.service.impl;

import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.down.sample.service.DataQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_FAMILY_NPC_PI_REAL_TIME;
import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 09:48
 */
@Slf4j
@Service
public class DataQueryServiceImpl implements DataQueryService {

    @Resource
    private HBaseUtil hBase;

    @Override
    public List<List<Object>> listHoursData(HistoryQuerySingleWithFeatureBO single) {
        try {
            return hBase.listDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, single.getSensorCode(), single.getStart(), single.getEnd()
                    , StringUtils.isNotBlank(single.getFeature()) ? single.getFeature() : H_BASE_FAMILY_NPC_PI_REAL_TIME);
        } catch (IOException e) {
            throw new CustomMessageException("查询历史数据失败");
        }
    }
}
