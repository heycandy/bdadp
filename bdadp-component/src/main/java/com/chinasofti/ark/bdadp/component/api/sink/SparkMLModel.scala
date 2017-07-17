package com.chinasofti.ark.bdadp.component.api.sink

import org.apache.spark.api.java.JavaRDD
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD

/**
 * Created by water on 2017.7.14.
 */
trait SparkMLModel extends Serializable {

    def predict(testData: RDD[Vector]): RDD[Double]

    /**
     * Predict values for a single data point using the model trained.
     *
     * @param testData array representing a single data point
     * @return predicted category from the trained model
     */
    def predict(testData: Vector): Double

    /**
     * Predict values for examples stored in a JavaRDD.
     * @param testData JavaRDD representing data points to be predicted
     * @return a JavaRDD[java.lang.Double] where each entry contains the corresponding prediction
     */
    def predict(testData: JavaRDD[Vector]): JavaRDD[java.lang.Double] =
      predict(testData.rdd).toJavaRDD().asInstanceOf[JavaRDD[java.lang.Double]]

    def evaluate(): Double
}
