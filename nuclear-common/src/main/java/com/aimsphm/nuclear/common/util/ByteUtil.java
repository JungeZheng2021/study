package com.aimsphm.nuclear.common.util;


import com.aimsphm.nuclear.common.exception.CustomMessageException;

import java.io.*;

/**
 * @Package: com.aimsphm.nuclear.hbase.util
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/3/6 11:53
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/6 11:53
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class ByteUtil {

    /**
     * 对象转数组
     *
     * @param obj 对象
     * @return
     */
    public static byte[] toBytes(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            throw new CustomMessageException(e);
        }
        return bytes;
    }

    /**
     * 数组转对象
     *
     * @param bytes 字节数据
     * @return
     */
    public static Object toObject(byte[] bytes) {
        Object obj;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException e) {
            throw new CustomMessageException(e);
        } catch (ClassNotFoundException e) {
            throw new CustomMessageException("Unable to find object class.", e);
        }
        return obj;
    }
}
