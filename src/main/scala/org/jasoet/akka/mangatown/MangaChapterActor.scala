package org.jasoet.akka.mangatown

import akka.actor.{ActorLogging, Actor}
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

class MangaChapterActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case chapter: MangaTownChapter =>
      sender ! grabChapter(chapter)
  }

  def grabChapter(chapter: MangaTownChapter): MangaTownChapter = {
    val start = System.currentTimeMillis()
    log.info(s"Start Grab Detail Chapter  [${chapter.title} - ${chapter.number}] from url[${chapter.url}")
    val pageDoc: Document = Jsoup.connect(chapter.url).get()
    val pageItemsOptions: Elements = pageDoc.select(".manga_read_footer div.page_select>select>option")

    val restPage: List[MangaTownChapterPage] = pageItemsOptions.asScala.par.map { e =>
      val doc = Jsoup.connect(e.attr("value")).get()
      val imgUrl: String = doc.select("#viewer img").attr("src")
      val number: String = e.text()
      log.info(s"Got Chapter  [${chapter.title} - ${chapter.number}] Number[$number] with ImageUrl[$imgUrl]")
      MangaTownChapterPage(number, imgUrl)
    }.toList

    chapter.pages = restPage
    log.info(s"Finish [${System.currentTimeMillis() - start}ms] Grab Detail Chapter  [${chapter.title} - ${chapter.number}] from url[${chapter.url}")
    chapter
  }

}
