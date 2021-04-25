package com.aimsphm.nuclear.task.util

import java.util.Objects
import java.util.concurrent.{Executors, Future}

import com.aimsphm.nuclear.task.constant.Constant
import com.aimsphm.nuclear.task.entity.bo.{PointBO, QueryBO}
import com.aimsphm.nuclear.task.hbase.HbaseWideConTableReader
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer

object HBaseUtil {

  val pool = Executors.newCachedThreadPool()

  def readTagData(tags: List[String], startTs: String, endTs: String, sc: SparkContext, hbaseConfig: Configuration, table: String, cf: String): RDD[(ImmutableBytesWritable, Result)] = {
    //var results = new ArrayBuffer[Future[RDD[(String,ArrayBuffer[(Long, Double)])]]()

    var results = new ArrayBuffer[Future[RDD[(ImmutableBytesWritable, Result)]]]()
    for (tag <- tags) {
      val startKey = tag.concat(Constant.H_BASE_ROW_KEY_CONNECTOR).concat(startTs)
      val endKey = tag.concat(Constant.H_BASE_ROW_KEY_CONNECTOR).concat(endTs)
      val scanner = new HbaseWideConTableReader(sc, hbaseConfig, table, startKey, endKey, cf)

      pool.synchronized {

        val rdd: Future[RDD[(ImmutableBytesWritable, Result)]] = pool.submit(scanner)
        results += rdd

      }
    }
    var rdds: RDD[(ImmutableBytesWritable, Result)] = null

    //取出第一个RDD
    if (results.size > 0) {
      rdds = results(0).get()
    }

    //合并第二个到之后的RDD
    if (results.size >= 1) {
      results.remove(0)
      for (x <- results) {
        // println("循环" + x.get())
        rdds = rdds.union(x.get())
      }
    }
    rdds
  }

  def listPointByConditions(pointList: List[(String, ArrayBuffer[PointBO])], sc: SparkContext, config: Configuration, query: QueryBO): RDD[(ImmutableBytesWritable, Result)] = {
    var results = new ArrayBuffer[Future[RDD[(ImmutableBytesWritable, Result)]]]()
    for (item <- pointList) {
      val point = item._2(0);
      if (Objects.nonNull(point.getFeature)) {
        val featureGroup = item._2.groupBy(_.getFeature)
        for (elements <- featureGroup.toList) {
          val element = elements._2(0);
          val pointId = element.getSensorCode;
          val family = element.getFeature;
          val startKey = pointId.concat(Constant.H_BASE_ROW_KEY_CONNECTOR).concat(query.getStart)
          val endKey = pointId.concat(Constant.H_BASE_ROW_KEY_CONNECTOR).concat(query.getEnd)
          val scanner = new HbaseWideConTableReader(sc, config, query.getTableName, startKey, endKey, if (Objects.isNull(family)) query.getDefaultFamily else family)
          pool.synchronized {
            val rdd: Future[RDD[(ImmutableBytesWritable, Result)]] = pool.submit(scanner)
            results += rdd
          }
        }
      } else {
        val pointId = point.getSensorCode;
        val family = point.getFeature;
        val startKey = pointId.concat(Constant.H_BASE_ROW_KEY_CONNECTOR).concat(query.getStart)
        val endKey = pointId.concat(Constant.H_BASE_ROW_KEY_CONNECTOR).concat(query.getEnd)
        val scanner = new HbaseWideConTableReader(sc, config, query.getTableName, startKey, endKey, if (Objects.isNull(family)) query.getDefaultFamily else family)
        pool.synchronized {
          val rdd: Future[RDD[(ImmutableBytesWritable, Result)]] = pool.submit(scanner)
          results += rdd
        }
      }
    }
    var rdds: RDD[(ImmutableBytesWritable, Result)] = null
    //取出第一个RDD
    if (results.size > 0) {
      rdds = results(0).get()
    }
    //合并第二个到之后的RDD
    if (results.size >= 1) {
      results.remove(0)
      for (x <- results) {
        rdds = rdds.union(x.get())
      }
    }
    rdds
  }
}
