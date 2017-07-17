package com.chinasofti.ark.bdadp.util.common

import org.apache.commons.io.FileUtils
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

/**
 * Created by water on 2017.7.14.
 */
class DataFrameUtils {

  def checkDirExists(path: String): Unit = {
    val file = new java.io.File(path)
    if (file.exists()) {
      FileUtils.deleteDirectory(file);
    }
  }

  def preProcessData(df: DataFrame, labelCol: String, featuresCol: Array[String], trainDataPer: Double): Array[RDD[LabeledPoint]] = {
    val allArr = labelCol +: featuresCol
    val allDF = df.selectExpr(allArr: _*)
    val parsedData = allDF.mapPartitions(iterator => iterator.map(row => {
      val label = row.toSeq.head.toString.toDouble
      val values = row.toSeq.tail.map(_.toString).map(_.toDouble).toArray
      LabeledPoint(label, Vectors.dense(values))
    }))

    parsedData.randomSplit(Array(trainDataPer, 1.0 - trainDataPer))
  }

//  def calculateAccuracy(testData: RDD[LabeledPoint],labelAndPreds: RDD[LabeledPoint]): Double ={
//    val testAccuracy = labelAndPreds.filter(r =>
//      r._1 == r._2).count.toDouble / testData.count
//
//    testAccuracy
//  }

}
