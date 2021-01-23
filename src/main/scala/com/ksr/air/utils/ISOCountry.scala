package com.ksr.air.utils

/**
 * *
 * From https://index.scala-lang.org/vitorsvieira/scala-iso/scala-iso/0.1.2?target=_2.12
 * https://github.com/vitorsvieira/scala-iso/blob/master/src/main/scala/com/vitorsvieira/iso/ISOCountry.scala
 */
object ISOCountry extends Enum {

  sealed class EnumVal(
                        val value:         String,
                        val numericalCode: Int,
                        val englishName:   String,
                        val alpha3Code:    String,
                      ) extends Value

  type ISOCountry = EnumVal
  // format: OFF
  val AFGHANISTAN                                  = new ISOCountry("AF", 4, "Afghanistan", "AFG")
  val ALAND_ISLANDS                                = new ISOCountry("AX", 248, "Åland Islands", "ALA")
  val ALBANIA                                      = new ISOCountry("AL", 8, "Albania", "ALB")
  val ALGERIA                                      = new ISOCountry("DZ", 12, "Algeria", "DZA")
  val AMERICAN_SAMOA                               = new ISOCountry("AS", 16, "American Samoa", "ASM")
  val ANDORRA                                      = new ISOCountry("AD", 20, "Andorra", "AND")
  val ANGOLA                                       = new ISOCountry("AO", 24, "Angola", "AGO")
  val ANGUILLA                                     = new ISOCountry("AI", 660, "Anguilla", "AIA")
  val ANTARCTICA                                   = new ISOCountry("AQ", 10, "Antarctica", "ATA")
  val ANTIGUA_AND_BARBUDA                          = new ISOCountry("AG", 28, "Antigua and Barbuda", "ATG")
  val ARGENTINA                                    = new ISOCountry("AR", 32, "Argentina", "ARG")
  val ARMENIA                                      = new ISOCountry("AM", 51, "Armenia", "ARM")
  val ARUBA                                        = new ISOCountry("AW", 533, "Aruba", "ABW")
  val AUSTRALIA                                    = new ISOCountry("AU", 36, "Australia", "AUS")
  val AUSTRIA                                      = new ISOCountry("AT", 40, "Austria", "AUT")
  val AZERBAIJAN                                   = new ISOCountry("AZ", 31, "Azerbaijan", "AZE")
  val BAHAMAS                                      = new ISOCountry("BS", 44, "Bahamas", "BHS")
  val BAHRAIN                                      = new ISOCountry("BH", 48, "Bahrain", "BHR")
  val BANGLADESH                                   = new ISOCountry("BD", 50, "Bangladesh", "BGD")
  val BARBADOS                                     = new ISOCountry("BB", 52, "Barbados", "BRB")
  val BELARUS                                      = new ISOCountry("BY", 112, "Belarus", "BLR")
  val BELGIUM                                      = new ISOCountry("BE", 56, "Belgium", "BEL")
  val BELIZE                                       = new ISOCountry("BZ", 84, "Belize", "BLZ")
  val BENIN                                        = new ISOCountry("BJ", 204, "Benin", "BEN")
  val BERMUDA                                      = new ISOCountry("BM", 60, "Bermuda", "BMU")
  val BHUTAN                                       = new ISOCountry("BT", 64, "Bhutan", "BTN")
  val BOLIVIA                                      = new ISOCountry("BO", 68, "Bolivia (Plurinational State of)", "BOL")
  val BONAIRE                                      = new ISOCountry("BQ", 535, "Bonaire, Sint Eustatius and Saba", "BES")
  val BOSNIA_AND_HERZEGOVINA                       = new ISOCountry("BA", 70, "Bosnia and Herzegovina", "BIH")
  val BOTSWANA                                     = new ISOCountry("BW", 72, "Botswana", "BWA")
  val BOUVET_ISLAND                                = new ISOCountry("BV", 74, "Bouvet Island", "BVT")
  val BRAZIL                                       = new ISOCountry("BR", 76, "Brazil", "BRA")
  val BRITISH_INDIAN_OCEAN_TERRITORY               = new ISOCountry("IO", 86, "British Indian Ocean Territory (the)", "IOT")
  val BRITISH_VIRGIN_ISLANDS                       = new ISOCountry("VG", 92, "British Virgin Islands", "VGB")
  val BRUNEI_DARUSSALAM                            = new ISOCountry("BN", 96, "Brunei Darussalam", "BRN")
  val BULGARIA                                     = new ISOCountry("BG", 100, "Bulgaria", "BGR")
  val BURKINA_FASO                                 = new ISOCountry("BF", 854, "Burkina Faso", "BFA")
  val BURUNDI                                      = new ISOCountry("BI", 108, "Burundi", "BDI")
  val CAMBODIA                                     = new ISOCountry("KH", 116, "Cambodia", "KHM")
  val CAMEROON                                     = new ISOCountry("CM", 120, "Cameroon", "CMR")
  val CANADA                                       = new ISOCountry("CA", 124, "Canada", "CAN")
  val CAPE_VERDE                                   = new ISOCountry("CV", 132, "Cabo Verde", "CPV")
  val CAYMAN_ISLANDS                               = new ISOCountry("KY", 136, "Cayman Islands", "CYM")
  val CENTRAL_AFRICAN_REPUBLIC                     = new ISOCountry("CF", 140, "Central African Republic", "CAF")
  val CHAD                                         = new ISOCountry("TD", 148, "Chad", "TCD")
  val CHILE                                        = new ISOCountry("CL", 152, "Chile", "CHL")
  val CHINA                                        = new ISOCountry("CN", 156, "China", "CHN")
  val CHRISTMAS_ISLAND                             = new ISOCountry("CX", 162, "Christmas Island", "CXR")
  val COCOS_ISLANDS                                = new ISOCountry("CC", 166, "Cocos (Keeling) Islands (the)", "CCK")
  val COLOMBIA                                     = new ISOCountry("CO", 170, "Colombia", "COL")
  val COMOROS                                      = new ISOCountry("KM", 174, "Comoros", "COM")
  val CONGO                                        = new ISOCountry("CG", 178, "Congo", "COG")
  val COOK_ISLANDS                                 = new ISOCountry("CK", 184, "Cook Islands", "COK")
  val COSTA_RICA                                   = new ISOCountry("CR", 188, "Costa Rica", "CRI")
  val COTE_D_IVOIRE                                = new ISOCountry("CI", 384, "Côte d'Ivoire", "CIV")
  val CROATIA                                      = new ISOCountry("HR", 191, "Croatia", "HRV")
  val CUBA                                         = new ISOCountry("CU", 192, "Cuba", "CUB")
  val CURACAO                                      = new ISOCountry("CW", 531, "Curaçao", "CUW")
  val CYPRUS                                       = new ISOCountry("CY", 196, "Cyprus", "CYP")
  val CZECH_REPUBLIC                               = new ISOCountry("CZ", 203, "Czechia", "CZE")
  val DENMARK                                      = new ISOCountry("DK", 208, "Denmark", "DNK")
  val DJIBOUTI                                     = new ISOCountry("DJ", 262, "Djibouti", "DJI")
  val DOMINICA                                     = new ISOCountry("DM", 212, "Dominica", "DMA")
  val DOMINICAN_REPUBLIC                           = new ISOCountry("DO", 214, "Dominican Republic", "DOM")
  val ECUADOR                                      = new ISOCountry("EC", 218, "Ecuador", "ECU")
  val EGYPT                                        = new ISOCountry("EG", 818, "Egypt", "EGY")
  val EL_SALVADOR                                  = new ISOCountry("SV", 222, "El Salvador", "SLV")
  val EQUATORIAL_GUINEA                            = new ISOCountry("GQ", 226, "Equatorial Guinea", "GNQ")
  val ERITREA                                      = new ISOCountry("ER", 232, "Eritrea", "ERI")
  val ESTONIA                                      = new ISOCountry("EE", 233, "Estonia", "EST")
  val ETHIOPIA                                     = new ISOCountry("ET", 231, "Ethiopia", "ETH")
  val FALKLAND_ISLANDS                             = new ISOCountry("FK", 238, "Falkland Islands (Malvinas)", "FLK")
  val FAROE_ISLANDS                                = new ISOCountry("FO", 234, "Faeroe Islands", "FRO")
  val FIJI                                         = new ISOCountry("FJ", 242, "Fiji", "FJI")
  val FINLAND                                      = new ISOCountry("FI", 246, "Finland", "FIN")
  val FRANCE                                       = new ISOCountry("FR", 250, "France", "FRA")
  val FRENCH_GUIANA                                = new ISOCountry("GF", 254, "French Guiana", "GUF")
  val FRENCH_POLYNESIA                             = new ISOCountry("PF", 258, "French Polynesia", "PYF")
  val FRENCH_SOUTHERN_TERRITORIES                  = new ISOCountry("TF", 260, "French Southern Territories (the)", "ATF")
  val GABON                                        = new ISOCountry("GA", 266, "Gabon", "GAB")
  val GAMBIA                                       = new ISOCountry("GM", 270, "Gambia", "GMB")
  val GEORGIA                                      = new ISOCountry("GE", 268, "Georgia", "GEO")
  val GERMANY                                      = new ISOCountry("DE", 276, "Germany", "DEU")
  val GHANA                                        = new ISOCountry("GH", 288, "Ghana", "GHA")
  val GIBRALTAR                                    = new ISOCountry("GI", 292, "Gibraltar", "GIB")
  val GREECE                                       = new ISOCountry("GR", 300, "Greece", "GRC")
  val GREENLAND                                    = new ISOCountry("GL", 304, "Greenland", "GRL")
  val GRENADA                                      = new ISOCountry("GD", 308, "Grenada", "GRD")
  val GUADELOUPE                                   = new ISOCountry("GP", 312, "Guadeloupe", "GLP")
  val GUAM                                         = new ISOCountry("GU", 316, "Guam", "GUM")
  val GUATEMALA                                    = new ISOCountry("GT", 320, "Guatemala", "GTM")
  val GUERNSEY                                     = new ISOCountry("GG", 831, "Guernsey", "GGY")
  val GUINEA                                       = new ISOCountry("GN", 324, "Guinea", "GIN")
  val GUINEA_BISSAU                                = new ISOCountry("GW", 624, "Guinea-Bissau", "GNB")
  val GUYANA                                       = new ISOCountry("GY", 328, "Guyana", "GUY")
  val HAITI                                        = new ISOCountry("HT", 332, "Haiti", "HTI")
  val HEARD_ISLAND_AND_MCDONALD_ISLANDS            = new ISOCountry("HM", 334, "Heard Island and McDonald Islands", "HMD")
  val HONDURAS                                     = new ISOCountry("HN", 340, "Honduras", "HND")
  val HONG_KONG                                    = new ISOCountry("HK", 344, "Hong Kong", "HKG")
  val HUNGARY                                      = new ISOCountry("HU", 348, "Hungary", "HUN")
  val ICELAND                                      = new ISOCountry("IS", 352, "Iceland", "ISL")
  val INDIA                                        = new ISOCountry("IN", 356, "India", "IND")
  val INDONESIA                                    = new ISOCountry("ID", 360, "Indonesia", "IDN")
  val IRAN                                         = new ISOCountry("IR", 364, "Iran (Islamic Republic of)", "IRN")
  val IRAQ                                         = new ISOCountry("IQ", 368, "Iraq", "IRQ")
  val IRELAND                                      = new ISOCountry("IE", 372, "Ireland", "IRL")
  val ISLE_OF_MAN                                  = new ISOCountry("IM", 833, "Isle of Man", "IMN")
  val ISRAEL                                       = new ISOCountry("IL", 376, "Israel", "ISR")
  val ITALY                                        = new ISOCountry("IT", 380, "Italy", "ITA")
  val JAMAICA                                      = new ISOCountry("JM", 388, "Jamaica", "JAM")
  val JAPAN                                        = new ISOCountry("JP", 392, "Japan", "JPN")
  val JERSEY                                       = new ISOCountry("JE", 832, "Jersey", "JEY")
  val JORDAN                                       = new ISOCountry("JO", 400, "Jordan", "JOR")
  val KAZAKHSTAN                                   = new ISOCountry("KZ", 398, "Kazakhstan", "KAZ")
  val KENYA                                        = new ISOCountry("KE", 404, "Kenya", "KEN")
  val KIRIBATI                                     = new ISOCountry("KI", 296, "Kiribati", "KIR")
  val KUWAIT                                       = new ISOCountry("KW", 414, "Kuwait", "KWT")
  val KYRGYZSTAN                                   = new ISOCountry("KG", 417, "Kyrgyzstan", "KGZ")
  val LAO_PEOPLES_DEMOCRATIC_REPUBLIC              = new ISOCountry("LA", 418, "Lao People's Democratic Republic", "LAO")
  val LATVIA                                       = new ISOCountry("LV", 428, "Latvia", "LVA")
  val LEBANON                                      = new ISOCountry("LB", 422, "Lebanon", "LBN")
  val LESOTHO                                      = new ISOCountry("LS", 426, "Lesotho", "LSO")
  val LIBERIA                                      = new ISOCountry("LR", 430, "Liberia", "LBR")
  val LIBYA                                        = new ISOCountry("LY", 434, "Libya", "LBY")
  val LIECHTENSTEIN                                = new ISOCountry("LI", 438, "Liechtenstein", "LIE")
  val LITHUANIA                                    = new ISOCountry("LT", 440, "Lithuania", "LTU")
  val LUXEMBOURG                                   = new ISOCountry("LU", 442, "Luxembourg", "LUX")
  val MACAO                                        = new ISOCountry("MO", 446, "Macao", "MAC")
  val MACEDONIA                                    = new ISOCountry("MK", 807, "Macedonia (the former Yugoslav Republic of)", "MKD")
  val MADAGASCAR                                   = new ISOCountry("MG", 450, "Madagascar", "MDG")
  val MALAWI                                       = new ISOCountry("MW", 454, "Malawi", "MWI")
  val MALAYSIA                                     = new ISOCountry("MY", 458, "Malaysia", "MYS")
  val MALDIVES                                     = new ISOCountry("MV", 462, "Maldives", "MDV")
  val MALI                                         = new ISOCountry("ML", 466, "Mali", "MLI")
  val MALTA                                        = new ISOCountry("MT", 470, "Malta", "MLT")
  val MARSHALL_ISLANDS                             = new ISOCountry("MH", 584, "Marshall Islands", "MHL")
  val MARTINIQUE                                   = new ISOCountry("MQ", 474, "Martinique", "MTQ")
  val MAURITANIA                                   = new ISOCountry("MR", 478, "Mauritania", "MRT")
  val MAURITIUS                                    = new ISOCountry("MU", 480, "Mauritius", "MUS")
  val MAYOTTE                                      = new ISOCountry("YT", 175, "Mayotte", "MYT")
  val MEXICO                                       = new ISOCountry("MX", 484, "Mexico", "MEX")
  val MICRONESIA                                   = new ISOCountry("FM", 583, "Micronesia (Federated States of)", "FSM")
  val MOLDOVA                                      = new ISOCountry("MD", 498, "Moldova (the Republic of)", "MDA")
  val MONACO                                       = new ISOCountry("MC", 492, "Monaco", "MCO")
  val MONGOLIA                                     = new ISOCountry("MN", 496, "Mongolia", "MNG")
  val MONTENEGRO                                   = new ISOCountry("ME", 499, "Montenegro", "MNE")
  val MONTSERRAT                                   = new ISOCountry("MS", 500, "Montserrat", "MSR")
  val MOROCCO                                      = new ISOCountry("MA", 504, "Morocco", "MAR")
  val MOZAMBIQUE                                   = new ISOCountry("MZ", 508, "Mozambique", "MOZ")
  val MYANMAR                                      = new ISOCountry("MM", 104, "Myanmar", "MMR")
  val NAMIBIA                                      = new ISOCountry("NA", 516, "Namibia", "NAM")
  val NAURU                                        = new ISOCountry("NR", 520, "Nauru", "NRU")
  val NEPAL                                        = new ISOCountry("NP", 524, "Nepal", "NPL")
  val NETHERLANDS                                  = new ISOCountry("NL", 528, "Netherlands", "NLD")
  val NEW_CALEDONIA                                = new ISOCountry("NC", 540, "New Caledonia", "NCL")
  val NEW_ZEALAND                                  = new ISOCountry("NZ", 554, "New Zealand", "NZL")
  val NICARAGUA                                    = new ISOCountry("NI", 558, "Nicaragua", "NIC")
  val NIGER                                        = new ISOCountry("NE", 562, "Niger", "NER")
  val NIGERIA                                      = new ISOCountry("NG", 566, "Nigeria", "NGA")
  val NIUE                                         = new ISOCountry("NU", 570, "Niue", "NIU")
  val NORFOLK_ISLAND                               = new ISOCountry("NF", 574, "Norfolk Island", "NFK")
  val NORTHERN_MARIANA_ISLANDS                     = new ISOCountry("MP", 580, "Northern Mariana Islands", "MNP")
  val NORTH_KOREA                                  = new ISOCountry("KP", 408, "Korea (the Democratic People's Republic of)", "PRK")
  val NORWAY                                       = new ISOCountry("NO", 578, "Norway", "NOR")
  val OMAN                                         = new ISOCountry("OM", 512, "Oman", "OMN")
  val PAKISTAN                                     = new ISOCountry("PK", 586, "Pakistan", "PAK")
  val PALAU                                        = new ISOCountry("PW", 585, "Palau", "PLW")
  val PALESTINE                                    = new ISOCountry("PS", 275, "Palestine, State of", "PSE")
  val PANAMA                                       = new ISOCountry("PA", 591, "Panama", "PAN")
  val PAPUA_NEW_GUINEA                             = new ISOCountry("PG", 598, "Papua New Guinea", "PNG")
  val PARAGUAY                                     = new ISOCountry("PY", 600, "Paraguay", "PRY")
  val PERU                                         = new ISOCountry("PE", 604, "Peru", "PER")
  val PHILIPPINES                                  = new ISOCountry("PH", 608, "Philippines", "PHL")
  val PITCAIRN                                     = new ISOCountry("PN", 612, "Pitcairn Islands", "PCN")
  val POLAND                                       = new ISOCountry("PL", 616, "Poland", "POL")
  val PORTUGAL                                     = new ISOCountry("PT", 620, "Portugal", "PRT")
  val PUERTO_RICO                                  = new ISOCountry("PR", 630, "Puerto Rico", "PRI")
  val QATAR                                        = new ISOCountry("QA", 634, "Qatar", "QAT")
  val REUNION                                      = new ISOCountry("RE", 638, "Réunion", "REU")
  val ROMANIA                                      = new ISOCountry("RO", 642, "Romania", "ROU")
  val RUSSIAN_FEDERATION                           = new ISOCountry("RU", 643, "Russian Federation", "RUS")
  val RWANDA                                       = new ISOCountry("RW", 646, "Rwanda", "RWA")
  val SAINT_BARTHELEMY                             = new ISOCountry("BL", 652, "Saint Barthélemy", "BLM")
  val SAINT_HELENA                                 = new ISOCountry("SH", 654, "Saint Helena", "SHN")
  val SAINT_KITTS_AND_NEVIS                        = new ISOCountry("KN", 659, "Saint Kitts and Nevis", "KNA")
  val SAINT_LUCIA                                  = new ISOCountry("LC", 662, "Saint Lucia", "LCA")
  val SAINT_MARTIN                                 = new ISOCountry("MF", 663, "Saint Martin (French part)", "MAF")
  val SAINT_PIERRE_AND_MIQUELON                    = new ISOCountry("PM", 666, "Saint Pierre and Miquelon", "SPM")
  val SAINT_VINCENT_AND_THE_GRENADINES             = new ISOCountry("VC", 670, "Saint Vincent and the Grenadines", "VCT")
  val SAMOA                                        = new ISOCountry("WS", 882, "Samoa", "WSM")
  val SAN_MARINO                                   = new ISOCountry("SM", 674, "San Marino", "SMR")
  val SAO_TOME_AND_PRINCIPE                        = new ISOCountry("ST", 678, "Sao Tome and Principe", "STP")
  val SAUDI_ARABIA                                 = new ISOCountry("SA", 682, "Saudi Arabia", "SAU")
  val SENEGAL                                      = new ISOCountry("SN", 686, "Senegal", "SEN")
  val SERBIA                                       = new ISOCountry("RS", 688, "Serbia", "SRB")
  val SEYCHELLES                                   = new ISOCountry("SC", 690, "Seychelles", "SYC")
  val SIERRA_LEONE                                 = new ISOCountry("SL", 694, "Sierra Leone", "SLE")
  val SINGAPORE                                    = new ISOCountry("SG", 702, "Singapore", "SGP")
  val SINT_MAARTEN                                 = new ISOCountry("SX", 534, "Sint Maarten (Dutch part)", "SXM")
  val SLOVAKIA                                     = new ISOCountry("SK", 703, "Slovakia", "SVK")
  val SLOVENIA                                     = new ISOCountry("SI", 705, "Slovenia", "SVN")
  val SOLOMON_ISLANDS                              = new ISOCountry("SB", 90, "Solomon Islands", "SLB")
  val SOMALIA                                      = new ISOCountry("SO", 706, "Somalia", "SOM")
  val SOUTH_AFRICA                                 = new ISOCountry("ZA", 710, "South Africa", "ZAF")
  val SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS = new ISOCountry("GS", 239, "South Georgia and the South Sandwich Island", "SGS")
  val SOUTH_KOREA                                  = new ISOCountry("KR", 410, "Korea (the Republic of)", "KOR")
  val SOUTH_SUDAN                                  = new ISOCountry("SS", 728, "South Sudan", "SSD")
  val SPAIN                                        = new ISOCountry("ES", 724, "Spain", "ESP")
  val SRI_LANKA                                    = new ISOCountry("LK", 144, "Sri Lanka", "LKA")
  val SUDAN                                        = new ISOCountry("SD", 729, "Sudan", "SDN")
  val SURINAME                                     = new ISOCountry("SR", 740, "Suriname", "SUR")
  val SVALBARD_AND_JAN_MAYEN                       = new ISOCountry("SJ", 744, "Svalbard and Jan Mayen Island", "SJM")
  val SWAZILAND                                    = new ISOCountry("SZ", 748, "Swaziland", "SWZ")
  val SWEDEN                                       = new ISOCountry("SE", 752, "Sweden", "SWE")
  val SWITZERLAND                                  = new ISOCountry("CH", 756, "Switzerland", "CHE")
  val SYRIAN_ARAB_REPUBLIC                         = new ISOCountry("SY", 760, "Syrian Arab Republic", "SYR")
  val TAIWAN                                       = new ISOCountry("TW", 158, "Taiwan (Province of China)", "TWN")
  val TAJIKISTAN                                   = new ISOCountry("TJ", 762, "Tajikistan", "TJK")
  val TANZANIA                                     = new ISOCountry("TZ", 834, "Tanzania, United Republic of", "TZA")
  val THAILAND                                     = new ISOCountry("TH", 764, "Thailand", "THA")
  val THE_DEMOCRATIC_REPUBLIC_OF_THE_CONGO         = new ISOCountry("CD", 180, "Congo (the Democratic Republic of the)", "COD")
  val TIMOR_LESTE                                  = new ISOCountry("TL", 626, "Timor-Leste", "TLS")
  val TOGO                                         = new ISOCountry("TG", 768, "Togo", "TGO")
  val TOKELAU                                      = new ISOCountry("TK", 772, "Tokelau", "TKL")
  val TONGA                                        = new ISOCountry("TO", 776, "Tonga", "TON")
  val TRINIDAD_AND_TOBAGO                          = new ISOCountry("TT", 780, "Trinidad and Tobago", "TTO")
  val TUNISIA                                      = new ISOCountry("TN", 788, "Tunisia", "TUN")
  val TURKEY                                       = new ISOCountry("TR", 792, "Turkey", "TUR")
  val TURKMENISTAN                                 = new ISOCountry("TM", 795, "Turkmenistan", "TKM")
  val TURKS_AND_CAICOS_ISLANDS                     = new ISOCountry("TC", 796, "Turks and Caicos Islands", "TCA")
  val TUVALU                                       = new ISOCountry("TV", 798, "Tuvalu", "TUV")
  val UGANDA                                       = new ISOCountry("UG", 800, "Uganda", "UGA")
  val UKRAINE                                      = new ISOCountry("UA", 804, "Ukraine", "UKR")
  val UNITED_ARAB_EMIRATES                         = new ISOCountry("AE", 784, "United Arab Emirates", "ARE")
  val UNITED_KINGDOM                               = new ISOCountry("GB", 826, "United Kingdom of Great Britain and Northern Ireland", "GBR")
  val UNITED_STATES                                = new ISOCountry("US", 840, "United States of America", "USA")
  val UNITED_STATES_MINOR_OUTLYING_ISLANDS         = new ISOCountry("UM", 581, "United States Minor Outlying Islands (the)", "UMI")
  val URUGUAY                                      = new ISOCountry("UY", 858, "Uruguay", "URY")
  val US_VIRGIN_ISLANDS                            = new ISOCountry("VI", 850, "United States Virgin Islands", "VIR")
  val UZBEKISTAN                                   = new ISOCountry("UZ", 860, "Uzbekistan", "UZB")
  val VANUATU                                      = new ISOCountry("VU", 548, "Vanuatu", "VUT")
  val VATICAN_CITY_STATE                           = new ISOCountry("VA", 336, "Holy See (the)", "VAT")
  val VENEZUELA                                    = new ISOCountry("VE", 862, "Venezuela (Bolivarian Republic of)", "VEN")
  val VIET_NAM                                     = new ISOCountry("VN", 704, "Viet Nam", "VNM")
  val WALLIS_AND_FUTUNA                            = new ISOCountry("WF", 876, "Wallis and Futuna Islands", "WLF")
  val WESTERN_SAHARA                               = new ISOCountry("EH", 732, "Western Sahara", "ESH")
  val YEMEN                                        = new ISOCountry("YE", 887, "Yemen", "YEM")
  val ZAMBIA                                       = new ISOCountry("ZM", 894, "Zambia", "ZMB")
  val ZIMBABWE                                     = new ISOCountry("ZW", 716, "Zimbabwe", "ZWE")
  // format: ON

  /**
   * Retrieves ISOCountry based on alpha-2 code.
   * https://www.iso.org/obp/ui/#search
   *
   * @param countryCode Country code, ie. US, CN
   * @return ISOCountry
   */
  def apply(countryCode: String): ISOCountry =
    ISOCountry.values.find(countryCode == _.toString) match {
      case Some(country) ⇒ country
      case _             ⇒ throw new ParseException(s"Invalid alpha-2 code '$countryCode' for ISOCountry")
    }

  /**
   * Retrieves Option[ISOCountry] based on alpha-2 code.
   * https://www.iso.org/obp/ui/#search
   *
   * @param countryCode Country code, ie. US, CN
   * @return Option[ISOCountry]
   */
  def from(countryCode: String): Option[ISOCountry] =
    ISOCountry.values.find(countryCode == _.toString)

  /**
   * Retrieves ISOCountry based on numeric code.
   * https://www.iso.org/obp/ui/#search
   *
   * @param numericCode Numeric code, ie. 840, 826
   * @return ISOCountry
   */
  def apply(numericCode: Int): ISOCountry =
    ISOCountry.values.find(numericCode == _.numericalCode) match {
      case Some(country) ⇒ country
      case _             ⇒ throw new ParseException(s"Invalid numeric code '$numericCode' for ISOCountry")
    }

  /**
   * Retrieves Option[ISOCountry] based on numeric code.
   * https://www.iso.org/obp/ui/#search
   *
   * @param numericCode Numeric code, ie. 840, 826
   * @return Option[ISOCountry]
   */
  def from(numericCode: Int): Option[ISOCountry] =
    ISOCountry.values.find(numericCode == _.numericalCode)

}