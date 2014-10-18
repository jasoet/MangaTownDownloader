package org.jasoet.akka.mangatown

import akka.actor.{Actor, ActorLogging}
import org.jasoet.util.StringUtils._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import scala.collection.JavaConverters._

/**
 * Deny Prasetyo,S.T
 * Java(Scala) Developer and Trainer
 * Software Engineer | Java Virtual Machine Junkie!
 * jasoet87@gmail.com
 * <p/>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 *
 */

class MangaActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case Request(url, startRange, endRange) =>
      sender ! grabManga(url, startRange, endRange)
  }

  def grabManga(url: String, startRange: Double, endRange: Double): MangaTown = {
    val range = Range.Double(startRange, endRange + 1, 0.1)
    log.info(s"Processing Manga From [$url]")
    val _mainPageDoc: Document = Jsoup.connect(url).get()
    val _title: String = _mainPageDoc.select(".article_content>h1.title-top").text()
    log.info(s"Got Manga Title[${_title}]")
    val _chapterListElements: Elements = _mainPageDoc.select("ul.chapter_list>li")

    log.info(s"Got Manga Title[${_title}] with ${_chapterListElements.size()} chapters")

    val _chapterList: List[MangaTownChapter] = _chapterListElements.asScala.par.map(e => {

      def sanitizeName(name: String): String = {
        val result = name.replaceAll("[^a-zA-Z0-9 '\\.\\-]", "")
        if (result.equalsIgnoreCase("new")) {
          ""
        } else {
          result
        }
      }

      val chTitle = e.select("span").size() match {
        case 2 => sanitizeName(e.select("span").get(0).text())
        case 3 => sanitizeName(e.select("span").get(1).text())
        case 4 => sanitizeName(e.select("span").get(1).text())
        case _ => ""
      }

      val chNumber = e.select("a").text().replace(_title, "").trim
      val chUrl = e.select("a").attr("href")

      MangaTownChapter(_title, chTitle, chNumber.toDoubleOpt.getOrElse(0.0), chUrl, List())
    }).filter(c => range.contains(c.number)).toList.sortBy(_.number)

    MangaTown(_title, url, _chapterList)
  }
}
