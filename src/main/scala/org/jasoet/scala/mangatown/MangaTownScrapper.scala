package org.jasoet.scala.mangatown

import com.typesafe.scalalogging.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.util.control.Exception

/**
 * Created by Deny Prasetyo,S.T
 * Java(Script) Developer and Trainer
 * Software Engineer
 * jasoet87@gmail.com
 * <p/>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 *
 **/

case class MangaTownChapterPage(number: String, imageUrl: String)

case class MangaTownChapter(mangaTitle: String, chapterTitle: String, number: String, url: String)

class MangaTownScrapper(val url: String) {

  val logger = Logger(LoggerFactory.getLogger(MangaTownChapter.getClass))

  logger.info("Start Processing URL : " + url)
  val _mainPageDoc: Document = Jsoup.connect(this.url).get()
  val _title: String = _mainPageDoc.select(".article_content>h1.title-top").text()
  val _chapterListElements: Elements = _mainPageDoc.select("ul.chapter_list>li")

  logger.info(s"Got Manga Title[${_title}] with ${_chapterListElements.size()} chapters")

  lazy val _chapterList: List[MangaTownChapter] = _chapterListElements.asScala.par.map { e =>
    val chTitle = e.select("span").size() match {
      case 2 => e.select("span").get(0).text()
      case 3 => e.select("span").get(1).text()
      case 4 => e.select("span").get(1).text()
      case _ => ""
    }

    val chNumber = e.select("a").text().replace(_title, "").trim
    val chUrl = e.select("a").attr("href")

    new MangaTownChapter(_title, chTitle, chNumber, chUrl)
  }.toList.sortBy { c =>
    Exception.failAsValue(classOf[Exception])(0.0) {
      c.number.toDouble
    }
  }


  def chapterList: List[MangaTownChapter] = _chapterList

  def title: String = _title

  def chapterPageList(chapter: MangaTownChapter): List[MangaTownChapterPage] = {
    logger.info("Grab Detail Chapter [" + chapter.mangaTitle + "-" + chapter.number + "] from url[" + chapter.url + "]")
    val pageDoc: Document = Jsoup.connect(chapter.url).get()
    val pageItemsOptions: Elements = pageDoc.select(".manga_read_footer div.page_select>select>option")
    val currentImgUrl: String = pageDoc.select("#viewer img").attr("src")
    val currentImgNumber: String = pageItemsOptions.select("[selected]").text()
    logger.info(s"Got Chapter [${chapter.mangaTitle} - ${chapter.number}]  Number[$currentImgNumber] with ImageUrl[$currentImgUrl]")
    val first = new MangaTownChapterPage(currentImgNumber, currentImgUrl)

    val restPage: List[MangaTownChapterPage] = pageItemsOptions.asScala.par.map { e =>
      if (!e.hasAttr("selected")) {
        val doc = Jsoup.connect(e.attr("value")).get()
        val imgUrl: String = doc.select("#viewer img").attr("src")
        val number: String = e.text()
        logger.info(s"Got Chapter  [${chapter.mangaTitle} - ${chapter.number}]   Number[$number] with ImageUrl[$imgUrl]")
        new MangaTownChapterPage(number, imgUrl)
      }
      null
    }.toList

    first :: restPage
  }
}

object MangaTownScrapper {
  def apply(url: String) = new MangaTownScrapper(url)
}