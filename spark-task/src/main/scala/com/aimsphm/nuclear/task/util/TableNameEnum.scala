package com.aimsphm.nuclear.task.util

object TableNameEnum extends Enumeration {
  type Category = String //声明枚举对外暴露的变量类型
  /**
   * 天表
   */
  val DAILY = "spark_down_sample_daily"
  /**
   * 周表
   */
  val WEEKLY = "spark_down_sample_weekly"
  /**
   * 半月表
   */
  val HALF_MONTHLY = "spark_down_sample_half_monthly"
  /**
   * 月表
   */
  val MONTHLY = "spark_down_sample_monthly"
  /**
   * 季度表
   */
  val QUARTERLY = "spark_down_sample_quarterly"
  /**
   * 半年表
   */
  val HALF_ANNUALLY = "spark_down_sample_half_annually"
  /**
   * 一年表
   */
  val ANNUALLY = "spark_down_sample_annually"
  /**
   * 两年表
   */
  val DOUBLE_ANNUALLY = "spark_down_sample_double_annually"
  /**
   * 三年表
   */
  val TRIPLE_ANNUALLY = "spark_down_sample_triple_annually"

  def checkExists(category: String) = this.values.exists(_.toString == category) //检测是否存在此枚举值

  def getTableName(rate: Int): String = rate match {

    case ScaleEnum.DAILY => this.DAILY
    case ScaleEnum.WEEKLY => this.WEEKLY
    case ScaleEnum.HALF_MONTHLY => this.HALF_MONTHLY
    case ScaleEnum.MONTHLY => this.MONTHLY
    case ScaleEnum.QUARTERLY => this.QUARTERLY
    case ScaleEnum.HALF_ANNUALLY => this.HALF_ANNUALLY
    case ScaleEnum.ANNUALLY => this.ANNUALLY
    case ScaleEnum.DOUBLE_ANNUALLY => this.DOUBLE_ANNUALLY
    case ScaleEnum.TRIPLE_ANNUALLY => this.TRIPLE_ANNUALLY
  }

}



