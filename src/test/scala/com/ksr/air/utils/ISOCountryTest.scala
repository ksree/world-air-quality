package com.ksr.air.utils

import com.ksr.air.utils.ISOCountry.ISOCountry
import org.scalatest.flatspec.AnyFlatSpec

class ISOCountryTest extends AnyFlatSpec {

  it should "retrive country from a ISO country code" in {
    assert("United States of America" === ISOCountry.from("US").get.englishName);
    assert("Botswana" === ISOCountry.from("BW").get.englishName);
  }
}
