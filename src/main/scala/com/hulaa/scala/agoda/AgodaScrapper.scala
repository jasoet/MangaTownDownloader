package com.hulaa.scala.agoda

import java.net.URLDecoder

/**
 * Created by Deny Prasetyo,S.T
 * Java(Script) Developer and Trainer
 * Software Engineer
 * jasoet87@gmail.com
 * <p/>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 *
 * @jasoet
 */

object AgodaScrapper {

  def splitQuery(url: String): Map[String, List[String]] = {
    var queryPairs: Map[String, List[String]] = Map()
    val pairs = url.split("&")
    pairs.foreach(pair => {
      val idx = pair.indexOf("=")
      val key: String = if (idx > 0) URLDecoder.decode(pair.substring(0, idx), "UTF-8") else pair
      if (!queryPairs.contains(key)) {
        queryPairs += (key -> List[String]())
      }
      val value: String = if (idx > 0 && pair.length > (idx + 1)) URLDecoder.decode(pair.substring(idx + 1), "UTF-8") else null
      queryPairs.get(key) +: value
    })
    queryPairs
  }

  def main(args: Array[String]) = {
   val x =  splitQuery("http://www.agoda.com/pages/agoda/popup/popup_areamap.aspx?hotel_id=303117&area_id=93467&area_Name=Malioboro&city_id=14018&city_Name=Yogyakarta&country_id=192&latitude=110.365821272135&longitude=-7.79178241613815&PopupSize=80&asq=bs17wTmKLORqTfZUfjFABi1u0OMyeOp7vAwb6EftNBsF%2fmiot8X8y2RhPnXwN4zlEI5B13jE5hrY8QrI8P8ebX54kd4Ulf4U7Z3Ew8lJnqiGdo8klxgVfNctUlTygovreTQ0XT7UMlmP4wXLONodxWYkpWc%2br619yyWB%2fGWL8I%2bNJUApsHkfItza0ebUlA6rvEwpTFbTM5YXE39bVuANmA%3d%3d&width=990&height=525")
    println("SUerrr")
  }

}
