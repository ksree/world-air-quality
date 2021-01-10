import com.ksr.air.Run.readOpenAQData
import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.functions.{col, date_format, month, year}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.Ignore
import org.scalatest.flatspec.AnyFlatSpec

@Ignore
class RunTest extends AnyFlatSpec {

  implicit val appConf: AppConfig = AppConfig.apply(Array.empty[String])
  implicit val spark: SparkSession = SparkSession
    .builder()
    .config("spark.master", "local")
    .appName("World-Air-Quality")
    .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
    .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
    .getOrCreate();

  val testSource = s"${appConf.awsBucket}/${appConf.startDate}/1506614129.ndjson"

  val openAQData: DataFrame = spark.read.format("json")
    .option("inferSchema", "true")
    .option("header", "false")
    .load(testSource)
    .withColumn("local_date", date_format(col("date.local"), "yyyy-MM-dd"))
    .withColumn("month", month(col("date.local")))
    .withColumn("year", year(col("date.local")))
    .repartition(col("local_date"))

  openAQData.createOrReplaceTempView("openaq")

  it should "readOpenAQData" in {
    readOpenAQData(appConf.startDate, appConf.endDate)
  }

  it should "calculate pm25 daily per city average" in {
    //https://github.com/openaq/openaq-averaging
    val pm25DailyAverage = spark.sql(
      """SELECT city, date, avg(value) AS monthly_pm25_average ,count(*) AS measurement_count
        |FROM openaq
        |WHERE  parameter="pm25" AND value > 0 AND value != 985
        |GROUP BY city, date
        |ORDER BY monthly_pm25_average DESC """.stripMargin)
    pm25DailyAverage.createOrReplaceTempView("pm25_daily_average")
    println(pm25DailyAverage.count())
    pm25DailyAverage.show(50)
    //AND averagingPeriod.value = 1 AND averagingPeriod.unit = "hours
  }
  it should "calculate pm25 monthly average concentration measurement " in {
    //https://github.com/openaq/openaq-averaging
    val pm25MonthlyAverage = spark.sql(
      """SELECT city, month, year, avg(value) AS pm25_average ,count(*) AS measurement_count
        |FROM openaq
        |WHERE  parameter="pm25" AND value > 0 AND value != 985
        |GROUP BY city, year, month
        |ORDER BY pm25_average DESC """.stripMargin)
    println(pm25MonthlyAverage.count())
    pm25MonthlyAverage.show(50)
    //AND averagingPeriod.value = 1 AND averagingPeriod.unit = "hours
  }

}
