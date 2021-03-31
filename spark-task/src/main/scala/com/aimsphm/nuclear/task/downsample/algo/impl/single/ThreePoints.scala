package com.aimsphm.nuclear.task.downsample.algo.impl.single

import com.aimsphm.nuclear.task.downsample.algo.{AlgoAction, Algorithm}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable

case class ThreePoints(targetNum: Long, srcRDD: RDD[(Long, Double)], sc: SparkContext, partitionNum: Int) extends Algorithm with AlgoAction {
  override def execute(cacheable: Boolean, additionalMap: mutable.Map[String, Int]): RDD[(Long, Double)] = {

    val zipRDD: RDD[(Long, (Long, Double))] = srcRDD.zipWithIndex().map(_.swap).repartition(partitionNum)
    // println("zip"+zipRDD.partitions.length)

    // println("zipRep"+zipRDD.partitions.length)
    //zipRDD.foreach(println(_))

    val segNum = targetNum.toInt / 3
    //here calls the action, the zipRDD will be cache at executor side
    val segLen = zipRDD.count() / segNum

    val groupRDD: RDD[(Long, (Long, (Long, Double)))] = zipRDD.map(x => {
      (x._1 / segLen, x)
    }).sortBy(_._2._1)
    //groupRDD.groupByKey().sortByKey().foreach(println(_))
    val res = groupRDD.groupByKey().map(x => {
      val max = x._2.maxBy(_._2._2)
      val min = x._2.minBy(_._2._2)
      val groupId = x._1
      var start: (Long, (Long, Double)) = Tuple2(0, (0, 0))
      for (elem <- x._2) {
        if (groupId * segLen == elem._1)
          start = elem
      }

      if (min._2._1 < max._2._1) (groupId, (start, min, max)) else (groupId, (start, max, min))

    })

    val finalRdd = res.values.sortBy(_._1._1).map(x => (x._1._2, x._2._2, x._3._2)).flatMap(x => {
      x.productIterator
    })


    // println("group"+groupRDD.partitions.length)
    // println("res"+res.partitions.length)
    // println("final"+finalRdd.partitions.length)
    //println(finalRdd.count()+"is finalRDD")
    finalRdd.asInstanceOf[RDD[(Long, Double)]]
  }
}
