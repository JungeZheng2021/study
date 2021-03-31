package com.aimsphm.nuclear.task.hbase

import java.util.Properties

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession


object HbaseTallTableReader {
  //sc.getConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  /*val sc = spark.sparkContext
  val sparkConf = sc.getConf
  sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  sparkConf.set("spark.kryo.registrator","org.apache.hadoop.conf.Configuration,org.apache.hadoop.hbase.io.ImmutableBytesWritable,org.apache.hadoop.hbase.client.Result,org.apache.hadoop.io.LongWritable")
*/
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  lazy val logger: Logger = Logger.getLogger(this.getClass().getName())
  val properties = new Properties()
  val in = this.getClass().getClassLoader().getResourceAsStream("dev.properties");
  properties.load(in)
  val config: Configuration = {
    var hbaseConf: Configuration = null
    try {
      hbaseConf = HBaseConfiguration.create()
      hbaseConf.set("hbase.zookeeper.quorum", properties.getProperty("hbaseQuorum"))
      hbaseConf.set("hbase.zookeeper.property.clientPort", properties.getProperty("hbaseClientPort"))

    } catch {
      case e: Exception => logger.error("==========连接hbase失败:," + e)
    }
    hbaseConf
  }

  def readAsRDD(sc: SparkContext, table: String, startKey: String, endKey: String, cf: String, col: String = ""): RDD[(Long, Double)] = { //table: String, startKey: String, endKey: String
    config.set(TableInputFormat.INPUT_TABLE, table)
    val startRowkey: String = startKey
    val endRowkey: String = endKey
    val scan = new Scan(Bytes.toBytes(startRowkey), Bytes.toBytes(endRowkey))
    scan.setCacheBlocks(false)

    scan.addFamily(Bytes.toBytes(cf));
    if (col != "") {
      scan.addColumn(Bytes.toBytes(cf), Bytes.toBytes(col))
    }
    //将scan类转化成string类型
    val scan_str = TableMapReduceUtil.convertScanToString(scan)
    config.set(TableInputFormat.SCAN, scan_str)
    //config.setLong("mapred.max.split.size", 4096)
    //使用new hadoop api，读取数据，并转成rdd
    val rdd = sc.newAPIHadoopRDD(config, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    //println("hadoop" + rdd.partitions.length)
    //rdd.getClass
    //rdd.repartition(6)
    val bc_cf = sc.broadcast(cf)
    val bc_c = sc.broadcast(col)
    //println("start--------------------")
    val transformedRdd = rdd.values.map(x => {
      val cf=Bytes.toBytes(bc_cf.value)
      val col = Bytes.toBytes(bc_c.value)
      val cell = x.getColumnLatestCell(cf, col)
      val ts = cell.getTimestamp
      val value = Bytes.toDouble(x.getValue(cf, col))
      (ts, value)
    })
    //println("hbase" + transformedRdd.getNumPartitions)
    transformedRdd
  }


}

