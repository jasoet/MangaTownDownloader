package org.jasoet.scala.mangatown

import org.apache.commons.lang3.StringUtils

import scala.collection.mutable.ArrayBuffer
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

object TownScrapMain {
  def main(args: Array[String]): Unit = {
    try {
      val mangaUrl = if (args.length >= 3 && StringUtils.isNotEmpty(args(0))) args(0) else throw new IllegalArgumentException
      val startRange = if (args.length >= 3 && StringUtils.isNumeric(args(1))) args(1).toDouble else throw new IllegalArgumentException
      val endRange = if (args.length >= 3 && StringUtils.isNumeric(args(2))) args(2).toDouble else throw new IllegalArgumentException
      val destination = if (args.length >= 4 && StringUtils.isNotEmpty(args(3))) args(3) else System.getProperty("user.home")

      val mangaTownScrapper = MangaTownScrapper(mangaUrl)
      val st = System.currentTimeMillis()
      val range = Range.Double(startRange, endRange + 1, 0.1)

      val downloadChapters = mangaTownScrapper.chapterList.par.filter { c =>
        Exception.failAsValue(classOf[Exception])(false) {
          range.contains(c.number.toDouble)
        }
      }

      val separator = System.getProperty("file.separator")

      val failedChapter = ArrayBuffer.empty[MangaTownChapter]
      downloadChapters.foreach { c =>
        try {
          println(s"Downloading ${c.mangaTitle} - ${c.number} - ${c.chapterTitle} -  ${c.url}")
          mangaTownScrapper.downloadChapter(c, destination + separator + c.mangaTitle)
        } catch {
          case _: Throwable =>
            println(s"Failed to Download ${c.mangaTitle} - ${c.number} - ${c.chapterTitle} From URL ${c.url}")
            failedChapter += c
        }
      }

      println("Process Takes  " + (System.currentTimeMillis() - st) + " ms")
      println(s"With ${failedChapter.size} failed chapters ")
      failedChapter.sortBy { c =>
        Exception.failAsValue(classOf[Exception])(0.0) {
          c.number.toDouble
        }
      }.foreach { c =>
        println(s"Failed to Download ${c.mangaTitle} - ${c.number} - ${c.chapterTitle} From URL ${c.url}")
      }
    } catch {
      case _: IllegalArgumentException => {
        println(s"Usage `java -jar mangatown-sedoter.jar <URL Manga> <Start Chapter> <End Chapter> <Destination>`")
        println(s"Example `java -jar mangatown-sedoter.jar http://www.mangatown.com/manga/one_piece/ 12 17 /Users/jasoet/Downloads`")
      }
    }

  }
}
