package org.jasoet.scala.mangatown

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
 */

object TownScrapTest {
  def main(args: Array[String]): Unit = {
    val shin = MangaTownScrapper("http://www.mangatown.com/manga/one_piece/")
    val st = System.currentTimeMillis()
    shin.chapterList.foreach { c =>
      println(c.mangaTitle + ", " + c.number + ", " + c.chapterTitle + ", " + c.url)
      println("===================")
    }
    val separator = System.getProperty("file.separator")
    val userHome = System.getProperty("user.home")
    val range = Range.Double(1, 3, 0.1)

    shin.chapterList.par.filter { c =>
      Exception.failAsValue(classOf[Exception])(false) {
        range.contains(c.number.toDouble)
      }
    }.foreach { c =>
      val pageList = shin.chapterPageList(c)
      val chapPath = userHome + separator + c.mangaTitle + separator + c.number + "-" + c.chapterTitle
      println(s"Processing for $chapPath")
      val downloader = ImageDownloader(chapPath)
      pageList.foreach { p =>
        println(s"Downloading page no ${p.number} - ${p.imageUrl}")
        downloader.downloadImage(p.imageUrl, p.number)
      }

    }
    println("List All Page take " + (System.currentTimeMillis() - st) + " ms")


  }
}
