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

  "Yearly Average" should "calculate yearly averages for each year for every city" in {
    val yearlyAvg: DataFrame = com.ksr.air.Run.yearlyAvg(openAQData)
    yearlyAvg.show()
  }

  "Aggregate transformations " should "calculate all aggregations in openair aq" in {
    val aggregateOpenAirAQ: DataFrame = com.ksr.air.Run.aggregateTransformations(openAQData, 1)
     aggregateOpenAirAQ.show()
  }
}
