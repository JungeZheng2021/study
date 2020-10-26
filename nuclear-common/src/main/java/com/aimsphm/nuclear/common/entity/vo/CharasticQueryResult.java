package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.dto.Cell;
import lombok.Data;

import java.util.List;

@Data
public class CharasticQueryResult {
    private String charasticName;
    private List<Cell> pointList;
}
