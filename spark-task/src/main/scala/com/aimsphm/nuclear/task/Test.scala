package com.aimsphm.nuclear.task

import java.util.Objects

import com.aimsphm.nuclear.task.entity.bo.PointBO
import com.aimsphm.nuclear.task.util.AlgoEnum

/**
 * @Package: com.aimsphm.nuclear.task
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/12/09 19:35
 * @UpdateUser: milla
 * @UpdateDate: 2020/12/09 19:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
object Test {

  def main(args: Array[String]): Unit = {
    var point1 = new PointBO("AA", "", 1, 10, 417, "portion")
    var point2 = new PointBO("AA", "daily", 1, 60, 1428, "portion")
    var point3 = new PointBO("AA", "daily", 1, 130, 666, "portion")
    var point4 = new PointBO("AA", "daily", 1, 260, 332, "portion")
    var point5 = new PointBO("AA", "weekly", 1, 780, 777, "portion")
    var point6 = new PointBO("AA", "weekly", 1, 1560, 417, "portion")
    var point7 = new PointBO("AA", "monthly", 1, 3155, 818, "portion")
    var point8 = new PointBO("AA", "monthly", 1, 6310, 410, "portion")
    var point9 = new PointBO("AA", "monthly", 1, 9461, 273, "portion")
    var point1B = new PointBO("BB", "hourly", 1, 10, 417, "portion")
    var point2B = new PointBO("BB", "daily", 1, 60, 1428, "portion")
    var point3B = new PointBO("BB", "daily", 1, 130, 666, "portion")
    var point4B = new PointBO("BB", "daily", 1, 260, 332, "portion")
    var point5B = new PointBO("BB", "weekly", 1, 780, 777, "portion")
    var point6B = new PointBO("BB", "weekly", 1, 1560, 417, "portion")
    var point7B = new PointBO("BB", "monthly", 1, 3155, 818, "portion")
    var point8B = new PointBO("BB", "monthly", 1, 6310, 410, "portion")
    var point9B = new PointBO("BB", "monthly", 1, 9461, 273, "portion")
    val list = List(point1, point2, point3, point4, point5, point6, point7, point8, point9, point1B, point2B, point3B, point4B, point5B, point6B, point7B, point8B, point9B)
    val threePointsTagConfig = list.filter(_.getAlgorithmType == AlgoEnum.THREEPOINTS)

    val nonThreePointsTagConfig = list.filter(_.getAlgorithmType != AlgoEnum.THREEPOINTS)

    val threePointsTagGroupConfig = threePointsTagConfig.groupBy(_.getSensorCode)

    val threePintsTags = threePointsTagGroupConfig.keys
    val correspondingTags: Set[String] = threePointsTagConfig.filter(_.getTargetNum == 417).map(item => if (Objects.isNull(item.getFeature)) item.getSensorCode else item.getSensorCode.concat(item.getFeature)).toSet
    println(correspondingTags)
  }
}
