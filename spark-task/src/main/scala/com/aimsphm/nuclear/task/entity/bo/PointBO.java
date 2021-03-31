package com.aimsphm.nuclear.task.entity.bo;

import java.io.Serializable;

/**
 * @Package: com.aimsphm.nuclear.task.entity
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/09 13:14
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/09 13:14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class PointBO implements Serializable {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -8550983655858153891L;
    /**
     * 传感器编号
     */
    private String sensorCode;
    /**
     * 特征-其实就是列族
     */
    private String feature;

    private Integer algorithmType;

    private Integer rate;

    private Integer targetNum;

    private String portion;

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Integer getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(Integer algorithmType) {
        this.algorithmType = algorithmType;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(Integer targetNum) {
        this.targetNum = targetNum;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    @Override
    public String toString() {
        return "PointBO{" +
                "sensorCode='" + sensorCode + '\'' +
                ", feature='" + feature + '\'' +
                ", algorithmType=" + algorithmType +
                ", rate=" + rate +
                ", targetNum=" + targetNum +
                ", portion='" + portion + '\'' +
                '}';
    }

    public PointBO(String sensorCode, String feature, Integer algorithmType, Integer rate, Integer targetNum, String portion) {
        this.sensorCode = sensorCode;
        this.feature = feature;
        this.algorithmType = algorithmType;
        this.rate = rate;
        this.targetNum = targetNum;
        this.portion = portion;
    }
}
