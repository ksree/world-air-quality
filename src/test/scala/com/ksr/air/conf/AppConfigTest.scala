package com.ksr.air.conf

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec

class AppConfigTest extends AnyFlatSpec {

  val appConfig: AppConfig = AppConfig(Array.empty[String])

  "config" should "have the properties" in {
    assert(appConfig.awsBucket == "s3a://noaa-ghcn-pds/csv/")
  }
}
