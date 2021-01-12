import com.ksr.air.Run.readOpenAQData
import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.joda.time.format.ISODateTimeFormat
import org.scalatest.flatspec.AnyFlatSpec

class RunTest extends AnyFlatSpec {

  implicit val appConf: AppConfig = AppConfig.apply(Array.empty[String])
  implicit val spark: SparkSession = SparkSession
    .builder()
    .config("spark.master", "local")
    .appName("World-Air-Quality")
    .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
    .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
    .getOrCreate();

  val testSource = s"${appConf.awsBucket}/${appConf.startDate}/1506558318.ndjson"

  val openAQData: DataFrame = spark.read.format("json")
    .option("inferSchema", "true")
    .option("header", "false")
    .load(testSource)
    .withColumn("local_date", to_date(col("date.local")))
    .withColumn("month", month(col("date.local")))
    .withColumn("year", year(col("date.local")))
    .withColumn("valueTmp", col("value").cast(IntegerType))
    .drop("value").withColumnRenamed("valueTmp", "value")
    .filter(col("value") > 0 && col( "value") != 985 && col( "parameter").contains("pm25") )

  openAQData.printSchema()
 // openAQData.show(10)
  openAQData.createOrReplaceTempView("openaq")
  spark.sql( "SELECT city," +
    " parameter," +
    " value," +
    " coordinates.latitude," +
    " coordinates.longitude," +
    " country," +
    " sourceName," +
    " sourceType," +
    " unit," +
    " month," +
    " COUNT(value) OVER(PARTITION BY city, month, year ) as monthly_reading_count ," +
    " AVG(value) OVER(PARTITION BY city, month, year ) as monthly_avg," +
    " year," +
    " COUNT(value) OVER(PARTITION BY city, year ) as yearly_reading_count ," +
    " AVG(value) OVER(PARTITION BY city, year ) as yearly_avg" +
    "  FROM openaq ").show()

/*  it should "Convert a date string to date type" in {
    println("Test")
    openAQData.show(100)
    openAQData.select(
      col("local_date"),
      to_date(col("local_date"), "yyyy-MM-dd").as("localDate")
    ).show(10)
  }*/

  /*it should "readOpenAQData" in {
    val df = readOpenAQData(appConf.startDate, appConf.endDate)
    val data = df.take(100)
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
  }*/


}
