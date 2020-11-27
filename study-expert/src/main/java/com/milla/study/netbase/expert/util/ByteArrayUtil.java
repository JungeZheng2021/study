package com.milla.study.netbase.expert.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Package: com.study.auth.config.core.handler
 * @Description: <对象和二进制数组及流对象间的转换>
 * @Author: milla
 * @CreateDate: 2020/09/09 09:52
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/09 09:52
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class ByteArrayUtil {
    public static void main(String[] args) {
        Student student = new Student("冯程程", "上海金融大街", 23, "女");
        byte[] array = toByteArray(student);
        System.out.println("字节数组：" + Arrays.toString(array));
        ByteArrayOutputStream arrayOutputStream = toOutputStream(student);
        System.out.println("输出流：　" + arrayOutputStream);

        array = new byte[]{-84, -19, 0, 5, 115, 114, 0, 42, 99, 111, 109, 46, 115, 116, 117, 100, 121, 46, 97, 117, 116, 104, 46, 99, 111, 110, 102, 105, 103, 46, 99, 111, 114, 101, 46, 104, 97, 110, 100, 108, 101, 114, 46, 83, 116, 117, 100, 101, 110, 116, 108, -107, 107, -81, -49, 49, 110, -103, 2, 0, 5, 76, 0, 7, 97, 100, 100, 114, 101, 115, 115, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 0, 3, 97, 103, 101, 116, 0, 19, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 73, 110, 116, 101, 103, 101, 114, 59, 76, 0, 4, 110, 97, 109, 101, 113, 0, 126, 0, 1, 76, 0, 3, 115, 101, 120, 113, 0, 126, 0, 1, 76, 0, 4, 115, 101, 120, 49, 113, 0, 126, 0, 1, 120, 112, 116, 0, 18, -28, -72, -118, -26, -75, -73, -23, -121, -111, -24, -98, -115, -27, -92, -89, -24, -95, -105, 115, 114, 0, 17, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 73, 110, 116, 101, 103, 101, 114, 18, -30, -96, -92, -9, -127, -121, 56, 2, 0, 1, 73, 0, 5, 118, 97, 108, 117, 101, 120, 114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108, -32, -117, 2, 0, 0, 120, 112, 0, 0, 0, 23, 116, 0, 9, -27, -122, -81, -25, -88, -117, -25, -88, -117, 116, 0, 3, -27, -91, -77, 112};
        Student obj = toObject(array, Student.class);
        Student obj1 = toObject(arrayOutputStream, Student.class);

        System.out.println("字节数组转换成对象：" + obj);
    }

    /**
     * 将ByteArrayOutputStream输出流转换成对象
     *
     * @param outputStream 输出流
     * @param clazz        要转话程的对象class文件
     * @param <T>          要转化的对象
     * @return
     */
    public static <T> T toObject(ByteArrayOutputStream outputStream, Class<T> clazz) {
        if (Objects.isNull(outputStream)) {
            log.error("outputStream array can not be null");
            return null;
        }
        byte[] data = outputStream.toByteArray();
        return toObject(data, clazz);
    }

    /**
     * 将输入流转换成对象
     *
     * @param inputStream 输入流
     * @param clazz       要转换成对象的class文件
     * @param <T>         要转换程的对象类型
     * @return
     */
    public static <T> T toObject(InputStream inputStream, Class<T> clazz) {
        if (Objects.isNull(inputStream)) {
            log.error("inputStream array can not be null");
            return null;
        }
        ObjectInputStream obj = null;
        try {
            obj = new ObjectInputStream(inputStream);
            return (T) obj.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("byte array to {} failed: {}", clazz.getName(), e);
            return null;
        } finally {
            try {
                if (Objects.nonNull(inputStream)) {
                    inputStream.close();
                }
                if (Objects.nonNull(obj)) {
                    obj.close();
                }
            } catch (IOException e) {
                log.error("stream close failed: {}", e);
            }

        }
    }

    /**
     * 将byte数组转换成对象
     *
     * @param array byte数组
     * @param clazz 要转成的对象字节码
     * @param <T>   要转成的类型
     * @return
     */
    public static <T> T toObject(byte[] array, Class<T> clazz) {
        if (Objects.isNull(array) || array.length == 0) {
            log.error("byte array can not be null");
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(array);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bis);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("byte array to {} failed: {}", clazz.getName(), e);
            return null;
        } finally {
            try {
                if (Objects.nonNull(bis)) {
                    bis.close();
                }
                if (Objects.nonNull(ois)) {
                    ois.close();
                }
            } catch (IOException e) {
                log.error("stream close failed: {}", e);
            }

        }
    }

    /**
     * 将对象抓换成二进制对象
     * Ps：对象必须实现Serializable接口,最好能给定一个serialVersionUID
     *
     * @param data 对象
     * @return
     */
    public static byte[] toByteArray(Object data) {
        ByteArrayOutputStream outputStream = toOutputStream(data);
        if (Objects.isNull(outputStream)) {
            return null;
        }
        return outputStream.toByteArray();
    }

    /**
     * 将对象转换成输出流
     * Ps：对象必须实现Serializable接口,最好能给定一个serialVersionUID
     *
     * @param data 对象
     * @return
     */
    public static ByteArrayOutputStream toOutputStream(Object data) {
        if (Objects.isNull(data)) {
            log.error("{} can not be null", data.getClass().getName());
            return null;
        }
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        ObjectOutputStream objectWrite = null;
        try {
            objectWrite = new ObjectOutputStream(array);
            objectWrite.writeObject(data);
            objectWrite.flush();
            return array;
        } catch (IOException e) {
            log.error("{} to byte array failed: {}", data.getClass().getName(), e);
            return null;
        } finally {
            try {
                if (Objects.nonNull(array)) {
                    array.close();
                }
                if (Objects.nonNull(objectWrite)) {
                    objectWrite.close();
                }
            } catch (IOException e) {
                log.error("stream close failed: {}", e);
            }
        }
    }

}

@Data
class Student implements Serializable {
    /**
     * 序列化/反序列化必须实现Serializable接口
     * 想要不出现类型转换异常就需要有一个统一的serialVersionUID
     */
    private static final long serialVersionUID = 7824278330465676953L;

    private String name;
    private String address;
    private Integer age;
    private String sex;

    public Student(String name, String address, Integer age, String sex) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.sex = sex;
    }
}