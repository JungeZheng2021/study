package com.aimsphm.nuclear.task.hbase

import java.util.concurrent.Callable

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.CompareOperator
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.filter.{BinaryComparator, FilterList, RowFilter}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer


class HbaseWideConTableReader(sc: SparkContext, hbaseConfig:Configuration,table: String, startKey: String, endKey: String, cf: String) extends Callable[RDD[(ImmutableBytesWritable, Result)]]{
  //sc.getConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  /*val sc = spark.sparkContext
  val sparkConf = sc.getConf
  sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  sparkConf.set("spark.kryo.registrator","org.apache.hadoop.conf.Configuration,org.apache.hadoop.hbase.io.ImmutableBytesWritable,org.apache.hadoop.hbase.client.Result,org.apache.hadoop.io.LongWritable")
*/
 /* Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  lazy val logger: Logger = Logger.getLogger(this.getClass().getName())
  val properties = new Properties()
  val in = this.getClass().getClassLoader().getResourceAsStream("dev.properties");
  properties.load(in)*/
  /*val config: Configuration = {
    var hbaseConf: Configuration = null
    try {
      hbaseConf = HBaseConfiguration.create()
      hbaseConf.set("hbase.zookeeper.quorum", properties.getProperty("hbaseQuorum"))
      hbaseConf.set("hbase.zookeeper.property.clientPort", properties.getProperty("hbaseClientPort"))

    } catch {
      case e: Exception => logger.error("==========连接hbase失败:," + e)
    }
    hbaseConf
  }*/

  //def batchReadAsRDD(sc: SparkContext, hbaseConfig:Configuration,table: String, startKey: String, endKey: String, cf: String): RDD[(String,ArrayBuffer[(Long, Double)])] = { //table: String, startKey: String, endKey: String
    hbaseConfig.set(TableInputFormat.INPUT_TABLE, table)
    val startRowkey: String = startKey
    val endRowkey: String = endKey



    val scan = new Scan(Bytes.toBytes(startRowkey), Bytes.toBytes(endRowkey))
    scan.setCacheBlocks(true)
    scan.setCaching(1000)





    scan.addFamily(Bytes.toBytes(cf));

    //将scan类转化成string类型
    val scan_str = TableMapReduceUtil.convertScanToString(scan)
    hbaseConfig.set(TableInputFormat.SCAN, scan_str)
    //config.setLong("mapred.max.split.size", 4096)
    //使用new hadoop api，读取数据，并转成rdd
    val rdd = sc.newAPIHadoopRDD(hbaseConfig, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    //println("hadoop" + rdd.partitions.length)
    //val bc_cf = sc.broadcast(cf)
/*******8*
    val transformedRdd = rdd.values.map(x => {//x其实就是一行数据，此处谨慎使用MapPartition，会导致内存溢出
      val cells = x.rawCells()
      var tag:String=""
      val arr: ArrayBuffer[(Long, Double)] = ArrayBuffer[(Long, Double)]()
      // val lineTuple: Tuple2[String,ArrayBuffer[(Long, Double)]] = Tuple2[String,ArrayBuffer[(Long, Double)]]("",arr)
      for (cell <- cells) {
        //val value = Bytes.toDouble(cell.getValueArray)
        tag = Bytes.toString(cell.getRowArray,cell.getRowOffset,cell.getRowLength).split(com.aims.nuclear.spark.constant.Constant.Hbase_Rowkey_seperator)(0)
        val value: Double= Bytes.toDouble(cell.getValueArray, cell.getValueOffset)
        val ts = cell.getTimestamp
        arr += Tuple2(ts, value)
      }//一行处理完毕
      Tuple2(tag,arr)
    })
 *****/
    //println("hbase" + transformedRdd.getNumPartitions)
    //transformedRdd
  //}

  override def call(): RDD[(ImmutableBytesWritable, Result)] = {
    return rdd
  }
}

