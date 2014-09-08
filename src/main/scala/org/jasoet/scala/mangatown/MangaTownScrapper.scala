package org.jasoet.scala.mangatown

import java.nio.file.Paths

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

  private val logger = Logger(LoggerFactory.getLogger(MangaTownChapter.getClass))

  logger.debug("Start Processing URL : " + url)
  private val _mainPageDoc: Document = Jsoup.connect(this.url).get()
  private val _title: String = _mainPageDoc.select(".article_content>h1.title-top").text()
  private val _chapterListElements: Elements = _mainPageDoc.select("ul.chapter_list>li")

  logger.debug(s"Got Manga Title[${_title}] with ${_chapterListElements.size()} chapters")

  private lazy val _chapterList: List[MangaTownChapter] = _chapterListElements.asScala.par.map { e =>
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
    logger.debug("Grab Detail Chapter [" + chapter.mangaTitle + "-" + chapter.number + "] from url[" + chapter.url + "]")
    val pageDoc: Document = Jsoup.connect(chapter.url).get()
    val pageItemsOptions: Elements = pageDoc.select(".manga_read_footer div.page_select>select>option")
    val currentImgUrl: String = pageDoc.select("#viewer img").attr("src")
    val currentImgNumber: String = pageItemsOptions.select("[selected]").text()
    logger.debug(s"Got Chapter [${chapter.mangaTitle} - ${chapter.number}]  Number[$currentImgNumber] with ImageUrl[$currentImgUrl]")

    val restPage: List[MangaTownChapterPage] = pageItemsOptions.asScala.par.map { e =>
      val doc = Jsoup.connect(e.attr("value")).get()
      val imgUrl: String = doc.select("#viewer img").attr("src")
      val number: String = e.text()
      logger.debug(s"Got Chapter  [${chapter.mangaTitle} - ${chapter.number}]   Number[$number] with ImageUrl[$imgUrl]")
      if (number.equalsIgnoreCase(currentImgNumber)) {
        new MangaTownChapterPage(number, currentImgUrl)
      } else {
        new MangaTownChapterPage(number, imgUrl)
      }

    }.toList

    restPage
  }

  def downloadChapter(chapter: MangaTownChapter, destination: String): Unit = {
    val chapterList = chapterPageList(chapter)
    val separator = System.getProperty("file.separator")
    val chapPath = destination + separator + chapter.number + "-" + chapter.chapterTitle
    val downloader = ImageDownloader(chapPath)
    chapterList.par.foreach { p =>
      downloader.downloadImage(p.imageUrl, p.number)
    }
    logger.debug("Downloading Image Finished. Zipping... ")
    val filePaths = ZipArchiveUtil.createFileList(Paths.get(chapPath).toFile, s"${chapter.number}-${chapter.chapterTitle}")
    ZipArchiveUtil.createZip(filePaths,  s"${chapter.number}-${chapter.chapterTitle}.cbr", destination)
  }
}

object MangaTownScrapper {
  def apply(url: String) = new MangaTownScrapper(url)
}