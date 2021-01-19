import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DecimalType, DoubleType, IntegerType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable.ListBuffer
import scala.math.BigDecimal.RoundingMode

class RunTest extends AnyFlatSpec {

  implicit val appConf: AppConfig = AppConfig.apply(Array.empty[String])
  implicit val spark: SparkSession = SparkSession
    .builder()
    .config("spark.master", "local")
    .appName("World-Air-Quality")
    .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
    .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
    .getOrCreate();

  //  val testSource = s"${appConf.awsBucket}/${appConf.startDate}/1506558318.ndjson"
  val testSource = new ListBuffer[String]
  /*  testSource += (s"${appConf.awsBucket}/${appConf.startDate}/1506558318.ndjson")
    testSource += (s"${appConf.awsBucket}/${appConf.startDate}/1506558901.ndjson")*/
  testSource += (s"${appConf.awsBucket}/2016-11-28/")
  //testSource += (s"${appConf.awsBucket}/2019-03-01/")

  val openAQData: DataFrame = spark.read.format("json")
    .option("inferSchema", "true")
    .option("header", "false")
    .load(testSource.toList: _*)
    .withColumn("local_date", to_date(col("date.local")))
    .withColumn("month", month(col("date.local")))
    .withColumn("year", year(col("date.local")))
    .withColumn("valueTmp", col("value").cast(IntegerType))
    .drop("value").withColumnRenamed("valueTmp", "value")
    .filter(col("value") > 0 && col("value") != 985 && col("parameter").contains("pm25"))

  "Read openaq s3 " should "return openair aq records" in {
    assert(openAQData.count() == 25557)
  }
  openAQData.createOrReplaceTempView("openaq")

  "Monthly Average" should "calculate monthly averages for each mponth of the year for every city" in {
    val monthlyAvg: DataFrame = com.ksr.air.Run.monthlyAvg(openAQData, 1)
    monthlyAvg.show()
    val monthly_avg_Albuquerque: List[java.math.BigDecimal] = monthlyAvg
      .select("Nov").filter(col("city") === "Albuquerque").collect.map(_.getDecimal(0)).toList
      assert(monthly_avg_Albuquerque.head.compareTo(new java.math.BigDecimal(3.87).setScale(2 , RoundingMode.DOWN)) === 0)
  }
  /*val openAveragesAQDF = spark.sql("SELECT * FROM (" +
    "   SELECT city," +
    "     parameter," +
    "     coordinates.latitude," +
    "     coordinates.longitude," +
    "     country," +
    "     sourceName," +
    "     sourceType," +
    "     unit," +
    "     month," +
    "     COUNT(value) OVER(PARTITION BY city, month, year ) as monthly_reading_count," +
    "     AVG(value) OVER(PARTITION BY city, month, year ) as monthly_avg," +
    "     year," +
    "     COUNT(value) OVER(PARTITION BY city, year ) as yearly_reading_count," +
    "     AVG(value) OVER(PARTITION BY city, year ) as yearly_avg," +
    "     value as daily_value," +
    "     local_date" +
    "     FROM openaq " +
    " )" +
    " PIVOT (" +
    "  CAST(avg(monthly_avg) AS DECIMAL(4, 2))" +
    "  FOR month in (" +
    "    1 JAN, 2 FEB, 3 MAR, 4 APR, 5 MAY, 6 JUN," +
    "    7 JUL, 8 AUG, 9 SEP, 10 OCT, 11 NOV, 12 DEC" +
    "   )" +
    " )"
  )

  openAveragesAQDF.show(100)*/
  /*openAveragesAQDF.show(100)
  openAQData.printSchema()*/


  // openAQData.show(10)
  /* val aggregated_openaq = spark.sql( "" +
     "SELECT * FROM (" +
     " SELECT city," +
     " parameter," +
     " coordinates.latitude," +
     " coordinates.longitude," +
     " country," +
     " sourceName," +
     " sourceType," +
     " unit," +
     " month," +
     " ROW_NUMBER() OVER(PARTITION BY city, month, year ORDER BY month, year) as row_number, " +
     " COUNT(value) OVER(PARTITION BY city, month, year ) as monthly_reading_count," +
     " AVG(value) OVER(PARTITION BY city, month, year ) as monthly_avg," +
     " year," +
     " COUNT(value) OVER(PARTITION BY city, year ) as yearly_reading_count," +
     " AVG(value) OVER(PARTITION BY city, year ) as yearly_avg," +
     " value as daily_value," +
     " local_date as local_date" +
     " FROM openaq )" +
     " WHERE row_number = 1")*/

  //aggregated_openaq.createOrReplaceTempView("aggregated_openaq")
  /*  spark.sql("SELECT * FROM (" +
      "  SELECT city, year, month, avg(value) AS monthly_avg" +
      "  FROM  openaq" +
      "  GROUP BY city, year, month" +
      ")" +
      "PIVOT (" +
      "  CAST(avg(monthly_avg) AS DECIMAL(4, 2))" +
      "  FOR month in (" +
      "    1 JAN, 2 FEB, 3 MAR, 4 APR, 5 MAY, 6 JUN," +
      "    7 JUL, 8 AUG, 9 SEP, 10 OCT, 11 NOV, 12 DEC" +
      "   )" +
      ")"
    ).show()*/

  /*  val monthlyAvg = openAQData.groupBy(col("year"), col("month"), col("city")).agg(avg(col("value")) as "monthly_avg")
    monthlyAvg.createOrReplaceTempView("monthlyAvg")
    val monthlyAvgPivotted = spark.sql("SELECT * FROM  monthlyAvg " +
      "PIVOT (" +
      "  CAST(avg(monthly_avg) AS DECIMAL(4, 2))" +
      "  FOR month in (" +
      "    1 JAN, 2 FEB, 3 MAR, 4 APR, 5 MAY, 6 JUN," +
      "    7 JUL, 8 AUG, 9 SEP, 10 OCT, 11 NOV, 12 DEC" +
      "   )" +
      ")"

    )*/

  //val yearlyAvg = openAQData.groupBy(col("year"), col("city")).agg(avg(col("value")) as "Yearly_Average")


  /*  val yearlyAvg = spark.sql("SELECT * FROM (" +
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
          "ROW_NUMBER() OVER(PARTITION BY city, month, year ORDER BY city, month, year) AS row_no, " +
          "AVG(value) OVER(PARTITION BY city, year) AS yearly_avg " +
        "FROM openaq " +
      ")" +
      "WHERE row_no = 1"
    )*/
  /*
    val aggregatedData = yearlyAvg.join(monthlyAvgPivotted, Seq("city", "year")).orderBy(desc("year"), desc("yearly_avg"))
    val aggregatedDataP = aggregatedData.withColumn("partitionDate", to_date(concat(col("year"), lit("-"), format_string("%02d",col("month")), lit("-01")), "yyyy-MM-dd"))

    aggregatedDataP.printSchema()
    aggregatedDataP.show()*/
  //avgValues.show(100)
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
