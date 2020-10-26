package com.aimsphm.nuclear.core.entity;

import com.aimsphm.nuclear.common.entity.ModelBase;
import lombok.Data;

import java.util.Date;

/**
 * @author Mao
 * @since 2020-05-11
 */
@Data
public class MdRuntimeBaseCfg extends ModelBase {
    private static final long serialVersionUID = -6558673507397086619L;
    private Long siteId;
    private Long setId;
    private Long subSystemId;
    private Long deviceId;
    private String initialParameterName;
    private String initialParameterDataType;
    private String initialParameterValue;
    private String parameterDisplayName;
    private Integer unit;
    //private LocalDateTime startDateTime;
    private Date startDateTime;
}