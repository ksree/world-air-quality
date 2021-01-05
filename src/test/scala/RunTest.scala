import Run.readOpenAQData
import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec

class RunTest extends AnyFlatSpec {

  behavior of "RunTest"

  implicit lazy val appConf: AppConfig = AppConfig.apply(Array.empty[String])
  implicit lazy val spark: SparkSession = SparkSession
    .builder()
    .appName("WORLD_AIR_QUALITY")
    .config("spark.master", "local")
    .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
    .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
    .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    .getOrCreate();

  it should "readOpenAQData" in {
    readOpenAQData(appConf.startDate, appConf.endDate)
  }

  it should "do something" in {
    spark.sql("""select date_format(date '2017-09-28T10:00:00+10:00', "M") """).show();

  }

}
