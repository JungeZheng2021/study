package com.aimsphm.nuclear.task.downsample.algo

import org.apache.spark.rdd.RDD

import scala.collection.mutable

trait AlgoAction {

  def execute(cacheable:Boolean,additionalParamMap:mutable.Map[String,Int]=mutable.Map()):RDD[(Long, Double)]
}
