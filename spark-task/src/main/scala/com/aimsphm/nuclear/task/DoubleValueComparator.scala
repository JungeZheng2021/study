package com.aimsphm.nuclear.task

case class DoubleValueComparator(v:Double) extends Ordered[DoubleValueComparator] with Serializable {
  override def compare(that: DoubleValueComparator): Int = {

        Ordering[Double].compare(this.v,that.v)
      }

}
