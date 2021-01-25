import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.s3.model.{ListObjectsRequest, ObjectListing, S3ObjectSummary}
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.ksr.air.conf.AppConfig
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
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
  testSource += (s"${appConf.awsBucketPrefix}/2016-11-28/")
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
    assert(monthly_avg_Albuquerque.head.compareTo(new java.math.BigDecimal(3.87).setScale(2, RoundingMode.DOWN)) === 0)
  }

  "Yearly Average" should "calculate yearly averages for each year for every city" in {
    val yearlyAvg: DataFrame = com.ksr.air.Run.yearlyAvg(openAQData)
    yearlyAvg.show()
  }

  "Aggregate transformations " should "calculate all aggregations in openair aq" in {
    val aggregateOpenAirAQ: DataFrame = com.ksr.air.Run.aggregateTransformations(openAQData, 1)
    aggregateOpenAirAQ.show()
  }

  "Check if exist" should "return false if path exists" in {
    import java.util.Properties
    val props: Properties = System.getProperties
    props.setProperty("AWS_ACCESS_KEY_ID", appConf.awsKey)
    props.setProperty("AWS_SECRET_ACCESS_KEY", appConf.awsSecret)

    val creds: BasicAWSCredentials = new BasicAWSCredentials(appConf.awsKey, appConf.awsSecret);
    val s3Client: AmazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion("us-east-1").build();
    val bucketName = "openaq-fetches"
    s3Client.listBuckets()

    val objectListing: ObjectListing = s3Client.listObjects(new ListObjectsRequest()
      .withBucketName(bucketName))

    objectListing.getObjectSummaries.toList.foreach{n => println(n.getKey)}

  }
}
