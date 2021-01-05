import java.time.LocalDate

import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.functions.{col, date_format}
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ListBuffer

object Run {
  def main(args: Array[String]): Unit = {

    implicit val appConf: AppConfig = AppConfig.apply(args)

    implicit val spark = SparkSession
      .builder()
      .appName("World-Air-Quality")
      .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
      .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
      .getOrCreate();
    readOpenAQData(appConf.startDate, appConf.endDate)
  }

  def readOpenAQData(startDate: String, endDate: String)(implicit spark: SparkSession, appConf: AppConfig) = {
    var start: LocalDate = LocalDate.parse(startDate)
    val end: LocalDate = LocalDate.parse(endDate)
    val paths = new ListBuffer[String]
    while (start.isBefore(end) || start.isEqual(end)) {
      paths += s"${appConf.awsBucket}/${start.toString}/1506614129.ndjson"
      start = start.plusMonths(1)
    }

    val load: DataFrame = spark.read.format("json")
      .option("inferSchema", "true")
      .option("header", "false")
      .load(paths.toList: _*)

    //Partition data based on date
    val openAQData = load.withColumn("date", date_format(col("date.local"), "yyyy-MM-dd"))
      .repartition(col("date"))

    openAQData.printSchema()
    openAQData.createOrReplaceTempView("openaq")
    val selectDF = spark.sql("SELECT * FROM openaq LIMIT 10")
    selectDF.show();

    /*Number of distinct cities per country  */
    val distinctCitiesDF = spark.sql("SELECT country, count(DISTINCT city) AS count FROM openaq GROUP BY country")
    distinctCitiesDF.show();

    /*Number of readings per day */
    val totalReadingsPerDayDF = spark.sql("SELECT date,  count(*) AS count FROM openaq GROUP BY date")
    /*Number of readings for each city per day  */
    totalReadingsPerDayDF.show(10)

    val totalReadingsPerCityPerDayDF = spark.sql("SELECT  city, date , count(*) AS count FROM openaq GROUP BY city, date")
    totalReadingsPerCityPerDayDF.show(10)

    /*    val out = data.withColumn("date", to_date(col("date.utc"))).repartition(col("date"))
    out.groupBy(col("city"))
    println(out.count())
    out.show(10)*/
  }
}
