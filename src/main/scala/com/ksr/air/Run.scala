package com.ksr.air

import java.time.LocalDate

import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.functions.{col, date_format}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import scala.collection.mutable.ListBuffer

object Run {
  def main(args: Array[String]): Unit = {

    implicit val appConf: AppConfig = AppConfig.apply(args)

    implicit val spark: SparkSession = SparkSession
      .builder()
      .appName("World-Air-Quality")
      .getOrCreate();
    val openAQDF: DataFrame = readOpenAQData(appConf.startDate, appConf.endDate)

    openAQDF.createOrReplaceTempView("openaq")

    val pm25DailyAverage = spark.sql(
      """SELECT city, date, avg(value) AS monthly_pm25_average ,count(*) AS measurement_count
        |FROM openaq
        |WHERE  parameter="pm25" AND value > 0 AND value != 985
        |GROUP BY city, date
        |ORDER BY monthly_pm25_average DESC """.stripMargin)

    writeToBigQuery(pm25DailyAverage, "PM25DailyAverage")
  }

  def readOpenAQData(startDate: String, endDate: String)(implicit spark: SparkSession, appConf: AppConfig): DataFrame = {
    var start: LocalDate = LocalDate.parse(startDate)
    val end: LocalDate = LocalDate.parse(endDate)
    val paths = new ListBuffer[String]
    while (start.isBefore(end) || start.isEqual(end)) {
      paths += s"${appConf.awsBucket}/${start.toString}"
      start = start.plusMonths(1)
    }

    val openAQData: DataFrame = spark.read.format("json")
      .option("inferSchema", "true")
      .option("header", "false")
      .load(paths.toList: _*)
      .withColumn("local_date", date_format(col("date.local"), "yyyy-MM-dd"))
      .withColumn("month", date_format(col("date.local"), "MMM"))
      .withColumn("year", date_format(col("date.local"), "yyyy"))
      .repartition(col("date"))

    openAQData.createOrReplaceTempView("openaq")

    openAQData
  }

  def writeToBigQuery(out: DataFrame, tableName: String)(implicit spark: SparkSession, appConf: AppConfig): Unit = {
    out.write
      .format("bigquery")
      .mode(SaveMode.Append)
      .option("temporaryGcsBucket", appConf.tempGCSBucket)
      .option("partitionField", "local_date")
      .option("partitionType", "DAY")
      .option("clusteredFields", "country")
      .option("allowFieldAddition", "true") //Adds the ALLOW_FIELD_ADDITION SchemaUpdateOption
      .save(tableName)
  }
}