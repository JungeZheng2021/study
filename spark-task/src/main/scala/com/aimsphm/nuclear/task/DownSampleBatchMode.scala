package com.aimsphm.nuclear.task

import java.sql.Date
import java.util.{Calendar, Objects, Properties}

import com.aimsphm.nuclear.task.constant.Constant
import com.aimsphm.nuclear.task.downsample.algo.impl
import com.aimsphm.nuclear.task.downsample.algo.impl.group.ThreePointsGroupWise
import com.aimsphm.nuclear.task.entity.bo.{PointBO, QueryBO}
import com.aimsphm.nuclear.task.hbase.HBaseWideTableReader
import com.aimsphm.nuclear.task.util._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object DownSampleBatchMode {


  def main(args: Array[String]): Unit = {

    val properties = new Properties()
    //                 类型     开始时间          结束时间          HBase表格名称     列族                   操作时间                           最大个数
    //    val args = Array("daily", "1607064602992", "1607129478018", "npc_phm_data", "pRaw", "1", "5", "5", System.currentTimeMillis() + "", "30")
    val in = DownSampleBatchMode.getClass.getClassLoader().getResourceAsStream("prod.properties");
    properties.load(in)
    val defaultPartitionNum = "4"
    val freq = args(0)
    val starrTime = args(1)
    val endTime = args(2)
    val tableName = args(3)
    val columnFamily = args(4)
    val five = args(5)
    val six = args(6)
    val seven = args(7)
    val operateTime = args(8)
    val maxBatchNumber = args(9).toInt
    val query = new QueryBO(starrTime, endTime, tableName, columnFamily);
    val creatTime: Date = new java.sql.Date(operateTime.toLong)
    val appName = freq + "_" + creatTime.toString
    val spark = SparkSession.builder()
      .appName(appName).config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      //      .master("local")
      .config("spark.kryoserializer.buffer.max", "1024m").config("spark.kryo.classesToRegister", "org.apache.hadoop.hbase.client.Result,org.apache.hadoop.hbase.HBaseConfiguration,org.apache.hadoop.conf.Configuration")
      .getOrCreate()

    val sc = spark.sparkContext

    val calendar = Calendar.getInstance

    calendar.setTime(creatTime)
    val year = calendar.get(Calendar.YEAR)

    val currentYear_bc = sc.broadcast(year + "")


    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    lazy val logger: Logger = Logger.getLogger(this.getClass().getName())


    /**
     * HBase Config
     */
    val config: Configuration = {
      var config: Configuration = null
      try {
        config = HBaseConfiguration.create()
        System.setProperty("zookeeper.sasl.client", "false")
        config.set("hbase.zookeeper.quorum", properties.getProperty("hbaseQuorum"))
        config.set("hbase.zookeeper.property.clientPort", properties.getProperty("hbaseClientPort"))
      } catch {
        case e: Exception => logger.error("==========连接hbase失败:," + e)
      }
      config
    }

    /**
     * mySql config
     */

    val mysqlUrl = properties.getProperty("mysqlUrl")
    val mysqlUser = properties.getProperty("mysqlUser")
    val mysqlPassword = properties.getProperty("mysqlPassword")
    println(mysqlUrl)
    println(mysqlUser)
    println(mysqlPassword)
    val tagConfig = Utils.listPointIdList(mysqlUrl, mysqlUser, mysqlPassword, freq)
    val rowKeySeparator = Constant.H_BASE_ROW_KEY_CONNECTOR

    val threePointsTagConfig = tagConfig.filter(_.getAlgorithmType == AlgoEnum.THREEPOINTS)

    val nonThreePointsTagConfig = tagConfig.filter(_.getAlgorithmType != AlgoEnum.THREEPOINTS)

    val threePointsTagGroupConfig = threePointsTagConfig.groupBy(_.getSensorCode)

    val sensorList = threePointsTagGroupConfig.toList
    val threePintsTags = threePointsTagGroupConfig.keys

    /**
     * 获取HBase中的数据
     *
     * @param pointList
     * @return
     */
    def queryHistoryData(pointList: List[(String, ArrayBuffer[PointBO])]): RDD[(String, ArrayBuffer[(Long, Double)])] = {
      val recordList: RDD[(ImmutableBytesWritable, Result)] = HBaseUtil.listPointByConditions(pointList, sc, config, query)
      val transformedRdd = recordList.values.map(x => { //x其实就是一行数据，此处谨慎使用MapPartition，会导致内存溢出
        val cells = x.rawCells()
        var pointId: String = null
        var resultList: ArrayBuffer[(Long, Double)] = ArrayBuffer[(Long, Double)]()
        for (cell <- cells) {
          if (Objects.isNull(pointId)) {
            pointId = Bytes.toString(CellUtil.cloneRow(cell)).split(Constant.H_BASE_ROW_KEY_CONNECTOR)(0)
            val family = Bytes.toString(CellUtil.cloneFamily(cell))
            //非Pi测点需要拼接pointId
            if (!family.equals(QueryBO.DEFAULT_COLUMN_FAMILY)) {
              pointId = pointId.concat(Constant.MIDDLE_LINE_CONNECTOR).concat(family)
            }
          }
          val value: Double = Bytes.toDouble(CellUtil.cloneValue(cell))
          val ts = cell.getTimestamp
          resultList += Tuple2(ts, value)
        } //一行处理完毕
        Tuple2(pointId, resultList)
      }).reduceByKey((a, b) => {
        if (a(0)._1 < b(0)._1) {
          a ++ b
        } else {
          b ++ a
        }
      })
      transformedRdd
    }

    //获取所有的目标数据量
    val targetNumberGroup = threePointsTagConfig.groupBy(_.getTargetNum)

    if (threePintsTags.size <= maxBatchNumber) {
      val transformedRdd: RDD[(String, ArrayBuffer[(Long, Double)])] = queryHistoryData(threePointsTagGroupConfig.toList).filter(_._2.length != 0)
      val partitionedRDD = transformedRdd.partitionBy(new CustPartitioner(threePintsTags.size)).persist(StorageLevel.MEMORY_AND_DISK_SER)
      for (targetN <- targetNumberGroup.keys) {
        val rate: Int = targetNumberGroup.get(targetN).get(0).getRate
        val correspondingTags: Set[String] = threePointsTagConfig.filter(_.getTargetNum == targetN).map(item => if (Objects.isNull(item.getFeature)) item.getSensorCode else item.getSensorCode.concat(Constant.MIDDLE_LINE_CONNECTOR).concat(item.getFeature)).toSet
        val toDoRdd = partitionedRDD.filter(x => correspondingTags.contains(x._1))
        if (!toDoRdd.partitions.isEmpty) {
          val nonEmptyRDD = toDoRdd.filter(_._2.length != 0)
          val ThreePointsResult: RDD[(String, (Int, Long, Double))] = ThreePointsGroupWise.execute(true, targetN, nonEmptyRDD, sc)
          val convertRdd: RDD[(String, (Long, Double))] = ThreePointsResult.map(x => (x._1, (x._2._2, x._2._3)))
          val toWriteRDD = Utils.easyToWriteRddWithTag(convertRdd)
          var tableName = TableNameEnum.getTableName(rate)

          //此处考虑是否是天表，天表则需要分表
          if (tableName.equalsIgnoreCase("spark_down_sample_daily")) {
            tableName = tableName.concat("_").concat(currentYear_bc.value)
          }
          toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String
            Utils.GroupRddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, AlgoEnum.THREEPOINTS, creatTime)
          })
        }
      }
      partitionedRDD.unpersist(false)
    } else {
      //需要循环的次数
      val loop: Int = (threePintsTags.size / maxBatchNumber)
      //最后剩余的个数
      val lastBatchRemainNumber: Int = threePintsTags.size % maxBatchNumber
      for (i <- 0 to loop) {
        if (i < loop) {
          val tags = sensorList.slice(i * maxBatchNumber, i * maxBatchNumber + maxBatchNumber) //左闭右开
          val partialRdd: RDD[(String, ArrayBuffer[(Long, Double)])] = queryHistoryData(tags.toList).filter(_._2.length != 0)
          val partitionedRDD = partialRdd.partitionBy(new CustPartitioner(maxBatchNumber)).persist(StorageLevel.MEMORY_AND_DISK_SER)
          for (targetN <- targetNumberGroup.keys) {
            val rate: Int = targetNumberGroup.get(targetN).get(0).getRate
            val correspondingTags: Set[String] = threePointsTagConfig.filter(_.getTargetNum == targetN).map(item => if (Objects.isNull(item.getFeature)) item.getSensorCode else item.getSensorCode.concat(Constant.MIDDLE_LINE_CONNECTOR).concat(item.getFeature)).toSet
            val toDoRdd = partitionedRDD.filter(x => correspondingTags.contains(x._1))
            if (!toDoRdd.partitions.isEmpty) {
              val threePointsAlgo = ThreePointsGroupWise
              val nonEmptyRDD = toDoRdd.filter(_._2.length != 0)
              val ThreePointsResult: RDD[(String, (Int, Long, Double))] = threePointsAlgo.execute(true, targetN, nonEmptyRDD, sc)
              val convertRdd: RDD[(String, (Long, Double))] = ThreePointsResult.map(x => (x._1, (x._2._2, x._2._3)))
              val toWriteRDD = Utils.easyToWriteRddWithTag(convertRdd)
              var tableName = TableNameEnum.getTableName(rate)

              //此处考虑是否是天表，天表则需要分表
              if (tableName.equalsIgnoreCase("spark_down_sample_daily")) {
                tableName = tableName.concat("_").concat(currentYear_bc.value)
              }
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String
                Utils.GroupRddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, AlgoEnum.THREEPOINTS, creatTime)
              })
            }
          }
          partitionedRDD.unpersist(false)
        } else { //i==loop
          val pointList = sensorList.slice(i * maxBatchNumber, i * maxBatchNumber + lastBatchRemainNumber)
          val partialRdd: RDD[(String, ArrayBuffer[(Long, Double)])] = queryHistoryData(pointList.toList).filter(_._2.length != 0)
          val partitionedRDD = partialRdd.partitionBy(new CustPartitioner(lastBatchRemainNumber)).persist(StorageLevel.MEMORY_AND_DISK_SER)
          for (targetN <- targetNumberGroup.keys) {
            val rate: Int = targetNumberGroup.get(targetN).get(0).getRate
            val correspondingTags: Set[String] = threePointsTagConfig.filter(_.getTargetNum == targetN).map(item => if (Objects.isNull(item.getFeature)) item.getSensorCode else item.getSensorCode.concat(Constant.MIDDLE_LINE_CONNECTOR).concat(item.getFeature)).toSet
            val toDoRdd = partitionedRDD.filter(x => correspondingTags.contains(x._1))
            if (!toDoRdd.partitions.isEmpty) {
              val nonEmptyRDD = toDoRdd.filter(_._2.length != 0)
              val threePointsAlgo = ThreePointsGroupWise
              val ThreePointsResult: RDD[(String, (Int, Long, Double))] = threePointsAlgo.execute(true, targetN, nonEmptyRDD, sc)
              val convertRdd: RDD[(String, (Long, Double))] = ThreePointsResult.map(x => (x._1, (x._2._2, x._2._3)))
              val toWriteRDD = Utils.easyToWriteRddWithTag(convertRdd)
              var tableName = TableNameEnum.getTableName(rate)

              //此处考虑是否是天表，天表则需要分表
              if (tableName.equalsIgnoreCase("spark_down_sample_daily")) {
                tableName = tableName.concat("_").concat(currentYear_bc.value)
              }
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String
                Utils.GroupRddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, AlgoEnum.THREEPOINTS, creatTime)
              })
            }
          }
          partitionedRDD.unpersist(false)
        }
      }
    }


    val groupConfig = nonThreePointsTagConfig.groupBy(_.getSensorCode)
    for (item <- groupConfig.toList) {
      val ele = item._1;
      val point = item._2(0);
      val startKey = ele.concat(rowKeySeparator).concat(starrTime)
      val endKey = ele.concat(rowKeySeparator).concat(endTime)
      val query = new QueryBO(startKey, endKey, tableName);
      var pointId: String = ele
      if (Objects.nonNull(point.getFeature) && point.getFeature.length > 0) {
        query.setDefaultFamily(point.getFeature)
        pointId = ele.concat(Constant.MIDDLE_LINE_CONNECTOR).concat(point.getFeature)
      }
      val transformedRdd = HBaseWideTableReader.readAsRDD(sc: SparkContext, config, query).values.flatMap(_.iterator)

      val toDoList: ArrayBuffer[PointBO] = groupConfig.get(ele).get

      /**
       * cache
       */
      if (!transformedRdd.partitions.isEmpty) {
        transformedRdd.persist(StorageLevel.MEMORY_AND_DISK_SER)

        for (toDo <- toDoList) {
          val algo: Int = toDo.getAlgorithmType
          algo match {
            case AlgoEnum.FIVEPOINTS => {
              val targetedNum: Int = toDo.getTargetNum
              val fivePointsAlgo = impl.single.FivePoints(targetedNum.toLong, transformedRdd, sc, five.toInt)
              val fivePointsResult = fivePointsAlgo.execute(true)
              val toWriteRDD = Utils.easyToWriteRdd(fivePointsResult)
              var tableName = TableNameEnum.getTableName(toDo.getRate)
              //此处考虑是否是天表，天表则需要分表
              if (tableName.equalsIgnoreCase("spark_down_sample_daily")) {
                tableName = tableName.concat("_").concat(currentYear_bc.value)
              }
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String
                Utils.RddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, pointId, algo, creatTime)
              })
            }
            case AlgoEnum.STEPWISE => {
              val targetedNum: Int = toDo.getTargetNum
              val stepPointsAlgo = impl.single.Step(targetedNum.toLong, transformedRdd, sc, five.toInt)
              val addtionalParamMap: mutable.Map[String, Int] = mutable.Map[String, Int]()
              addtionalParamMap += ("m1" -> six.toInt)
              addtionalParamMap += ("m2" -> seven.toInt)
              val stepResult = stepPointsAlgo.execute(true, addtionalParamMap)

              val toWriteRDD = Utils.easyToWriteRdd(stepResult)
              var tableName = TableNameEnum.getTableName(toDo.getRate)

              //此处考虑是否是天表，天表则需要分表
              if (tableName.equalsIgnoreCase("spark_down_sample_daily")) {

                tableName = tableName.concat("_").concat(currentYear_bc.value)
              }
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String

                Utils.RddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, pointId, algo, creatTime)
              })
            }
            case AlgoEnum.TRANSIENT => {
              val targetedNum: Int = toDo.getTargetNum
              val transientAlgo = impl.single.Transient(targetedNum.toLong, transformedRdd, sc, five.toInt)
              val transientResult = transientAlgo.execute(true)
              val toWriteRDD = Utils.easyToWriteRdd(transientResult)
              var tableName = TableNameEnum.getTableName(toDo.getRate)

              //此处考虑是否是天表，天表则需要分表
              if (tableName.equalsIgnoreCase("spark_down_sample_daily")) {
                tableName = tableName.concat("_").concat(currentYear_bc.value)
              }
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String

                Utils.RddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, pointId, algo, creatTime)
              })
            }

            case AlgoEnum.COMBINED => {
              val targetedNum: Int = toDo.getTargetNum
              val portion: Array[String] = toDo.getPortion.split(":")
              val stepPortion: Int = portion(0).toInt
              val transientPortion: Int = portion(1).toInt
              val stepTargetN: Int = targetedNum * stepPortion / (stepPortion + transientPortion)
              val transientTargetN: Int = targetedNum * transientPortion / (stepPortion + transientPortion)
              val stepPointsAlgo = impl.single.Step(stepTargetN, transformedRdd, sc, five.toInt)
              val addtionalParamMap: mutable.Map[String, Int] = mutable.Map[String, Int]()
              addtionalParamMap += ("m1" -> six.toInt)
              addtionalParamMap += ("m2" -> seven.toInt)
              val stepResult = stepPointsAlgo.execute(true, addtionalParamMap)

              val transientAlgo = impl.single.Transient(transientTargetN, transformedRdd, sc, five.toInt)
              val transientResult = transientAlgo.execute(true)
              val finalResult = stepResult.union(transientResult).sortBy(_._1)
              val toWriteRDD = Utils.easyToWriteRdd(finalResult)
              var tableName = TableNameEnum.getTableName(toDo.getRate)

              //此处考虑是否是天表，天表则需要分表
              if (tableName.equalsIgnoreCase("spark_down_sample_daily")) {

                tableName = tableName.concat("_").concat(currentYear_bc.value)
              }
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String
                Utils.RddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, pointId, algo, creatTime)
              })
            }
          }
        }
        transformedRdd.unpersist(false)
      }
    }

    println("quit sc")
    sc.stop()
    spark.close()
  }
}


