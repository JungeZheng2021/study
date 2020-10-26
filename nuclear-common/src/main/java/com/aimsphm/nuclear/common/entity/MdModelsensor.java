package com.aimsphm.nuclear.common.entity;

import com.aimsphm.nuclear.common.entity.ModelBase;
import lombok.Data;

/**
 * @author lu.yi
 * @since 2020-04-13
 */
@Data
public class MdModelsensor extends ModelBase {
    private static final long serialVersionUID = 1992623811958075839L;
    private Long modelId;
    private Long sensorId;
}