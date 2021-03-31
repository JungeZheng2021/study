package com.aimsphm.nuclear.task.hbase

import java.util.Objects

import com.aimsphm.nuclear.task.constant.Constant
import com.aimsphm.nuclear.task.entity.bo.QueryBO
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.CellUtil
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer


object HBaseWideTableReader {
  def readAsRDD(sc: SparkContext, config: Configuration, query: QueryBO): RDD[(String, ArrayBuffer[(Long, Double)])] = {
    config.set(TableInputFormat.INPUT_TABLE, query.getTableName)
    val scan = new Scan(Bytes.toBytes(query.getStart), Bytes.toBytes(query.getEnd))
    scan.setCacheBlocks(false)
    scan.addFamily(Bytes.toBytes(query.getDefaultFamily));

    //将scan类转化成string类型
    val scan_str = TableMapReduceUtil.convertScanToString(scan)
    config.set(TableInputFormat.SCAN, scan_str)
    //config.setLong("mapred.max.split.size", 4096)
    //使用new hadoop api，读取数据，并转成rdd
    val rdd = sc.newAPIHadoopRDD(config, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    //println("hadoop" + rdd.partitions.length)
    //val bc_cf = sc.broadcast(cf)

    val transformedRdd = rdd.values.map(x => { //x其实就是一行数据，此处谨慎使用MapPartition，会导致内存溢出
      val cells = x.rawCells()
      var pointId: String = ""
      val arr: ArrayBuffer[(Long, Double)] = ArrayBuffer[(Long, Double)]()
      for (cell <- cells) {
        if (Objects.isNull(pointId)) {
          pointId = Bytes.toString(CellUtil.cloneRow(cell)).split(Constant.H_BASE_ROW_KEY_CONNECTOR)(0)
          val family = Bytes.toString(CellUtil.cloneFamily(cell))
          //非Pi测点需要拼接pointId
          if (!family.equals(QueryBO.DEFAULT_COLUMN_FAMILY)) {
            pointId = pointId.concat(Constant.MIDDLE_LINE_CONNECTOR).concat(family)
          }
        }
        val value: Double = Bytes.toDouble(cell.getValueArray, cell.getValueOffset)
        val ts = cell.getTimestamp
        arr += Tuple2(ts, value)
      } //一行处理完毕
      Tuple2(pointId, arr)
    })
    transformedRdd
  }
}

