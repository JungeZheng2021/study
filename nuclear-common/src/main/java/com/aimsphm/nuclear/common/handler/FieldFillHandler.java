package com.aimsphm.nuclear.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Package: com.aimsphm.nuclear.common.handler
 * @Description: <自动填充字段>
 * @Author: milla
 * @CreateDate: 2020/11/14 13:29
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/14 13:29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class FieldFillHandler implements MetaObjectHandler {
    public static final String OPERATOR = "服务器";

    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.setFieldValByName("creator", OPERATOR, metaObject);
        this.setFieldValByName("modifier", OPERATOR, metaObject);
        this.setFieldValByName("gmtCreate", date, metaObject);
        this.setFieldValByName("gmtModified", date, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        this.setFieldValByName("modifier", OPERATOR, metaObject);
    }
}
