package org.jasoet.scala.mangatown

import java.net.SocketTimeoutException

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

    println("List Chapters take " + (System.currentTimeMillis() - st) + " ms")
    shin.chapterList.par.foreach { c =>
      try {
        shin.chapterPageList(c)
      } catch {
        case ste: SocketTimeoutException => println(ste.getMessage)
      }

    }
    println("List All Page take " + (System.currentTimeMillis() - st) + " ms")


  }
}
