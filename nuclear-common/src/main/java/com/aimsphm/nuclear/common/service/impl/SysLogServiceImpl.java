package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.SysLog;
import com.aimsphm.nuclear.common.mapper.SysLogMapper;
import com.aimsphm.nuclear.common.service.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

}
