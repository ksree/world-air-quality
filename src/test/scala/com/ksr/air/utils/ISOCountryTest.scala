package com.ksr.air.utils

import com.ksr.air.utils.ISOCountry.ISOCountry
import org.scalatest.flatspec.AnyFlatSpec

class ISOCountryTest extends AnyFlatSpec {

  it should "retrive country from a ISO country code" in {
    assert("United States of America" === ISOCountry.from("US"));
    assert("Botswana" === ISOCountry.from("BW"));
    assert("InvalidCountryCode" === ISOCountry.from("DX"));
  }
}
