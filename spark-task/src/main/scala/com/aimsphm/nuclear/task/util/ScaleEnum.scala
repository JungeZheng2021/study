package com.aimsphm.nuclear.task.util

object ScaleEnum extends Enumeration {
  type scale = Int //声明枚举对外暴露的变量类型
  val DAILY = 10
  val WEEKLY = 60
  val HALF_MONTHLY = 130
  val MONTHLY = 260
  val QUARTERLY = 780
  val HALF_ANNUALLY = 1560
  val ANNUALLY = 3155
  val DOUBLE_ANNUALLY = 6310
  val TRIPLE_ANNUALLY = 9461

  def checkExists(scale: Int) = this.values.exists(_ == scale) //检测是否存在此枚举值

}



