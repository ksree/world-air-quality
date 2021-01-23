package com.ksr.air

import java.time.LocalDate

import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DecimalType, IntegerType}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import scala.collection.mutable.ListBuffer

object Run {
  def main(args: Array[String]): Unit = {

    implicit val appConf: AppConfig = AppConfig.apply(args)

    implicit val spark: SparkSession = SparkSession
      .builder()
      .appName("World-Air-Quality")
      .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
      .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
      .getOrCreate();

    val openAQDF: DataFrame = readOpenAQData(appConf.startDate, appConf.endDate)

    val outDF: DataFrame = appConf.applyAggregations match {
      case true => aggregateTransformations(openAQDF)
      case false => openAQDF
    }

    writeToBigQuery(outDF, appConf.bigQueryTableName)
  }

  def readOpenAQData(startDate: String, endDate: String)(implicit spark: SparkSession, appConf: AppConfig): DataFrame = {
    var start: LocalDate = LocalDate.parse(startDate)
    val end: LocalDate = LocalDate.parse(endDate)
    val paths = new ListBuffer[String]
    while (start.isBefore(end) || start.isEqual(end)) {
      paths += s"${appConf.awsBucket}/${start.toString}"
      start = start.plusMonths(1)
    }
    for (path <- paths) {
      println(s"The path is $path")
    }

    val openAQData: DataFrame = spark.read.format("json")
      .option("inferSchema", "true")
      .option("header", "false")
      .load(paths.toList: _*)
      .withColumn("local_date", to_date(col("date.local")))
      .withColumn("month", month(col("date.local")))
      .withColumn("year", year(col("date.local")))
      .withColumn("valueTmp", col("value").cast(IntegerType))
      .drop("value").withColumnRenamed("valueTmp", "value")
      .filter(col("value") > 0
        && col("value") <= 985
        && col("value") != 905
        && col("parameter").contains("pm25"))

    openAQData
  }

  def aggregateTransformations(openAQDF: DataFrame, minYearlyReadings: Int = 4)(implicit spark: SparkSession, appConf: AppConfig): DataFrame = {
    val yearly_agg = yearlyAvg(openAQDF)
    val montly_agg = monthlyAvg(openAQDF, minYearlyReadings)
    yearly_agg.show()
    montly_agg.show()
    val agg = yearly_agg.join(montly_agg, Seq("year", "city"), "left")
    agg.printSchema()
    agg.select("year", "country", "city", "yearly_avg", "Jan", "Feb", "March", "April", "May",
      "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec", "coordinates.latitude", "coordinates.longitude")
  }

  def yearlyAvg(in: DataFrame): DataFrame = {
    val yearlyAvg: DataFrame = in
      .withColumn("yearly_avg",
        avg("value").over(Window.partitionBy("year", "city")).cast(new DecimalType(4, 2)))
      .withColumn("row_no",
        row_number().over(Window.partitionBy("year", "city")
          .orderBy("year", "city")))
    yearlyAvg.filter(col("row_no") === 1)
  }

  def monthlyAvg(in: DataFrame, minReadingsPerYear: Int = 4): DataFrame = {
    val monthlyAvg = in
      .groupBy("year", "month", "city")
      .agg(count("*").over(Window.partitionBy("city", "year")) as "no_readings_per_yr",
        avg("value").cast(new DecimalType(4, 2)) as "monthly_avg"
      )
      .where(col("no_readings_per_yr") >= minReadingsPerYear)
    monthlyAvg.groupBy("city", "year").pivot("month", List(expr("1 as Jan"), expr("2 as Feb"),
      expr("3 as March"), expr("4 as April"), expr("5 as May"), expr("6 as June"), expr("7 as July"),
      expr("8 as Aug"), expr("9 as Sept"), expr("10 as Oct"), expr("11 as Nov"), expr("12 as Dec")))
      .agg(avg("monthly_avg").cast(new DecimalType(4, 2)))
  }

  def writeToBigQuery(out: DataFrame, tableName: String)(implicit spark: SparkSession, appConf: AppConfig): Unit = {
    val pOut = out.withColumn("partitionDate", to_date(concat(col("year"), lit("-"),
      format_string("%02d", col("month")), lit("-01")), "yyyy-MM-dd"))
    pOut.write
      .format("bigquery")
      .mode(SaveMode.Append)
      .option("temporaryGcsBucket", appConf.tempGCSBucket)
      .option("partitionField", "partitionDate")
      .option("clusteredFields", "country")
      .option("allowFieldAddition", "true") //Adds the ALLOW_FIELD_ADDITION SchemaUpdateOption
      .save(tableName)
  }
}
