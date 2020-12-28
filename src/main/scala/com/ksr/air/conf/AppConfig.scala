package com.ksr.air.conf

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

case class AppConfig(awsKey: String,
                     awsSecret: String,
                     awsBucket: String,
                     tempGCSBucket: String,
                     bigQueryTableName: String,
                     startYear: Int,
                     endYear: Int)

object AppConfig {
  def apply(args: Array[String]): AppConfig = {
    val conf: Config =
      if (args.length == 0)
        ConfigFactory.load()
      else
        ConfigFactory.parseFile(new File(args(0).trim))

    AppConfig(conf.getString("AWS_ACCESS_KEY"),
      conf.getString("AWS_SECRET_KEY"),
      conf.getString("AWS_BUCKET"),
      conf.getString("GCS_TEMPORARY_BUCKET"),
      conf.getString("BIGQUERY_TABLE_NAME"),
      conf.getInt("startYear"),
      conf.getInt("endYear"))
  }
}
