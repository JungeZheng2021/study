package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AnalysisFavoriteDO;
import com.aimsphm.nuclear.common.entity.AnalysisFavoriteRemarkDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.AnalysisFavoriteVO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.AnalysisFavoriteMapper;
import com.aimsphm.nuclear.common.service.AnalysisFavoriteRemarkService;
import com.aimsphm.nuclear.common.service.AnalysisFavoriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <振动分析收藏夹服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-01-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AnalysisFavoriteServiceImpl extends ServiceImpl<AnalysisFavoriteMapper, AnalysisFavoriteDO> implements AnalysisFavoriteService {

    @Resource
    AnalysisFavoriteRemarkService remarkService;

    @Override
    public Page<AnalysisFavoriteDO> listAnalysisFavoriteByPageWithParams(QueryBO<AnalysisFavoriteDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<AnalysisFavoriteDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
            wrapper.ge(AnalysisFavoriteDO::getAcquisitionTime, query.getStart()).le(AnalysisFavoriteDO::getAcquisitionTime, query.getEnd());
        }
        wrapper.orderByDesc(AnalysisFavoriteDO::getAcquisitionTime);
        Page<AnalysisFavoriteDO> page = this.page(queryBO.getPage(), wrapper);
        List<AnalysisFavoriteDO> records = page.getRecords();
        List collect = records.stream().map(item -> {
            AnalysisFavoriteVO favorite = new AnalysisFavoriteVO();
            BeanUtils.copyProperties(item, favorite);
            favorite.setRemarked(remarkService.countRemarked(favorite.getId()) > 0);
            return favorite;
        }).collect(Collectors.toList());
        page.setRecords(collect);
        return page;
    }

    @Override
    public boolean saveFavoriteAndRemark(AnalysisFavoriteVO entity) {
        checkParams(entity);
        LambdaQueryWrapper<AnalysisFavoriteDO> wrapper = Wrappers.lambdaQuery(AnalysisFavoriteDO.class);
        wrapper.eq(AnalysisFavoriteDO::getPointId, entity.getPointId()).eq(AnalysisFavoriteDO::getAcquisitionTime, entity.getAcquisitionTime());
        if (this.count(wrapper) > 0) {
            throw new CustomMessageException("this favorite has been already exist");
        }
        boolean save = this.save(entity);
        String remark = entity.getRemark();
        if (StringUtils.isBlank(remark)) {
            return save;
        }
        AnalysisFavoriteRemarkDO remarkDO = new AnalysisFavoriteRemarkDO();
        remarkDO.setRemark(remark);
        remarkDO.setFavoriteId(entity.getId());
        return remarkService.save(remarkDO) && save;
    }

    private void checkParams(AnalysisFavoriteDO entity) {
        if (StringUtils.isBlank(entity.getPointId()) || Objects.isNull(entity.getAcquisitionTime())) {
            throw new CustomMessageException("params can not be null");
        }
    }

    @Override
    public AnalysisFavoriteVO getAnalysisFavoriteWithParams(AnalysisFavoriteDO entity) {
        checkParams(entity);
        LambdaQueryWrapper<AnalysisFavoriteDO> wrapper = Wrappers.lambdaQuery(AnalysisFavoriteDO.class);
        wrapper.eq(AnalysisFavoriteDO::getPointId, entity.getPointId()).eq(AnalysisFavoriteDO::getAcquisitionTime, entity.getAcquisitionTime());
        AnalysisFavoriteDO one = this.getOne(wrapper);
        if (Objects.isNull(one)) {
            return null;
        }
        AnalysisFavoriteVO vo = new AnalysisFavoriteVO();
        BeanUtils.copyProperties(one, vo);
        List<AnalysisFavoriteRemarkDO> remarkList = remarkService.listRemarkByFavoriteId(one.getId());
        vo.setRemarkList(remarkList);
        return vo;
    }
}
