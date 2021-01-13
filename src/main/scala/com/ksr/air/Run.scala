package com.ksr.air

import java.time.LocalDate

import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.functions.{col, month, to_date, year}
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
    openAQDF.createOrReplaceTempView("openaq")

    val openAveragesAQDF = spark.sql("SELECT * FROM (" +
      "SELECT * FROM (" +
      "   SELECT city," +
      "     parameter," +
      "     coordinates.latitude," +
      "     coordinates.longitude," +
      "     country," +
      "     sourceName," +
      "     sourceType," +
      "     unit," +
      "     month," +
      "     ROW_NUMBER() OVER(PARTITION BY city, month, year ORDER BY month, year) as row_number, " +
      "     COUNT(value) OVER(PARTITION BY city, month, year ) as monthly_reading_count," +
      "     AVG(value) OVER(PARTITION BY city, month, year ) as monthly_avg," +
      "     year," +
      "     COUNT(value) OVER(PARTITION BY city, year ) as yearly_reading_count," +
      "     AVG(value) OVER(PARTITION BY city, year ) as yearly_avg," +
      "     value as daily_value," +
      "     local_date" +
      "     FROM openaq " +
      "   )" +
      " WHERE row_number = 1 " +
      " )" +
      " PIVOT (" +
      "  CAST(avg(monthly_avg) AS DECIMAL(4, 2))" +
      "  FOR month in (" +
      "    1 JAN, 2 FEB, 3 MAR, 4 APR, 5 MAY, 6 JUN," +
      "    7 JUL, 8 AUG, 9 SEP, 10 OCT, 11 NOV, 12 DEC" +
      "   )" +
      " )"
    )

    openAveragesAQDF
    writeToBigQuery(openAveragesAQDF, appConf.bigQueryTableName)
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
