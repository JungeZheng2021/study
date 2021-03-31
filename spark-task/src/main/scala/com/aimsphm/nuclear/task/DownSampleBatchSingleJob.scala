package com.aimsphm.nuclear.task

import java.util.Properties

import com.aimsphm.nuclear.task.constant.Constant
import com.aimsphm.nuclear.task.downsample.algo.impl.group.ThreePointsGroupWise
import com.aimsphm.nuclear.task.util.{CustPartitioner, _}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable.ArrayBuffer

object DownSampleBatchSingleJob {


  def main(args: Array[String]): Unit = {

    import java.io.{FileOutputStream, PrintStream}
    val ps = new PrintStream(new FileOutputStream("//home//sparkJob//logSingle"))
    //将标准输出重定向到PS输出流
    System.setOut(ps)


    val properties = new Properties()
    val in = this.getClass().getClassLoader().getResourceAsStream("dev.properties");
    properties.load(in)
    val defaultPartitionNum = "4"
    val freq = args(0)
    val creatTime: java.sql.Date = new java.sql.Date(args(8).toLong)
    val appName = freq + "_" + creatTime.toString
    val spark = SparkSession.builder()
      .appName(appName).config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .config("spark.kryoserializer.buffer.max", "1024m").config("spark.kryo.classesToRegister", "org.apache.hadoop.hbase.client.Result,org.apache.hadoop.hbase.HBaseConfiguration,org.apache.hadoop.conf.Configuration")
      .getOrCreate()
    val sc = spark.sparkContext

    /* val paramMap: mutable.Map[String, String] = mutable.Map[String, String]()
     for (elem <- args) {
       val key = elem.split("=")(0)
       val value = elem.split("=")(1)
       paramMap += (key -> value)
     }*/



    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    lazy val logger: Logger = Logger.getLogger(this.getClass().getName())


    //val tag = paramMap.get("tag").get
    /**
     * HBase Config
     */
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
    /**
     * mySql config
     */

    val mysqlUrl = properties.getProperty("mysqlUrl")
    val mysqlUser = properties.getProperty("mysqlUser")
    val mysqlPassword = properties.getProperty("mysqlPassword")

    val numOfPartitions: Int = args(5).toInt

    val tagConfig = Utils.getDownSampleConfigData(mysqlUrl, mysqlUser, mysqlPassword, freq)
    val rowKeySeparator = Constant.H_BASE_ROW_KEY_CONNECTOR

    val maxBatchNumber = args(9).toInt
    val threePointsTagConfig = tagConfig.filter(_._2 == AlgoEnum.THREEPOINTS)
    val nonThreePointsTagConfig = tagConfig.filter(_._2 != AlgoEnum.THREEPOINTS)
    val threePointsTagGroupConfig = threePointsTagConfig.groupBy(_._1)
    val threePintsTags = threePointsTagGroupConfig.keys

    def readBatchTags(tags: List[String]): RDD[(String, ArrayBuffer[(Long, Double)])] = {
      val HbaseBatchRecors: RDD[(ImmutableBytesWritable, Result)] = HBaseUtil.readTagData(tags, args(1), args(2), sc, config, args(3), args(4))
      val transformedRdd = HbaseBatchRecors.values.map(x => { //x其实就是一行数据，此处谨慎使用MapPartition，会导致内存溢出
        val cells = x.rawCells()
        var tag: String = ""
        var resultList: ArrayBuffer[(Long, Double)] = ArrayBuffer[(Long, Double)]()
        //val lineTuple: Tuple2[String,ArrayBuffer[(Long, Double)]] = Tuple2[String,ArrayBuffer[(Long, Double)]]("",arr)
        for (cell <- cells) {
          //val value = Bytes.toDouble(cell.getValueArray)
          tag = Bytes.toString(cell.getRowArray, cell.getRowOffset, cell.getRowLength).split(Constant.H_BASE_ROW_KEY_CONNECTOR)(0)
          val value: Double = Bytes.toDouble(cell.getValueArray, cell.getValueOffset)
          val ts = cell.getTimestamp
          resultList += Tuple2(ts, value)
        } //一行处理完毕
        Tuple2(tag, resultList)

      }).reduceByKey((a, b) => {
        if (a(0)._1 < b(0)._1) {
          a ++ b
        } else {
          b ++ a
        }
      })
      //println(transformedRdd.count())
      //transformedRdd.foreach(println(_))
      transformedRdd
    }

    def readBatchTagsWithTs(tags: List[String], start: Long, end: Long): RDD[(String, ArrayBuffer[(Long, Double)])] = {
      val HbaseBatchRecors: RDD[(ImmutableBytesWritable, Result)] = HBaseUtil.readTagData(tags, start + "", end + "", sc, config, args(3), args(4))
      val transformedRdd = HbaseBatchRecors.values.map(x => { //x其实就是一行数据，此处谨慎使用MapPartition，会导致内存溢出
        val cells = x.rawCells()
        var tag: String = ""
        var resultList: ArrayBuffer[(Long, Double)] = ArrayBuffer[(Long, Double)]()
        //val lineTuple: Tuple2[String,ArrayBuffer[(Long, Double)]] = Tuple2[String,ArrayBuffer[(Long, Double)]]("",arr)
        for (cell <- cells) {
          //val value = Bytes.toDouble(cell.getValueArray)
          tag = Bytes.toString(cell.getRowArray, cell.getRowOffset, cell.getRowLength).split(Constant.H_BASE_ROW_KEY_CONNECTOR)(0)
          val value: Double = Bytes.toDouble(cell.getValueArray, cell.getValueOffset)
          val ts = cell.getTimestamp
          resultList += Tuple2(ts, value)
        } //一行处理完毕
        Tuple2(tag, resultList)

      }).reduceByKey((a, b) => {
        if (a(0)._1 < b(0)._1) {
          a ++ b
        } else {
          b ++ a
        }
      })
      //println(transformedRdd.count())
      //transformedRdd.foreach(println(_))
      transformedRdd
    }

    val targetNumberGroup = threePointsTagConfig.groupBy(_._3)

    /**
     * 计算间隔以便得出loop
     */

    val fre = args(0)
    var batchSz: Int = 0
    freq match {
      case "hourly" => {
        val beginTs = args(1).toLong
        val eTs = args(2).toLong
        batchSz = ((eTs - beginTs) / 3600000).toInt

      }
      case "daily" => {
        val beginTs = args(1).toLong
        val eTs = args(2).toLong
        batchSz = ((eTs - beginTs) / 3600000 / 24).toInt

      }
      case "weekly" => {
        val beginTs = args(1).toLong
        val eTs = args(2).toLong
        batchSz = ((eTs - beginTs) / 3600000 / 24 / 7).toInt

      }
    }
    //val totalRdd: RDD[(String, ArrayBuffer[(Long, Double)])] = readBatchTagsWithTs(threePintsTags.toList,args(1).toLong,args(2).toLong).filter(_._2.length != 0)//后面改
    //totalRdd.persist(StorageLevel.MEMORY_AND_DISK_SER)
    //logger.info("@@@@@@@@@@@@@@@@@@batch size is:"+batchSz+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
    for (bz <- 0 until batchSz) {
      var stepStart: Long = 0;
      var stepEnd: Long = 0;
      freq match {
        case "hourly" => {

          stepStart = args(1).toLong + bz.toLong * 3600000L
          stepEnd = stepStart


        }
        case "daily" => {
          stepStart = args(1).toLong + bz.toLong * 3600000L * 24L
          stepEnd = args(1).toLong + (bz.toLong + 1L) * 3600000L * 24L

        }
        case "weekly" => {
          stepStart = args(1).toLong + bz.toLong.toLong * 3600000L * 24L * 7L
          stepEnd = args(1).toLong + (bz.toLong + 1L) * 3600000L * 24L * 7L

        }
      }


      //for (targetN <- targetNumberGroup.keys) {
      if (threePintsTags.size <= maxBatchNumber) {
        //val totalRdd: RDD[(String, ArrayBuffer[(Long, Double)])] = readBatchTagsWithTs(threePintsTags.toList,stepStart,stepEnd).filter(_._2.length != 0)
        val transformedRdd: RDD[(String, ArrayBuffer[(Long, Double)])] = readBatchTagsWithTs(threePintsTags.toList, stepStart, stepEnd).filter(_._2.length != 0)
        /*val transformedRdd: RDD[(String, ArrayBuffer[(Long, Double)])] = totalRdd.mapValues(x=>{
          x.filter(y=>{
            y._1>=stepStart&&y._1<=stepEnd
          })
        })*/
        // val partitionedRDD = transformedRdd.partitionBy(new CustPartitioner(threePintsTags.size))
        val partitionedRDD = transformedRdd.partitionBy(new CustPartitioner(numOfPartitions))
        //val todoList = threePointsTagGroupConfig.values.toList(0) //为了拿target Number
        partitionedRDD.persist(StorageLevel.MEMORY_AND_DISK_SER)
        //val targetNumberGroup = threePointsTagConfig.groupBy(_._3)
        for (targetN <- targetNumberGroup.keys) {
          //logger.info("@@@@@@@@@@@@@@@@@target Num is >>> "+targetN+" <<<@@@@@@@@@@@@@@")
          //for (targetN <- targetNumberGroup.keys) {
          val rate: Int = targetNumberGroup.get(targetN).get(0)._4
          val correspondingTags: Set[String] = threePointsTagConfig.filter(_._3 == targetN).groupBy(_._1).keys.toSet
          val toDoRdd = partitionedRDD.filter(x => correspondingTags.contains(x._1))
          // println("rodoRDD parti---"+toDoRdd.getNumPartitions)
          if (!toDoRdd.partitions.isEmpty) {
            val nonEmptyRDD = toDoRdd.filter(_._2.length != 0)
            val threePointsAlgo = ThreePointsGroupWise
            val ThreePointsResult: RDD[(String, (Int, Long, Double))] = threePointsAlgo.execute(true, targetN, nonEmptyRDD, sc)
            val convertRdd: RDD[(String, (Long, Double))] = ThreePointsResult.map(x => (x._1, (x._2._2, x._2._3)))


            //ThreePointsResult.foreachPartition(it=>it.foreach(println(_)))
            val toWriteRDD = Utils.easyToWriteRddWithTag(convertRdd)
            val tableName = TableNameEnum.getTableName(rate)
            //val tableName = "nuclear_phm.tx_down_sample_daily"
            //toWriteRDD.foreach(x=>x.foreach(println(_)))
            toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String

              Utils.GroupRddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, AlgoEnum.THREEPOINTS, creatTime)

            })
            // println("written to MySql")
          }


        }
        partitionedRDD.unpersist(true)
      } else {
        val loop: Int = (threePintsTags.size / maxBatchNumber)
        val lastBatchAmmount: Int = threePintsTags.size % maxBatchNumber
        for (i <- 0 to loop) {
          if (i < loop) {
            val tags = threePintsTags.slice(i * maxBatchNumber, i * maxBatchNumber + maxBatchNumber) //左闭右开
            //val threePointsSliceTags = tags.groupBy(_._1)
            val partialRdd: RDD[(String, ArrayBuffer[(Long, Double)])] = readBatchTagsWithTs(tags.toList, stepStart, stepEnd).filter(_._2.length != 0)
            //val partitionedRDD = partialRdd.partitionBy(new CustPartitioner(maxBatchNumber)).persist(StorageLevel.MEMORY_AND_DISK_SER)
            val partitionedRDD = partialRdd.partitionBy(new CustPartitioner(numOfPartitions)).persist(StorageLevel.MEMORY_AND_DISK_SER)
            for (targetN <- targetNumberGroup.keys) {
              val rate: Int = targetNumberGroup.get(targetN).get(0)._4
              val correspondingTags: Set[String] = threePointsTagConfig.filter(_._3 == targetN).groupBy(_._1).keys.toSet
              val toDoRdd = partitionedRDD.filter(x => correspondingTags.contains(x._1))
              //println("in batchrodoRDD parti---"+toDoRdd.getNumPartitions)
              if (!toDoRdd.partitions.isEmpty) {
                val threePointsAlgo = ThreePointsGroupWise
                val nonEmptyRDD = toDoRdd.filter(_._2.length != 0)
                val ThreePointsResult: RDD[(String, (Int, Long, Double))] = threePointsAlgo.execute(true, targetN, nonEmptyRDD, sc)
                val convertRdd: RDD[(String, (Long, Double))] = ThreePointsResult.map(x => (x._1, (x._2._2, x._2._3)))


                //ThreePointsResult.foreachPartition(it=>it.foreach(println(_)))
                val toWriteRDD = Utils.easyToWriteRddWithTag(convertRdd)
                val tableName = TableNameEnum.getTableName(rate)
                //toWriteRDD.foreach(x=>x.foreach(println(_)))
                toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String

                  Utils.GroupRddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, AlgoEnum.THREEPOINTS, creatTime)

                })
              }
            }
            partitionedRDD.unpersist(true)
            Thread.sleep(1000)
          } else { //i==loop
            val tags = threePintsTags.slice(i * maxBatchNumber, i * maxBatchNumber + lastBatchAmmount)
            //val threePointsSliceTags = tags.groupBy(_._1)
            val partialdRdd: RDD[(String, ArrayBuffer[(Long, Double)])] = readBatchTagsWithTs(tags.toList, stepStart, stepEnd).filter(_._2.length != 0)
            // val partitionedRDD = partialdRdd.partitionBy(new CustPartitioner(lastBatchAmmount)).persist(StorageLevel.MEMORY_AND_DISK_SER)
            var atculaPartition: Int = defaultPartitionNum.toInt
            if (lastBatchAmmount > numOfPartitions) {
              atculaPartition = numOfPartitions
            } else {
              atculaPartition = lastBatchAmmount
            }
            val partitionedRDD = partialdRdd.partitionBy(new CustPartitioner(atculaPartition)).persist(StorageLevel.MEMORY_AND_DISK_SER)
            for (targetN <- targetNumberGroup.keys) {
              val rate: Int = targetNumberGroup.get(targetN).get(0)._4
              val correspondingTags: Set[String] = threePointsTagConfig.filter(_._3 == targetN).groupBy(_._1).keys.toSet
              val toDoRdd = partitionedRDD.filter(x => correspondingTags.contains(x._1))
              //println("in batch else rodoRDD parti---"+toDoRdd.getNumPartitions)
              if (!toDoRdd.partitions.isEmpty) {
                val nonEmptyRDD = toDoRdd.filter(_._2.length != 0)
                val threePointsAlgo = ThreePointsGroupWise
                val ThreePointsResult: RDD[(String, (Int, Long, Double))] = threePointsAlgo.execute(true, targetN, nonEmptyRDD, sc)
                val convertRdd: RDD[(String, (Long, Double))] = ThreePointsResult.map(x => (x._1, (x._2._2, x._2._3)))


                //ThreePointsResult.foreachPartition(it=>it.foreach(println(_)))
                val toWriteRDD = Utils.easyToWriteRddWithTag(convertRdd)
                val tableName = TableNameEnum.getTableName(rate)
                //toWriteRDD.foreach(x=>x.foreach(println(_)))
                toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String

                  Utils.GroupRddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, AlgoEnum.THREEPOINTS, creatTime)

                })
              }
            }
            partitionedRDD.unpersist(true)
            Thread.sleep(1000)
          }
        }
      }
      Thread.sleep(1000)
    }

    //totalRdd.unpersist(true)
    /*val groupConfig = nonThreePointsTagConfig.groupBy(_._1)
    for (ele <- groupConfig.keys) {
      val startKey = ele.concat(rowKeySeparator).concat(args(1))
      val endKey = ele.concat(rowKeySeparator).concat(args(2))

      val transformedRdd = HbaseWideTableReader.readAsRDD( //:RDD[(String,ArrayBuffer[(Long,Double)])]
        sc: SparkContext, config, args(3), startKey,
        endKey, args(4)).values.flatMap(_.iterator)

      val toDoList: ArrayBuffer[(String, Int, Int, Int, String)] = groupConfig.get(ele).get

      /**
       * cache
       */
      if (!transformedRdd.partitions.isEmpty) {
        transformedRdd.persist(StorageLevel.MEMORY_AND_DISK_SER)


        for (toDo <- toDoList) {
          val algo: Int = toDo._2
          algo match {

            case AlgoEnum.FIVEPOINTS => {
              val targetedNum: Int = toDo._3
              val fivePointsAlgo = impl.single.FivePoints(targetedNum.toLong, transformedRdd, sc, Integer.parseInt(args(5)))
              val fivePointsResult = fivePointsAlgo.execute(true)

              val toWriteRDD = Utils.easyToWriteRdd(fivePointsResult)
              val tableName = TableNameEnum.getTableName(toDo._4)
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String

                Utils.RddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, ele, algo, creatTime)
              })
            }
            case AlgoEnum.STEPWISE => {
              val targetedNum: Int = toDo._3
              val stepPointsAlgo = impl.single.Step(targetedNum.toLong, transformedRdd, sc, Integer.parseInt(args(5)))
              val addtionalParamMap: mutable.Map[String, Int] = mutable.Map[String, Int]()
              addtionalParamMap += ("m1" -> Integer.parseInt(args(6)))
              addtionalParamMap += ("m2" -> Integer.parseInt(args(7)))
              val stepResult = stepPointsAlgo.execute(true, addtionalParamMap)

              val toWriteRDD = Utils.easyToWriteRdd(stepResult)
              val tableName = TableNameEnum.getTableName(toDo._4)
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String

                Utils.RddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, ele, algo, creatTime)
              })
            }
            case AlgoEnum.TRANSIENT => {
              val targetedNum: Int = toDo._3
              val transientAlgo = impl.single.Transient(targetedNum.toLong, transformedRdd, sc, Integer.parseInt(args(5)))


              val transientResult = transientAlgo.execute(true)

              val toWriteRDD = Utils.easyToWriteRdd(transientResult)
              val tableName = TableNameEnum.getTableName(toDo._4)
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String

                Utils.RddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, ele, algo, creatTime)
              })
            }

            case AlgoEnum.COMBINED => {
              val targetedNum: Int = toDo._3
              val portion: Array[String] = toDo._5.split(":")
              val stepPortion: Int = portion(0).toInt
              val transientPortion: Int = portion(1).toInt
              val stepTargetN: Int = targetedNum * stepPortion / (stepPortion + transientPortion)
              val transientTargetN: Int = targetedNum * transientPortion / (stepPortion + transientPortion)
              val stepPointsAlgo = impl.single.Step(stepTargetN, transformedRdd, sc, Integer.parseInt(args(5)))
              val addtionalParamMap: mutable.Map[String, Int] = mutable.Map[String, Int]()
              addtionalParamMap += ("m1" -> Integer.parseInt(args(6)))
              addtionalParamMap += ("m2" -> Integer.parseInt(args(7)))
              val stepResult = stepPointsAlgo.execute(true, addtionalParamMap)

              val transientAlgo = impl.single.Transient(transientTargetN, transformedRdd, sc, Integer.parseInt(args(5)))


              val transientResult = transientAlgo.execute(true)
              val finalResult = stepResult.union(transientResult).sortBy(_._1)
              val toWriteRDD = Utils.easyToWriteRdd(finalResult)
              val tableName = TableNameEnum.getTableName(toDo._4)
              toWriteRDD.foreachPartition(it => { //table:String,url:String,user:String,password:String,tag:String

                Utils.RddToSQL(it, tableName, mysqlUrl, mysqlUser, mysqlPassword, ele, algo, creatTime)
              })
            }
          }
        }
        transformedRdd.unpersist(false)
      }
    }*/

    println("quit sc")
    sc.stop()
    spark.close()
  }
}


