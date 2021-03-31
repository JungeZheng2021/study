package com.aimsphm.nuclear.task.downsample.algo.impl.group

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object ThreePointsGroupWiseReduceBy {
  def execute(cacheable: Boolean, targetNum: Long, src: RDD[(String, ArrayBuffer[(Long, Double)])], sc: SparkContext) = {
    //val srcRDD=sc.parallelize(src.toList)
    val zipRDD: RDD[(String, Iterable[(Int, (Long, Double))])] = src.mapValues(x => x.zipWithIndex.map(_.swap))
    // println("zip"+zipRDD.partitions.length)

    // println("zipRep"+zipRDD.partitions.length)
    //check whether the RDD need to be cached inorder to accelerate
   /* if (cacheable) {
      zipRDD.persist(StorageLevel.MEMORY_AND_DISK_SER)
    }*/

    //zipRDD.count()
    val segNum = sc.broadcast(targetNum.toInt / 3)
    val groupRDD: RDD[(String, Iterable[(Int, Int, Long, Double, Int)])] = zipRDD.mapValues(x => {


      //here calls the action, the zipRDD will be cache at executor side
      var segLen = 0

      segLen = x.size / segNum.value
      if (segLen == 0) {
        segLen = x.size
      }


      //println("senLen="+segLen)
      val res = x.map(el => (el._1 / segLen, el._1, el._2._1, el._2._2, segLen))
      res
    })


    val result = groupRDD.mapValues(x => {

      val grouped= x.groupBy(_._1)

      grouped.map(y => {
        val min = y._2.minBy(_._4)
        val max = y._2.maxBy(_._4)
        val groupId = y._1
        var start: (Int, Int, Long, Double, Int) = Tuple5(0, 0, 0L, 0.0, 0)

        for (elem <- y._2) {
          if (groupId * elem._5 == elem._2)
            start = elem
        }
        if (min._3 < max._3) (start, min, max) else (start, max, min)
      })
    })
    // println("result count="+result.count())
    // println("result parti="+result.getNumPartitions)
    // result.foreach(println(_))


    val finalRdd = result.flatMapValues(x => x.iterator).mapValues(y => {
      val arr: mutable.ArrayBuffer[(Int, Long, Double)] = mutable.ArrayBuffer[(Int, Long, Double)]()
      arr += Tuple3(y._1._1, y._1._3, y._1._4)
      arr += Tuple3(y._2._1, y._2._3, y._2._4)
      arr += Tuple3(y._3._1, y._3._3, y._3._4)
      arr
    }
    ).flatMapValues(_.iterator)

    // println("final count="+finalRdd.count())
    //println("final parti="+finalRdd.getNumPartitions)
    //finalRdd.foreach(println(_))
    //finalRdd.foreach(println(_))
   /* if (cacheable) {
      zipRDD.unpersist(true)
    }*/
    finalRdd
  }
}
