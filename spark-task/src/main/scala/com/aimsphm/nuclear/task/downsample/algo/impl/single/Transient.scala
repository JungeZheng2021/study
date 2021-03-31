package com.aimsphm.nuclear.task.downsample.algo.impl.single

import breeze.linalg.diff
import com.aimsphm.nuclear.task.downsample.algo.{AlgoAction, Algorithm}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class Transient(targetNum: Long, srcRDD: RDD[(Long, Double)], sc: SparkContext, partitionNum: Int) extends Algorithm with AlgoAction {
  override def execute(cacheable: Boolean, additionalParamMap: mutable.Map[String, Int]): RDD[(Long, Double)] = {
    val zipRDD: RDD[(Long, (Long, Double))] = srcRDD.zipWithIndex().map(_.swap).repartition(partitionNum)

    //check whether the RDD need to be cached in order to accelerate

    import breeze.linalg.{DenseVector => BDV}
    import org.apache.spark.mllib.linalg.{Vectors, DenseVector => SDV}

    val valueRdd = srcRDD.map(_._2)
    val dv: SDV = Vectors.dense(valueRdd.collect()).asInstanceOf[SDV]

    val r = new BDV(dv.values)
    val differedArray: Array[Double] = diff(r).toArray
    // differedArray.foreach(println(_))
    val nums: Long = targetNum / 4
    val diffRdd: RDD[(Double, Long)] = sc.parallelize(differedArray).zipWithIndex()


    val partionedRDD = diffRdd //.partitionBy(new CustPartitioner(2))
    val positiveRDD = partionedRDD.filter(_._1 >= 0).sortBy(_._1, false).top(nums.asInstanceOf[Int])

    val negativeRDD = partionedRDD.filter(_._1 < 0).top(nums.asInstanceOf[Int])((new Ordering[(Double, Long)] {
      override def compare(x: (Double, Long), y: (Double, Long)): Int = {
        Ordering[Double].compare(y._1, x._1)
      }
    }))

    val unionRdd = positiveRDD union negativeRDD
    val sortedUnionRdd = unionRdd.sortBy(_._2).map(_.swap)
    val len = sortedUnionRdd.length
    val arr: ArrayBuffer[(Long, Long)] = ArrayBuffer[(Long, Long)]()
    for (i <- 0 until len) {
      if (i != len - 1) { //last element
        val first = sortedUnionRdd(i)
        val second = sortedUnionRdd(i + 1)
        if ((first._2 < 0 && second._2 > 0) || (first._2 > 0 && second._2 < 0)) {
          arr += Tuple2(first._1, second._1)
        }
      }
      arr
    }
    // arr.foreach(println(_))
    //val zipArr = arr.zipWithIndex
    val resultRdd = zipRDD.filter(y => {
      var isTure = false
      for (elem <- arr) {
        val minIdx = elem._1
        val maxIdx = elem._2
        if (y._1 >= minIdx && y._1 <= maxIdx)
          isTure = true

      }
      isTure
    }).map(x => {

      val resArr: ArrayBuffer[(Long, (Long, Double))] = ArrayBuffer[(Long, (Long, Double))]()
      //val key = x._1
      for (elem <- arr) {
        val minIdx = elem._1
        val maxIdx = elem._2
        if (x._1 >= minIdx && x._1 <= maxIdx) {

          resArr += Tuple2(minIdx, x._2)
        }

      }
      resArr
    })
    val groupResultRDD = resultRdd.flatMap(x => x.iterator).groupByKey().sortByKey()
    val finalRDD = groupResultRDD.mapValues(x => {
      val resultArray: ArrayBuffer[(Long, Double)] = ArrayBuffer[(Long, Double)]()
      val len = x.count(x => true)
      if (len <= 4) {
        resultArray ++= x
      } else {
        val max = x.maxBy(_._2)
        val min = x.minBy(_._2)
        val last = x.last
        var first = x.head
        resultArray += first
        if (min._1 < max._1) {
          resultArray += min
          resultArray += max
        } else {
          resultArray += max
          resultArray += min
        }
        resultArray += last
      }
      resultArray
    })


    finalRDD.values.flatMap(_.iterator)
  }
}
