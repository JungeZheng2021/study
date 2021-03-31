package com.aimsphm.nuclear.task.util

object AlgoEnum extends Enumeration {
  /*type ALgo = String //声明枚举对外暴露的变量类型
  val THREEPOINTS = "threePoints"
  val FIVEPOINTS = "fivePoints"
  val STEPWISE = "stepwise"
  val TRANSIENT = "transient"*/
  type algo = Int //声明枚举对外暴露的变量类型
  val THREEPOINTS = 1
  val FIVEPOINTS = 2
  val STEPWISE = 3
  val TRANSIENT = 4
  val COMBINED = 5

  def checkExists(algo: Int) = this.values.exists(_ == algo) //检测是否存在此枚举值

}



