package com.aimsphm.nuclear.down.sample.entity.bo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能描述:三点法
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 12:49
 */
@Data
public class ThreePointBO {

    /**
     * 第一个值
     */
    private List<Object> first;
    /**
     * 最小值
     */
    private List<Object> min;
    /**
     * 最大值
     */
    private List<Object> max;

    /**
     * 排序的结果
     *
     * @return 集合
     */
    public List<List<Object>> sorted() {
        List<List<Object>> data = new ArrayList<>();
        if (Objects.isNull(first)) {
            return data;
        }
        data.add(first);
        if (Objects.nonNull(max)) {
            data.add(max);
        }
        if (Objects.nonNull(min)) {
            data.add(min);
        }
        return data.stream().sorted(Comparator.comparing(a -> ((Long) a.get(0)))).collect(Collectors.toList());
    }
}
