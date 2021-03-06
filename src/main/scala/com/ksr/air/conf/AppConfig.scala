package com.ksr.air.conf

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

case class AppConfig(awsKey: String,
                     awsSecret: String,
                     awsBucketName: String,
                     awsBucketPrefix: String,
                     tempGCSBucket: String,
                     bigQueryTableName: String,
                     startDate: String,
                     endDate: String,
                     applyAggregations: Boolean = false)

object AppConfig {
  def apply(args: Array[String]): AppConfig = {
    val conf: Config =
      if (args.length == 0)
        ConfigFactory.load()
      else
        ConfigFactory.parseFile(new File(args(0).trim))

    AppConfig(conf.getString("AWS_ACCESS_KEY"),
      conf.getString("AWS_SECRET_KEY"),
      conf.getString("AWS_BUCKET_NAME"),
      conf.getString("AWS_BUCKET_PREFIX"),
      conf.getString("GCS_TEMPORARY_BUCKET"),
      conf.getString("BIGQUERY_TABLE_NAME"),
      conf.getString("startDate"),
      conf.getString("endDate"),
      conf.getBoolean("applyAggregations"))
  }
}
