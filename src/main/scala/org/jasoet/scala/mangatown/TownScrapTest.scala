package org.jasoet.scala.mangatown

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

object TownScrapTest {
  def main(args: Array[String]): Unit = {
    val mangaTownScrapper = MangaTownScrapper("http://www.mangatown.com/manga/one_piece/")
    val st = System.currentTimeMillis()
    val range = Range.Double(200, 400, 0.1)

    val downloadChapters = mangaTownScrapper.chapterList.par.filter { c =>
      Exception.failAsValue(classOf[Exception])(false) {
        range.contains(c.number.toDouble)
      }
    }

    val separator = System.getProperty("file.separator")
    val userHome = System.getProperty("user.home")

    val failedChapter = ArrayBuffer.empty[MangaTownChapter]
    downloadChapters.foreach { c =>
      try {
        println(s"Downloading ${c.mangaTitle} - ${c.number} - ${c.chapterTitle} -  ${c.url}")
        mangaTownScrapper.downloadChapter(c, userHome + separator + "manga" + separator + c.mangaTitle)
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

  }
}
