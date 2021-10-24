package com.aimsphm.nuclear.report.util;

import java.util.Collection;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.report.util
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/11/16 10:51
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/16 10:51
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * 判断一个集合中所有的元素都是空
     * 只要有一个集合不是空就是非空
     *
     * @param data 集合对象
     * @return 判断结果
     */
    public static boolean checkDataIsAllNull(Object data) {
        if (Objects.isNull(data)) {
            return true;
        }
        if (data instanceof Collection) {
            Collection<Object> object = (Collection<Object>) data;
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(object)) {
                return true;
            }
            for (Object obj : object) {
                if (!checkDataIsAllNull(obj)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
