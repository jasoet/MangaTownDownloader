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
    val range = Range.Double(2, 3, 0.1)

    val downloadChapters = shin.chapterList.par.filter { c =>
      Exception.failAsValue(classOf[Exception])(false) {
        range.contains(c.number.toDouble)
      }
    }

    val separator = System.getProperty("file.separator")
    val userHome = System.getProperty("user.home")

    downloadChapters.foreach { c =>
      println(s"Downloading ${c.mangaTitle} - ${c.number} - ${c.chapterTitle} -  ${c.url}")
      shin.downloadChapter(c, userHome + separator + "manga" + separator + c.mangaTitle)
    }
    println("Process Takes  " + (System.currentTimeMillis() - st) + " ms")


  }
}
