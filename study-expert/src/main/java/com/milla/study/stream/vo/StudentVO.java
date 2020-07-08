package com.milla.study.stream.vo;

import lombok.Data;

/**
 * @Package: com.milla.study.stream.vo
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/23 11:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/23 11:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class StudentVO {

    private String username;

    private String city;

    private String sex;

    private int age;

    public StudentVO(String username, String city, String sex, int age) {
        this.username = username;
        this.city = city;
        this.sex = sex;
        this.age = age;
    }

    public StudentVO() {
    }
}
