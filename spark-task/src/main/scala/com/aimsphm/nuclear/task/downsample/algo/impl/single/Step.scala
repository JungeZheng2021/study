package com.aimsphm.nuclear.task.downsample.algo.impl.single

import com.aimsphm.nuclear.task.downsample.algo.{AlgoAction, Algorithm}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class Step(targetNum: Long, srcRDD: RDD[(Long, Double)], sc: SparkContext, partitionNum: Int) extends Algorithm with AlgoAction {
  override def execute(cacheable: Boolean, additionalParamMap: mutable.Map[String, Int]): RDD[(Long, Double)] = {
    val zipRDD: RDD[(Long, (Long, Double))] = srcRDD.zipWithIndex().map(_.swap).repartition(partitionNum)
    //check whether the RDD need to be cached in order to accelerate
    var m1: Int = 0
    var m2: Int = 0
    if (!additionalParamMap.isEmpty) {
      m1 = additionalParamMap.getOrElse("m1", 5)
      m2 = additionalParamMap.getOrElse("m2", 10)
    }

    val totalLen: Long = zipRDD.count()
    var sectionLen: Long = 0
    if (sectionLen == 0) {
      sectionLen = totalLen
    } else {
      sectionLen = totalLen / m1
    }

    val T2 = targetNum / m1
    val groupRDD: RDD[(Long, (Long, (Long, Double)))] = zipRDD.map(x => {
      (x._1 / sectionLen, x)
    }).sortBy(_._2._1)
    val lastSegLen = totalLen - sectionLen * m1
    val sectionMinMaxRangeRdd = groupRDD.groupByKey().map(x => {
      val max = x._2.maxBy(_._2._2)
      val min = x._2.minBy(_._2._2)
      val groupId = x._1
      (groupId, (min._2._2, max._2._2))
    }).mapValues(x => {
      val min = x._1
      val max = x._2
      val difference = math.abs(max - min)
      val step = difference / m2
      val resArray: ArrayBuffer[(Double, Double)] = ArrayBuffer()
      for (i <- 0 until m2) {
        resArray += Tuple2(min + step * i, min + step * (i + 1))
      }
      resArray
    }
    ).sortByKey()
    //result will be like (493,Map(68 -> List((1537049260000,150.7991), (1537049261000,150.5814)...,List((xxx,xxx),(xxx,xxx)
    val joinedRdd = groupRDD.map(x => (x._1, x._2._2)).groupByKey().join(sectionMinMaxRangeRdd)
    val calculatedRdd = joinedRdd.mapValues(x => {
      val rawDatas = x._1 //iter
      val ranges = x._2 //bufferArray
      val tempMap: mutable.Map[Int, Iterable[(Long, Double)]] = mutable.Map()
      for (elem <- ranges) {
        val filtered = rawDatas.filter(y => y._2 >= elem._1 && y._2 < elem._2)
        tempMap += (filtered.count(e => true) -> filtered)
      }
      tempMap
    })

    val filterRdd = calculatedRdd.mapValues(x => {
      val r = T2 / m2
      val keys = x.keys
      var resIter: Iterable[(Long, Double)] = Iterable[(Long, Double)]()

      for (key <- keys) {
        val raws = x.get(key).get
        if (key <= r) {
          resIter ++= raws
        } else {
          val targetStep: Long = key / r
          resIter ++= raws.zipWithIndex.filter(_._2 % targetStep == 0).map(_._1)
        }
      }
      resIter
    }).sortByKey()


    val finalRdd = filterRdd.map(x => x._2).flatMap(y => y.iterator)
    //finalRdd.foreach(println(_))
    //println(finalRdd.count())

    finalRdd.asInstanceOf[RDD[(Long, Double)]]
  }
}
