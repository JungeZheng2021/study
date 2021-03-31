package com.aimsphm.nuclear.task.downsample.algo

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

abstract  class Algorithm {
   val targetNum:Long
   val srcRDD:RDD[(Long,Double)]
   val sc:SparkContext
   val partitionNum:Int

}
