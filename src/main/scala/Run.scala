import java.time.LocalDate

import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.SparkSession

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
    val paths = ListBuffer("")
    while (start.isBefore(end) || start.isEqual(end)) {
      paths += s"${appConf.awsBucket}/${start.toString}"
      start = start.plusMonths(1)
    }
    for (p <- paths) {
      println("path is " + p)
    }

    spark.read.format("json")
      .option("inferSchema", "true")
      .option("header", "false")
      .load("s3a://openaq-fetches/test-realtime/2017-09-28")
  }
}
