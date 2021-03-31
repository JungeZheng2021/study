package com.aimsphm.nuclear.task.util

import org.apache.spark.Partitioner

class CustPartitioner(numPartition: Int) extends Partitioner {

  override def numPartitions: Int = numPartition

  override def getPartition(key: Any): Int = {
    val tag = key.asInstanceOf[String]

    val code = (tag.hashCode % numPartitions)

    if (code < 0) {

      code + numPartitions

    } else {

      code

    }
  }

  override def equals(other: Any): Boolean = other match {
    case cust: CustPartitioner =>
      cust.numPartitions == numPartitions
    case _ =>
      false
  }

  override def hashCode: Int = numPartitions
}
