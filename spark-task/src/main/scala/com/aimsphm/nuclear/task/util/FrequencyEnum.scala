package com.aimsphm.nuclear.task.util

object FrequencyEnum extends Enumeration {

  type freq = String //声明枚举对外暴露的变量类型
  val HOURLY = "hourly"
  val DAILY = "daily"
  val WEEKLY = "weekly"
  val HALF_MONTHLY = "half_monthly"
  val MONTHLY = "monthly"

  def checkExists(freq: String) = this.values.exists(_.toString == freq) //检测是否存在此枚举值

}



