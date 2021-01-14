package com.ksr.air

import java.time.LocalDate

import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType
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

  def aggregateTransformations(openAQDF: DataFrame)(implicit spark: SparkSession, appConf: AppConfig): DataFrame = {
    //Transform Start
    val monthlyAvg = openAQDF
      .groupBy(col("year"), col("month"), col("city"))
      .agg(avg(col("value")) as "monthly_avg")

    monthlyAvg.createOrReplaceTempView("monthlyAvg")

    val monthlyAvgPivotted = spark.sql("SELECT * FROM  monthlyAvg " +
      "PIVOT (" +
      "  CAST(avg(monthly_avg) AS DECIMAL(4, 2))" +
      "  FOR month in (" +
      "    1 JAN, 2 FEB, 3 MAR, 4 APR, 5 MAY, 6 JUN," +
      "    7 JUL, 8 AUG, 9 SEP, 10 OCT, 11 NOV, 12 DEC" +
      "   )" +
      ")"
    )

    val yearlyAvg = spark.sql("SELECT * FROM (" +
      "SELECT " +
      "city, " +
      "parameter, " +
      "coordinates.latitude AS latitude ," +
      "coordinates.longitude AS longitude, " +
      "country," +
      "sourceName, " +
      "sourceType, " +
      "unit, " +
      "month, " +
      "year, " +
      "AVG(value) OVER(PARTITION BY city, year) AS yearly_avg, " +
      "ROW_NUMBER() OVER(PARTITION BY city, month, year ORDER BY city, month, year) AS row_no " +
      "FROM openaq " +
      ")" +
      "WHERE row_no = 1"
    )
    import org.apache.spark.sql.functions._
    val aggregatedData = yearlyAvg.join(monthlyAvgPivotted, Seq("city", "year")).orderBy(desc("year"), desc("yearly_avg"))

    aggregatedData
  }

  def readOpenAQData(startDate: String, endDate: String)(implicit spark: SparkSession, appConf: AppConfig): DataFrame = {
    var start: LocalDate = LocalDate.parse(startDate)
    val end: LocalDate = LocalDate.parse(endDate)
    val paths = new ListBuffer[String]
    while (start.isBefore(end) || start.isEqual(end)) {
      paths += s"${appConf.awsBucket}/${start.toString}"
      start = start.plusMonths(1)
    }
    for(path <- paths){
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
      .filter(col("value") > 0 && col("value") != 985 && col("parameter").contains("pm25"))

    openAQData.createOrReplaceTempView("openaq")

    openAQData
  }

  def writeToBigQuery(out: DataFrame, tableName: String)(implicit spark: SparkSession, appConf: AppConfig): Unit = {
    val pOut = out.withColumn("partitionDate", to_date(concat(col("year"), lit("-"), lpad(col("month"), 2, "0"), lit("-01")), "yyyy-mm-dd"))
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
