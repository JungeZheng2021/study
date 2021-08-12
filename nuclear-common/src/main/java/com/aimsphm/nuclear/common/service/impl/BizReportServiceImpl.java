package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.constant.ReportConstant;
import com.aimsphm.nuclear.common.entity.BizReportDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import com.aimsphm.nuclear.common.enums.DataStatusEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.BizReportMapper;
import com.aimsphm.nuclear.common.service.BizReportService;
import com.alibaba.excel.util.FileUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <报告表服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-02-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class BizReportServiceImpl extends ServiceImpl<BizReportMapper, BizReportDO> implements BizReportService {

    @Override
    public Page<BizReportDO> listBizReportByPageWithParams(QueryBO<BizReportDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<BizReportDO> customerConditions(QueryBO<BizReportDO> queryBO) {
        LambdaQueryWrapper<BizReportDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
            //开始时间，结束时间
            wrapper.between(BizReportDO::getReportTime, new Date(query.getStart()), new Date(query.getEnd()));
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
            //报告名称
            wrapper.like(BizReportDO::getReportName, query.getKeyword());
        }
        //报告生成时间降序
        wrapper.orderBy(true, false, BizReportDO::getReportTime);
        return wrapper;
    }

    @Override
    public List<BizReportDO> listBizReportWithParams(QueryBO<BizReportDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public void downLoad2Website(Long id) {
        BizReportDO find = this.getById(id);
        if (Objects.isNull(find)) {
            throw new CustomMessageException("the file not exist");
        }
        File file = new File(ReportConstant.DOC_TEMP_DIR_PRE + find.getReportPath());
        try {
            byte[] byteArray = FileUtils.readFileToByteArray(file);
            downLoadFile2Website(byteArray, find.getReportName() + ".docx");
        } catch (IOException e) {
            throw new CustomMessageException("download file failed");
        }
    }

    @Override
    public boolean removeReportById(Long id) {
        BizReportDO find = this.getById(id);
        if (Objects.isNull(find)) {
            throw new CustomMessageException("the report not exist");
        }
        if (this.removeById(id)) {
            String reportPath = find.getReportPath();
            File file = new File(ReportConstant.DOC_TEMP_DIR_PRE + reportPath);
            if (file.exists()) {
                //真实删除
                return file.delete();
            }
        }
        return false;
    }

    @Override
    public BizReportDO getReport(ReportQueryBO query) {
        LambdaQueryWrapper<BizReportDO> wrapper = Wrappers.lambdaQuery(BizReportDO.class);
        wrapper.eq(BizReportDO::getSubSystemId, query.getSubSystemId())
                .eq(BizReportDO::getStatus, DataStatusEnum.RUNNING.getValue());
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public void updateReportStatus(Long reportId, Integer status, String cause) {
        BizReportDO reportDO = new BizReportDO();
        reportDO.setId(reportId);
        reportDO.setStatus(status);
        reportDO.setRemark(cause);
        this.updateById(reportDO);
    }

    /**
     * 下载文档到浏览器
     *
     * @param data     文件数据
     * @param filename 文件名称
     * @throws IOException
     */
    public static void downLoadFile2Website(byte[] data, String filename) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setHeader("Content-disposition", "attachment; filename="
                //设置下载的文件名
                + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
