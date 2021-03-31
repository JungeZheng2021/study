package com.aimsphm.nuclear.task.downsample.algo.impl.single

import com.aimsphm.nuclear.task.downsample.algo.{AlgoAction, Algorithm}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable

case class FivePoints(targetNum: Long, srcRDD: RDD[(Long, Double)], sc: SparkContext, partitionNum: Int) extends Algorithm with AlgoAction {
  override def execute(cacheable: Boolean, additionalParamMap: mutable.Map[String, Int]): RDD[(Long, Double)] = {
    val zipRDD: RDD[(Long, (Long, Double))] = srcRDD.zipWithIndex().map(_.swap).repartition(partitionNum)
    //check whether the RDD need to be cached inorder to accelerate

    val segNum = targetNum.toInt / 5
    //here calls the action, the zipRDD will be cache at executor side
    val totalSize: Long = zipRDD.count()
    var segLen: Long = totalSize / segNum
    var lastSegLen: Long = 0
    if (segLen == 0) {
      segLen = totalSize
      lastSegLen = totalSize
    } else {
      lastSegLen = totalSize - segLen * segNum
    }


    //first is group， second is index, then is the real record
    val groupRDD: RDD[(Long, (Long, (Long, Double)))] = zipRDD.map(x => {
      (x._1 / segLen, x)
    }).sortBy(_._2._1)
    val res = groupRDD.groupByKey().map(x => {
      val max = x._2.maxBy(_._2._2)
      val min = x._2.minBy(_._2._2)
      val groupId = x._1
      var median: Tuple2[Long, (Long, Double)] = Tuple2(0L, (0L, 0D))
      var quater: Tuple2[Long, (Long, Double)] = Tuple2(0L, (0L, 0D))
      var thirdQuater: Tuple2[Long, (Long, Double)] = Tuple2(0L, (0L, 0D))

      for (elem <- x._2) {
        if (x._1 != segNum - 1) { //not last section
          if (groupId * segLen + (segLen * 0.25).asInstanceOf[Long] == elem._1) quater = elem
          if (groupId * segLen + (segLen * 0.5).asInstanceOf[Long] == elem._1) median = elem
          if (groupId * segLen + (segLen * 0.75).asInstanceOf[Long] == elem._1) thirdQuater = elem
        } else {
          if (groupId * segLen + (lastSegLen * 0.25).asInstanceOf[Long] == elem._1) quater = elem
          if (groupId * segLen + (lastSegLen * 0.5).asInstanceOf[Long] == elem._1) median = elem
          if (groupId * segLen + (lastSegLen * 0.75).asInstanceOf[Long] == elem._1) thirdQuater = elem
        }
      }

      (groupId, (median, min, max, quater, thirdQuater))

    })

    val finalRdd = res.values.sortBy(_._1._1).map(x => (x._1._2, x._2._2, x._3._2)).flatMap(_.productIterator)

    finalRdd.asInstanceOf[RDD[(Long, Double)]].filter(_._1 != 0L)
  }
}
