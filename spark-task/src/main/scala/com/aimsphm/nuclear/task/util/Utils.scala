package com.aimsphm.nuclear.task.util

import java.sql._

import com.aimsphm.nuclear.task.entity.bo.PointBO

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer

/**
 * Created by xianfengmao on 2020/03-17.
 */
object Utils {

  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
  lazy val logger: Logger = Logger.getLogger(this.getClass().getName())

  def RddToSQL(it: Iterator[(Long, String)], table: String, url: String, user: String, password: String, pointId: String, algorithmType: Int, gmtCreate: java.sql.Date): Unit = {
    //一个迭代器代表一个分区，分区中有多条数据
    //先获得一个JDBC连接
    val conn: Connection = DriverManager.getConnection(url, user, password)
    //将数据通过Connection写入到数据库
    val ps: PreparedStatement = conn.prepareStatement(s"INSERT IGNORE INTO ${table}(`algorithm_type`,`point_id`,`start_timestamp`,`points`,`gmt_create`,`creator`) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)

    //将分区中的数据一条一条写入到MySQL中
    try {
      it.foreach(tp => {
        ps.setInt(1, algorithmType)
        ps.setString(2, pointId)
        ps.setLong(3, tp._1)
        ps.setString(4, tp._2)
        ps.setTimestamp(5, new Timestamp(gmtCreate.getTime))
        ps.setString(6, "spark")
        ps.executeUpdate()
      })
    } catch {
      case ex1: Exception => logger.error(ex1.printStackTrace())
      case _: Exception => logger.error("fatal exception happened")
    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }

  def GroupRddToSQL(it: Iterator[Iterator[(String, Long, String)]], table: String, url: String, user: String, password: String, algorithmType: Int, gmtCreate: java.sql.Date): Unit = {
    //一个迭代器代表一个分区，分区中有多条数据
    //先获得一个JDBC连接
    //获取数据库连接池
    //val connectionPool = HikariConnectionPool.getDataSourceInstance
    //获取链接
    //val conn:Connection = connectionPool.getConnection
    val conn: Connection = DriverManager.getConnection(url, user, password)
    //将数据通过Connection写入到数据库
    val ps: PreparedStatement = conn.prepareStatement(s"INSERT IGNORE INTO ${table}(`algorithm_type`,`point_id`,`start_timestamp`,`points`,`gmt_create`,`creator`) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)
    try {
      //将分区中的数据一条一条写入到MySQL中
      it.flatten.foreach(tp => {
        ps.setInt(1, algorithmType)
        ps.setString(2, tp._1)
        ps.setLong(3, tp._2)
        ps.setString(4, tp._3)
        ps.setTimestamp(5, new Timestamp(gmtCreate.getTime))
        ps.setString(6, "spark")
        ps.executeUpdate()
      })
    } catch {
      case ex1: Exception => logger.error(ex1.printStackTrace())
    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }


  def mapPartIndexFunc(i1: Int, iter: Iterator[(Long, Double)]): Iterator[(Int, (Long, Double))] = {
    var res = List[(Int, (Long, Double))]()
    while (iter.hasNext) {
      var next = iter.next()
      res = res.::(i1, next)
    }
    res.iterator
  }

  def printRDDPart(rdd: RDD[(Long, Double)]): Unit = {
    var mapPartIndexRDDs = rdd.mapPartitionsWithIndex(mapPartIndexFunc)
    mapPartIndexRDDs.foreach(println(_))
  }

  def easyToWriteRdd(srcRDD: RDD[(Long, Double)]): RDD[(Long, String)] = {
    // println(srcRDD.count())
    srcRDD.mapPartitions(it => {
      val list = it.toList
      val firstTS: Long = list(0)._1
      var resultList: List[(Long, String)] = List[(Long, String)]()
      val resultStringBuffer: StringBuffer = new StringBuffer()
      var stageResult: (Long, String) = (0L, "")
      list.foreach(x => {
        resultStringBuffer.append("[").append(x._1).append(",").append(x._2).append("]").append(",")
        //resultStringBuffer.deleteCharAt(resultStringBuffer.length() - 1)
      })
      if (resultStringBuffer.length() != 0) {
        resultStringBuffer.deleteCharAt(resultStringBuffer.length() - 1)
      }
      stageResult = (firstTS, resultStringBuffer.toString)
      resultList +:= stageResult


      resultList.iterator
    })
  }

  def easyToWriteRddWithTag(srcRDD: RDD[(String, (Long, Double))]): RDD[Iterator[(String, Long, String)]] = {
    // println(srcRDD.count())
    val groupRdd = srcRDD.groupByKey()
    groupRdd.map(it => {
      val tag = it._1
      val list = it._2.toList.sortBy(_._1)
      val firstTS: Long = list(0)._1
      var resultList: List[(String, Long, String)] = List[(String, Long, String)]()
      val resultStringBuffer: StringBuffer = new StringBuffer()
      var stageResult: (String, Long, String) = ("", 0L, "")
      list.foreach(x => {

        resultStringBuffer.append("[").append(x._1).append(",").append(x._2).append("]").append(",")
        //resultStringBuffer.deleteCharAt(resultStringBuffer.length() - 1)
      })
      if (resultStringBuffer.length() != 0) {
        resultStringBuffer.deleteCharAt(resultStringBuffer.length() - 1)
      }
      stageResult = (tag, firstTS, resultStringBuffer.toString)
      resultList +:= stageResult


      resultList.iterator
    })
  }

  import java.sql.PreparedStatement

  def getDownSampleConfigData(url: String, user: String, password: String, freq: String): ArrayBuffer[(String, Int, Int, Int, String)] = {
    val configTableName = "cfg_down_sample_config"

    val conn: Connection = DriverManager.getConnection(url, user, password)
    //将数据读出来
    val pstm: PreparedStatement = conn.prepareStatement(s"SELECT tag,algo,target_num,rate,portion from ${configTableName} where frequency=?")
    pstm.setString(1, freq)
    val resultSet: java.sql.ResultSet = pstm.executeQuery()
    val arrBuffer: ArrayBuffer[(String, Int, Int, Int, String)] = ArrayBuffer()
    while (resultSet.next()) {
      val tag = resultSet.getString(1)
      val algo = resultSet.getInt(2)
      val targetNum = resultSet.getInt(3)
      val rate = resultSet.getInt(4)
      val portion = resultSet.getString(5)
      val res: (String, Int, Int, Int, String) = Tuple5(tag, algo, targetNum, rate, portion)
      arrBuffer += res
    }
    if (resultSet != null) resultSet.close
    if (pstm != null) pstm.close
    if (conn != null) conn.close
    arrBuffer
  }

  def listPointIdList(url: String, user: String, password: String, freq: String): ArrayBuffer[PointBO] = {
    val configTableName = "spark_down_sample_config"
    val arrBuffer: ArrayBuffer[PointBO] = ArrayBuffer()
    val conn: Connection = DriverManager.getConnection(url, user, password)
    val ps: PreparedStatement = conn.prepareStatement(s"SELECT	DISTINCT sensor_code,feature,algorithm_type,rate,target_num,portion from ${configTableName} WHERE frequency=?")
    //    val ps: PreparedStatement = conn.prepareStatement(s"SELECT	DISTINCT sensor_code,feature,algorithm_type,rate,target_num,portion from ${configTableName} WHERE frequency=? and sensor_code like '6M2DVC003%'")
    ps.setString(1, freq)
    val rs: ResultSet = ps.executeQuery();
    while (rs.next()) {
      val sensorCode = rs.getString(1)
      val feature = rs.getString(2)
      val algorithmType = rs.getInt(3)
      val rate = rs.getInt(4)
      val targetNum = rs.getInt(5)
      val portion = rs.getString(6)
      var point = new PointBO(sensorCode, feature, algorithmType, rate, targetNum, portion)
      arrBuffer += point
    }
    if (rs != null) rs.close
    if (ps != null) ps.close
    if (conn != null) conn.close
    arrBuffer
  }
}
